package com.contractwatch.Controller;

import com.contractwatch.Entity.ContractDocumentReference;
import com.contractwatch.Service.ContractDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contractdocument")
public class ContractDocumentController {

    @Autowired
    ContractDocumentService ContractDocumentService;

    @PostMapping("/{id}")
    public ResponseEntity<ContractDocumentReference> addDocumentReference(
            @PathVariable int id,
            @RequestBody ContractDocumentReference reference) {

        ContractDocumentReference saved =
                ContractDocumentService.addDocumentReference(id, reference);

        if (saved == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<ContractDocumentReference>> getDocumentReferences(
            @PathVariable int id) {

        return new ResponseEntity<>(
                ContractDocumentService.getDocumentReferences(id),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteDocumentReference(
            @PathVariable int id) {

        ContractDocumentService.deleteDocumentReference(id);

        return new ResponseEntity<>(
                "Document reference deleted successfully",
                HttpStatus.OK
        );
    }
}