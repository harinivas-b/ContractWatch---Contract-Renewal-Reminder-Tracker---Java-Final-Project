package com.contractwatch.service;

import com.contractwatch.dto.ContractRequest;
import com.contractwatch.dto.DocumentReferenceRequest;
import com.contractwatch.dto.RenewalDecisionRequest;
import com.contractwatch.entity.Contract;
import com.contractwatch.entity.ContractDocumentReference;
import com.contractwatch.entity.RenewalDecision;
import com.contractwatch.entity.Vendor;
import com.contractwatch.repository.ContractDocumentReferenceRepository;
import com.contractwatch.repository.ContractRepository;
import com.contractwatch.repository.RenewalDecisionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ContractService
{
    private final ContractRepository repository;
    private final VendorService vendorService;
    private final RenewalDecisionRepository renewalDecisionRepository;
    private final ContractDocumentReferenceRepository documentRepository;

    public ContractService(
            ContractRepository repository,
            VendorService vendorService,
            RenewalDecisionRepository renewalDecisionRepository,
            ContractDocumentReferenceRepository documentRepository)
    {
        this.repository = repository;
        this.vendorService = vendorService;
        this.renewalDecisionRepository = renewalDecisionRepository;
        this.documentRepository = documentRepository;
    }

    public List<Contract> findAll()
    {
        refreshRenewalFlags();
        return repository.findAll();
    }

    public Contract findById(Long id)
    {
        refreshRenewalFlags();

        return repository.findById(id)
                .orElseThrow(
                        () -> new ContractNotFoundException(id)
                );
    }

    @Transactional
    public Contract create(ContractRequest request)
    {
        validateDates(request.getContractStartDate(), request.getEndDate());
        validateNoticePeriod(request.getRenewalNoticePeriodDays());

        Vendor vendor = vendorService.findById(
                request.getVendorId()
        );

        Contract contract = new Contract();

        copyRequestToContract(
                request,
                contract,
                vendor
        );

        refreshRenewalFlag(contract);

        return repository.save(contract);
    }

    @Transactional
    public Contract update(Long id, ContractRequest request)
    {
        Contract current = findById(id);

        validateDates(request.getContractStartDate(), request.getEndDate());
        validateNoticePeriod(request.getRenewalNoticePeriodDays());

        Vendor vendor = vendorService.findById(
                request.getVendorId()
        );

        copyRequestToContract(
                request,
                current,
                vendor
        );

        refreshRenewalFlag(current);

        return repository.save(current);
    }

    @Transactional
    public void delete(Long id)
    {
        Contract contract = findById(id);

        renewalDecisionRepository.deleteByContractId(id);
        documentRepository.deleteByContractId(id);

        repository.delete(contract);
    }

    public List<Contract> expiringWithinDays(int days)
    {
        validateDays(days);
        refreshRenewalFlags();

        return repository.findByEndDateBetweenAndStatusIgnoreCase(
                LocalDate.now(),
                LocalDate.now().plusDays(days),
                "ACTIVE"
        );
    }

    public List<Contract> renewalReviewContracts()
    {
        refreshRenewalFlags();

        return repository
                .findByStatusIgnoreCaseAndRenewalReviewFlagTrueOrderByEndDateAsc(
                        "ACTIVE"
                );
    }

    public long countActive()
    {
        refreshRenewalFlags();
        return repository.countByStatusIgnoreCase("ACTIVE");
    }

    @Transactional
    public RenewalDecision recordRenewalDecision(
            Long contractId,
            RenewalDecisionRequest request)
    {
        Contract contract = findById(contractId);

        String decision = request.getDecision();

        if (decision == null || decision.isBlank())
        {
            throw new IllegalArgumentException(
                    "Decision is required"
            );
        }

        decision = decision.trim().toUpperCase();

        if (!decision.equals("RENEWED") &&
                !decision.equals("TERMINATED"))
        {
            throw new IllegalArgumentException(
                    "Decision must be RENEWED or TERMINATED"
            );
        }

        RenewalDecision renewalDecision =
                new RenewalDecision();

        renewalDecision.setContract(contract);
        renewalDecision.setDecision(decision);
        renewalDecision.setDecisionDate(LocalDate.now());
        renewalDecision.setNotes(request.getNotes());

        if (decision.equals("RENEWED"))
        {
            if (request.getNewEndDate() == null)
            {
                throw new IllegalArgumentException(
                        "New end date is required when a contract is renewed"
                );
            }

            if (!request.getNewEndDate()
                    .isAfter(contract.getEndDate()))
            {
                throw new IllegalArgumentException(
                        "New end date must be after the current end date"
                );
            }

            if (request.getNewEndDate()
                    .isBefore(contract.getContractStartDate()))
            {
                throw new IllegalArgumentException(
                        "New end date cannot be before the contract start date"
                );
            }

            renewalDecision.setNewEndDate(
                    request.getNewEndDate()
            );

            contract.setEndDate(
                    request.getNewEndDate()
            );

            contract.setStatus("ACTIVE");
            contract.setRenewalReviewFlag(false);
            refreshRenewalFlag(contract);
        }
        else
        {
            contract.setStatus("TERMINATED");
            contract.setRenewalReviewFlag(false);
        }

        repository.save(contract);

        return renewalDecisionRepository.save(
                renewalDecision
        );
    }

    public List<RenewalDecision> renewalDecisions(Long contractId)
    {
        findById(contractId);

        return renewalDecisionRepository
                .findByContractIdOrderByDecisionDateDesc(
                        contractId
                );
    }

    @Transactional
    public ContractDocumentReference addDocumentReference(
            Long contractId,
            DocumentReferenceRequest request)
    {
        Contract contract = findById(contractId);

        if (request.getTitle() == null ||
                request.getTitle().isBlank())
        {
            throw new IllegalArgumentException(
                    "Document title is required"
            );
        }

        if (request.getReferenceUrl() == null ||
                request.getReferenceUrl().isBlank())
        {
            throw new IllegalArgumentException(
                    "Document reference URL is required"
            );
        }

        ContractDocumentReference reference =
                new ContractDocumentReference();

        reference.setContract(contract);
        reference.setTitle(request.getTitle());
        reference.setReferenceUrl(
                request.getReferenceUrl()
        );
        reference.setDescription(
                request.getDescription()
        );

        return documentRepository.save(reference);
    }

    public List<ContractDocumentReference> documentReferences(
            Long contractId)
    {
        findById(contractId);

        return documentRepository
                .findByContractIdOrderByCreatedAtDesc(
                        contractId
                );
    }

    @Transactional
    public void deleteDocumentReference(Long documentId)
    {
        documentRepository.deleteById(documentId);
    }

    @Transactional
    public void refreshRenewalFlags()
    {
        List<Contract> contracts = repository.findAll();
        LocalDate today = LocalDate.now();

        for (Contract contract : contracts)
        {
            if (contract.getEndDate() == null)
            {
                continue;
            }

            boolean changed = false;

            if ("ACTIVE".equalsIgnoreCase(
                    contract.getStatus()))
            {
                if (today.isAfter(contract.getEndDate()))
                {
                    contract.setStatus("EXPIRED");
                    contract.setRenewalReviewFlag(false);
                    changed = true;
                }
                else
                {
                    boolean before =
                            contract.isRenewalReviewFlag();

                    refreshRenewalFlag(contract);

                    changed =
                            before != contract.isRenewalReviewFlag();
                }
            }
            else if (contract.isRenewalReviewFlag())
            {
                contract.setRenewalReviewFlag(false);
                changed = true;
            }

            if (changed)
            {
                repository.save(contract);
            }
        }
    }

    private void refreshRenewalFlag(Contract contract)
    {
        if (!"ACTIVE".equalsIgnoreCase(
                contract.getStatus()))
        {
            contract.setRenewalReviewFlag(false);
            return;
        }

        LocalDate noticeStart =
                contract.getEndDate().minusDays(
                        contract.getRenewalNoticePeriodDays()
                );

        LocalDate today = LocalDate.now();

        boolean inNoticeWindow =
                !today.isBefore(noticeStart) &&
                !today.isAfter(contract.getEndDate());

        contract.setRenewalReviewFlag(
                inNoticeWindow
        );
    }

    private void copyRequestToContract(
            ContractRequest request,
            Contract contract,
            Vendor vendor)
    {
        contract.setVendor(vendor);
        contract.setServiceName(
                request.getServiceName()
        );
        contract.setContractStartDate(
                request.getContractStartDate()
        );
        contract.setEndDate(
                request.getEndDate()
        );
        contract.setRenewalNoticePeriodDays(
                request.getRenewalNoticePeriodDays()
        );
        contract.setContactEmail(
                request.getContactEmail()
        );
        contract.setStatus(
                request.getStatus() == null ||
                        request.getStatus().isBlank()
                        ? "ACTIVE"
                        : request.getStatus().trim().toUpperCase()
        );
        contract.setNotes(
                request.getNotes()
        );
    }

    private void validateDates(
            LocalDate startDate,
            LocalDate endDate)
    {
        if (startDate == null || endDate == null)
        {
            throw new IllegalArgumentException(
                    "Contract start date and end date are required"
            );
        }

        if (endDate.isBefore(startDate))
        {
            throw new IllegalArgumentException(
                    "End date cannot be before contract start date"
            );
        }
    }

    private void validateNoticePeriod(int days)
    {
        if (days < 0 || days > 3650)
        {
            throw new IllegalArgumentException(
                    "Renewal notice period must be between 0 and 3650 days"
            );
        }
    }

    private void validateDays(int days)
    {
        if (days < 0 || days > 365)
        {
            throw new IllegalArgumentException(
                    "days must be between 0 and 365"
            );
        }
    }
}
