package com.contractwatch.controller;

import com.contractwatch.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
public class DashboardController
{
    @Autowired
    ContractService ContractService;

    @GetMapping("/api/dashboard")
    public ResponseEntity<Map<String, Object>> dashboard()
    {
        int totalContracts =
                ContractService.findAll().size();

        long activeContracts =
                ContractService.countActive();

        int expiringIn30Days =
                ContractService.expiringWithinDays(30).size();

        int renewalReviewContracts =
                ContractService.renewalReviewContracts().size();

        String asOfDate =
                LocalDate.now().toString();

        Map<String, Object> dashboardData =
                Map.of(
                        "totalContracts", totalContracts,
                        "activeContracts", activeContracts,
                        "expiringIn30Days", expiringIn30Days,
                        "renewalReviewContracts", renewalReviewContracts,
                        "asOfDate", asOfDate
                );

        return new ResponseEntity<>(
                dashboardData,
                HttpStatus.OK
        );
    }
}
