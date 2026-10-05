package com.contractwatch.Repository;

import com.contractwatch.Entity.RenewalDecision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RenewalDecisionRepo extends JpaRepository<RenewalDecision, Integer> {

    List<RenewalDecision> findByContractId(int contractId);
}