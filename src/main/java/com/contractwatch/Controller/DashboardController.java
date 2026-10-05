package com.contractwatch.Controller;

import com.contractwatch.Service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
public class DashboardController {
    @Autowired
    ContractService ContractService;

    @GetMapping("/api/dashboard")
    public ResponseEntity<Map<String, Object>> dashboard() {
        Map<String, Object> dashboard = Map.of(
                "totalContracts", ContractService.getAllContracts().size(),
                "activeContracts", ContractService.countActive(),
                "expiringIn30Days", ContractService.expiringWithinDays(30).size(),
                "renewalReviewContracts", ContractService.renewalReviewContracts().size(),
                "asOfDate", LocalDate.now().toString()
        );
        return new ResponseEntity<>(dashboard, HttpStatus.OK);
    }
}
