package com.contractwatch.Controller;

import com.contractwatch.Entity.RenewalDecision;
import com.contractwatch.Entity.Contract;
import com.contractwatch.Service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/renewal")
public class RenewalController {

    @Autowired
    ContractService ContractService;

    @GetMapping("/review")
    public ResponseEntity<List<Contract>> renewalReview() {
        return new ResponseEntity<>(
                ContractService.renewalReviewContracts(),
                HttpStatus.OK
        );
    }

    @PostMapping("/{id}/decision")
    public ResponseEntity<RenewalDecision> recordRenewalDecision(
            @PathVariable int id,
            @RequestBody RenewalDecision decision) {

        RenewalDecision saved =
                ContractService.recordRenewalDecision(id, decision);

        if (saved == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    @GetMapping("/{id}/decisions")
    public ResponseEntity<List<RenewalDecision>> renewalDecisions(
            @PathVariable int id) {

        return new ResponseEntity<>(
                ContractService.renewalDecisions(id),
                HttpStatus.OK
        );
    }
}