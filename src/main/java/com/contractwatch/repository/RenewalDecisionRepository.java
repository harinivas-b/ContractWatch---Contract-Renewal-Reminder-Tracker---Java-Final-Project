package com.contractwatch.repository;

import com.contractwatch.entity.RenewalDecision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RenewalDecisionRepository
        extends JpaRepository<RenewalDecision, Long>
{
    List<RenewalDecision> findByContractIdOrderByDecisionDateDesc(
            Long contractId
    );

    void deleteByContractId(Long contractId);
}
