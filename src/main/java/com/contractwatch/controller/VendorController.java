package com.contractwatch.controller;

import com.contractwatch.entity.Vendor;
import com.contractwatch.service.VendorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
@CrossOrigin
public class VendorController
{
    @Autowired
    VendorService VendorService;

    @GetMapping
    public ResponseEntity<List<Vendor>> getAllVendors()
    {
        List<Vendor> vendors =
                VendorService.findAll();

        return new ResponseEntity<>(
                vendors,
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<Vendor> createVendor(
            @Valid @RequestBody Vendor vendor)
    {
        Vendor savedVendor =
                VendorService.create(vendor);

        return new ResponseEntity<>(
                savedVendor,
                HttpStatus.CREATED
        );
    }
}
