package com.contractwatch.repository;

import com.contractwatch.entity.ContractDocumentReference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContractDocumentReferenceRepository
        extends JpaRepository<ContractDocumentReference, Long>
{
    List<ContractDocumentReference> findByContractIdOrderByCreatedAtDesc(
            Long contractId
    );

    void deleteByContractId(Long contractId);
}
