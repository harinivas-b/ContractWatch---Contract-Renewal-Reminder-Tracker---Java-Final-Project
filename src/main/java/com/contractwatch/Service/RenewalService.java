package com.contractwatch.Service;

import com.contractwatch.Entity.Contract;
import com.contractwatch.Entity.RenewalDecision;
import com.contractwatch.Repository.ContractRepo;
import com.contractwatch.Repository.RenewalDecisionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RenewalService {

    @Autowired
    ContractRepo ContractRepo;

    @Autowired
    RenewalDecisionRepo RenewalDecisionRepo;

    public RenewalDecision recordRenewalDecision(
            int contractId,
            RenewalDecision decision) {

        Contract contract = ContractRepo.findById(contractId).orElse(null);

        if (contract == null) {
            return null;
        }

        decision.setContractId(contractId);

        return RenewalDecisionRepo.save(decision);
    }

    public List<RenewalDecision> renewalDecisions(
            int contractId) {

        return RenewalDecisionRepo.findByContractId(contractId);
    }
}