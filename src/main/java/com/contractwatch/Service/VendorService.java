package com.contractwatch.Service;

import com.contractwatch.Entity.Vendor;
import com.contractwatch.Repository.VendorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorService {
    @Autowired
    VendorRepo VendorRepo;

    public Vendor createVendor(Vendor vendor)
    {
        return VendorRepo.save(vendor);
    }
    public List<Vendor> getAllVendors()
    {
        return VendorRepo.findAll();
    }
    public Vendor getVendor(int id)
    {
        return VendorRepo.findById(id).orElse(null);
    }
    public Vendor updateVendor(Vendor vendor)
    {
        return VendorRepo.save(vendor);
    }
    public void deleteVendor(int id)
    {
        VendorRepo.deleteById(id);
    }
}
