package com.contractwatch.service;

public class VendorNotFoundException extends RuntimeException
{
    public VendorNotFoundException(Long id)
    {
        super("Vendor not found with id: " + id);
    }
}
