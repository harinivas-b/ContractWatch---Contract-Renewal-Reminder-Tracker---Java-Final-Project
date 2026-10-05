package com.contractwatch.Controller;

import com.contractwatch.Entity.Vendor;
import com.contractwatch.Service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
public class VendorController {
    @Autowired
    VendorService VendorService;

    @PostMapping
    public ResponseEntity<Vendor> createVendor(@RequestBody Vendor vendor) {
        VendorService.createVendor(vendor);
        return new ResponseEntity<>(vendor, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Vendor>> getAllVendors() {
        return new ResponseEntity<>(VendorService.getAllVendors(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vendor> getVendor(@PathVariable int id) {
        return new ResponseEntity<>(VendorService.getVendor(id), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Vendor> updateVendor(@PathVariable int id, @RequestBody Vendor vendor) {
        vendor.setId(id);
        return new ResponseEntity<>(VendorService.updateVendor(vendor), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteVendor(@PathVariable int id) {
        VendorService.deleteVendor(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
