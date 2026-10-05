package com.contractwatch.Service;

import com.contractwatch.Entity.ContractDocumentReference;
import com.contractwatch.Repository.ContractDocumentReferenceRepo;
import com.contractwatch.Repository.ContractRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContractDocumentService {

    @Autowired
    ContractDocumentReferenceRepo ContractDocumentReferenceRepo;

    @Autowired
    ContractRepo ContractRepo;

    public ContractDocumentReference addDocumentReference(
            int contractId,
            ContractDocumentReference reference) {

        if (ContractRepo.findById(contractId).orElse(null) == null) {
            return null;
        }

        reference.setContractId(contractId);

        if (reference.getCreatedAt() == null) {
            reference.setCreatedAt(LocalDateTime.now());
        }

        return ContractDocumentReferenceRepo.save(reference);
    }

    public List<ContractDocumentReference> getDocumentReferences(
            int contractId) {

        if (ContractRepo.findById(contractId).orElse(null) == null) {
            return new ArrayList<>();
        }

        return ContractDocumentReferenceRepo.findByContractId(contractId);
    }

    public void deleteDocumentReference(int id) {
        ContractDocumentReferenceRepo.deleteById(id);
    }
}