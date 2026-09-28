package com.contractwatch.controller;

import com.contractwatch.dto.ContractRequest;
import com.contractwatch.dto.DocumentReferenceRequest;
import com.contractwatch.dto.RenewalDecisionRequest;
import com.contractwatch.entity.Contract;
import com.contractwatch.entity.ContractDocumentReference;
import com.contractwatch.entity.RenewalDecision;
import com.contractwatch.service.ContractService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/contracts")
@CrossOrigin
public class ContractController
{
    @Autowired
    ContractService ContractService;

    @GetMapping
    public ResponseEntity<List<Contract>> getAllContracts()
    {
        List<Contract> contracts =
                ContractService.findAll();

        return new ResponseEntity<>(
                contracts,
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contract> getContract(
            @PathVariable Long id)
    {
        Contract contract =
                ContractService.findById(id);

        return new ResponseEntity<>(
                contract,
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<Contract> createContract(
            @Valid @RequestBody ContractRequest request)
    {
        Contract contract =
                ContractService.create(request);

        return new ResponseEntity<>(
                contract,
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contract> updateContract(
            @PathVariable Long id,
            @Valid @RequestBody ContractRequest request)
    {
        Contract contract =
                ContractService.update(id, request);

        return new ResponseEntity<>(
                contract,
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContract(
            @PathVariable Long id)
    {
        ContractService.delete(id);

        return new ResponseEntity<>(
                HttpStatus.NO_CONTENT
        );
    }

    @GetMapping("/expiring")
    public ResponseEntity<List<Contract>> expiring(
            @RequestParam(defaultValue = "30") int days)
    {
        List<Contract> contracts =
                ContractService.expiringWithinDays(days);

        return new ResponseEntity<>(
                contracts,
                HttpStatus.OK
        );
    }

    @GetMapping("/renewal-review")
    public ResponseEntity<List<Contract>> renewalReview()
    {
        List<Contract> contracts =
                ContractService.renewalReviewContracts();

        return new ResponseEntity<>(
                contracts,
                HttpStatus.OK
        );
    }

    @PostMapping("/{id}/renewal-decision")
    public ResponseEntity<RenewalDecision> recordRenewalDecision(
            @PathVariable Long id,
            @RequestBody RenewalDecisionRequest request)
    {
        RenewalDecision decision =
                ContractService.recordRenewalDecision(
                        id,
                        request
                );

        return new ResponseEntity<>(
                decision,
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}/renewal-decisions")
    public ResponseEntity<List<RenewalDecision>> renewalDecisions(
            @PathVariable Long id)
    {
        List<RenewalDecision> decisions =
                ContractService.renewalDecisions(id);

        return new ResponseEntity<>(
                decisions,
                HttpStatus.OK
        );
    }

    @PostMapping("/{id}/documents")
    public ResponseEntity<ContractDocumentReference> addDocumentReference(
            @PathVariable Long id,
            @RequestBody DocumentReferenceRequest request)
    {
        ContractDocumentReference reference =
                ContractService.addDocumentReference(
                        id,
                        request
                );

        return new ResponseEntity<>(
                reference,
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}/documents")
    public ResponseEntity<List<ContractDocumentReference>> documentReferences(
            @PathVariable Long id)
    {
        List<ContractDocumentReference> references =
                ContractService.documentReferences(id);

        return new ResponseEntity<>(
                references,
                HttpStatus.OK
        );
    }

    @DeleteMapping("/documents/{documentId}")
    public ResponseEntity<Void> deleteDocumentReference(
            @PathVariable Long documentId)
    {
        ContractService.deleteDocumentReference(
                documentId
        );

        return new ResponseEntity<>(
                HttpStatus.NO_CONTENT
        );
    }
}
