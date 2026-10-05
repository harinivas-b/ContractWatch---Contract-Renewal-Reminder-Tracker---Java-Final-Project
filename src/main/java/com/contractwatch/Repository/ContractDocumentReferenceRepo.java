package com.contractwatch.Repository;

import com.contractwatch.Entity.ContractDocumentReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractDocumentReferenceRepo
        extends JpaRepository<ContractDocumentReference, Integer> {

    List<ContractDocumentReference> findByContractId(int contractId);
}