package com.contractwatch.repository;

import com.contractwatch.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ContractRepository
        extends JpaRepository<Contract, Long>
{
    List<Contract> findByEndDateBetweenAndStatusIgnoreCase(
            LocalDate start,
            LocalDate end,
            String status
    );

    List<Contract> findByStatusIgnoreCaseAndRenewalReviewFlagTrueOrderByEndDateAsc(
            String status
    );

    long countByStatusIgnoreCase(String status);
}
