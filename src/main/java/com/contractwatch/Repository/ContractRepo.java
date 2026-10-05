package com.contractwatch.Repository;

import com.contractwatch.Entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractRepo extends JpaRepository<Contract, Integer> {
}
