package com.contractwatch.service;

import com.contractwatch.entity.Vendor;
import com.contractwatch.repository.VendorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorService
{
    private final VendorRepository repository;

    public VendorService(VendorRepository repository)
    {
        this.repository = repository;
    }

    public List<Vendor> findAll()
    {
        return repository.findAll();
    }

    public Vendor findById(Long id)
    {
        return repository.findById(id)
                .orElseThrow(
                        () -> new VendorNotFoundException(id)
                );
    }

    public Vendor create(Vendor vendor)
    {
        return repository.save(vendor);
    }
}
