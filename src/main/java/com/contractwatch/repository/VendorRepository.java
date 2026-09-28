package com.contractwatch.repository;

import com.contractwatch.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorRepository
        extends JpaRepository<Vendor, Long>
{
}
