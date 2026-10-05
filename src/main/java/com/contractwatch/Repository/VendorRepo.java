package com.contractwatch.Repository;

import com.contractwatch.Entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorRepo extends JpaRepository<Vendor, Integer> {
}
