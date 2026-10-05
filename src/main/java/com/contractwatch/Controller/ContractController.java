package com.contractwatch.Controller;

import com.contractwatch.Entity.Contract;
import com.contractwatch.Entity.RenewalDecision;
import com.contractwatch.Service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contracts")
public class ContractController {
    @Autowired
    ContractService ContractService;

    @PostMapping("/create")
    public ResponseEntity<Contract> createContract(@RequestBody Contract contract) {
        ContractService.createContract(contract);
        return new ResponseEntity<>(contract, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Contract>> getAllContracts() {
        return new ResponseEntity<>(ContractService.getAllContracts(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contract> getContract(@PathVariable int id) {
        return new ResponseEntity<>(ContractService.getContract(id), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Contract> updateContract(@PathVariable int id, @RequestBody Contract contract) {
        contract.setId(id);
        return new ResponseEntity<>(ContractService.updateContract(contract), HttpStatus.OK);
    }

    @GetMapping("/expiring")
    public ResponseEntity<List<Contract>> expiring(@RequestParam(defaultValue = "30") int days) {
        return new ResponseEntity<>(ContractService.expiringWithinDays(days), HttpStatus.OK);
    }

    @GetMapping("/renewal-review")
    public ResponseEntity<List<Contract>> renewalReview() {
        return new ResponseEntity<>(ContractService.renewalReviewContracts(), HttpStatus.OK);
    }

    @PostMapping("/{id}/renewal-decision")
    public ResponseEntity<RenewalDecision> recordRenewalDecision(@PathVariable int id,
                                                                  @RequestBody RenewalDecision decision) {
        RenewalDecision saved = ContractService.recordRenewalDecision(id, decision);
        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

}