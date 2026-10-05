package com.contractwatch.Service;

import com.contractwatch.Entity.Contract;
import com.contractwatch.Entity.ContractDocumentReference;
import com.contractwatch.Entity.RenewalDecision;
import com.contractwatch.Repository.ContractDocumentReferenceRepo;
import com.contractwatch.Repository.ContractRepo;
import com.contractwatch.Repository.RenewalDecisionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContractService {

    @Autowired
    ContractRepo ContractRepo;

    @Autowired
    RenewalDecisionRepo RenewalDecisionRepo;

    @Autowired
    ContractDocumentReferenceRepo ContractDocumentReferenceRepo;

    public Contract createContract(Contract contract) {
        prepareContract(contract);
        return ContractRepo.save(contract);
    }

    public List<Contract> getAllContracts() {
        List<Contract> contracts = ContractRepo.findAll();

        for (Contract contract : contracts) {
            refreshContract(contract);
        }

        return ContractRepo.saveAll(contracts);
    }

    public Contract getContract(int id) {
        Contract contract =
                ContractRepo.findById(id).orElse(null);

        if (contract != null) {
            refreshContract(contract);
            ContractRepo.save(contract);
        }

        return contract;
    }

    public Contract updateContract(Contract contract) {
        prepareContract(contract);
        return ContractRepo.save(contract);
    }

    public List<Contract> expiringWithinDays(int days) {

        List<Contract> result = new ArrayList<>();

        LocalDate today = LocalDate.now();
        LocalDate limit = today.plusDays(days);

        for (Contract contract : getAllContracts()) {

            if ("ACTIVE".equalsIgnoreCase(contract.getStatus())
                    && contract.getEndDate() != null
                    && !contract.getEndDate().isBefore(today)
                    && !contract.getEndDate().isAfter(limit)) {

                result.add(contract);
            }
        }

        return result;
    }

    public List<Contract> renewalReviewContracts() {

        List<Contract> result = new ArrayList<>();

        for (Contract contract : getAllContracts()) {

            if (contract.isRenewalReviewFlag()) {
                result.add(contract);
            }
        }

        return result;
    }

    public RenewalDecision recordRenewalDecision(
            int id,
            RenewalDecision decision) {

        Contract contract =
                ContractRepo.findById(id).orElse(null);

        if (contract == null) {
            return null;
        }

        String value =
                decision.getDecision() == null
                        ? ""
                        : decision.getDecision()
                        .trim()
                        .toUpperCase();

        if ("RENEWED".equals(value)) {

            if (decision.getNewEndDate() == null) {
                return null;
            }

            contract.setEndDate(
                    decision.getNewEndDate()
            );

            contract.setStatus("ACTIVE");

        } else if ("TERMINATED".equals(value)) {

            contract.setStatus("TERMINATED");

        } else {

            return null;
        }

        contract.setRenewalReviewFlag(false);

        ContractRepo.save(contract);

        decision.setContractId(id);
        decision.setDecision(value);

        if (decision.getDecisionDate() == null) {
            decision.setDecisionDate(LocalDate.now());
        }

        return RenewalDecisionRepo.save(decision);
    }

    public List<RenewalDecision> renewalDecisions(int id) {

        Contract contract =
                ContractRepo.findById(id).orElse(null);

        if (contract == null) {
            return new ArrayList<>();
        }

        return RenewalDecisionRepo.findByContractId(id);
    }

    public long countActive() {

        long count = 0;

        for (Contract contract : getAllContracts()) {

            if ("ACTIVE".equalsIgnoreCase(
                    contract.getStatus())) {

                count++;
            }
        }

        return count;
    }

    private void prepareContract(Contract contract) {

        if (contract.getStatus() == null
                || contract.getStatus().isBlank()) {

            contract.setStatus("ACTIVE");
        }

        if (contract.getRenewalNoticePeriodDays() < 0) {
            contract.setRenewalNoticePeriodDays(0);
        }

        refreshContract(contract);
    }

    private void refreshContract(Contract contract) {

        if (contract.getEndDate() == null) {
            return;
        }

        LocalDate today = LocalDate.now();

        if ("ACTIVE".equalsIgnoreCase(contract.getStatus())
                && contract.getEndDate().isBefore(today)) {

            contract.setStatus("EXPIRED");
            contract.setRenewalReviewFlag(false);

            return;
        }

        if (!"ACTIVE".equalsIgnoreCase(
                contract.getStatus())) {

            contract.setRenewalReviewFlag(false);

            return;
        }

        LocalDate noticeStart =
                contract.getEndDate().minusDays(
                        contract.getRenewalNoticePeriodDays()
                );

        contract.setRenewalReviewFlag(
                !today.isBefore(noticeStart)
                        && !today.isAfter(contract.getEndDate())
        );
    }
}