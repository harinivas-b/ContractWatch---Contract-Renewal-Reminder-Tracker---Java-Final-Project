package com.contractwatch.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class ContractRequest
{
    @NotNull
    private Long vendorId;

    @NotBlank
    private String serviceName;

    @NotNull
    private LocalDate contractStartDate;

    @NotNull
    private LocalDate endDate;

    @Min(0)
    private int renewalNoticePeriodDays = 30;

    @Email
    private String contactEmail;

    private String status = "ACTIVE";

    private String notes;

    public ContractRequest()
    {
    }

    public Long getVendorId()
    {
        return vendorId;
    }

    public void setVendorId(Long vendorId)
    {
        this.vendorId = vendorId;
    }

    public String getServiceName()
    {
        return serviceName;
    }

    public void setServiceName(String serviceName)
    {
        this.serviceName = serviceName;
    }

    public LocalDate getContractStartDate()
    {
        return contractStartDate;
    }

    public void setContractStartDate(LocalDate contractStartDate)
    {
        this.contractStartDate = contractStartDate;
    }

    public LocalDate getEndDate()
    {
        return endDate;
    }

    public void setEndDate(LocalDate endDate)
    {
        this.endDate = endDate;
    }

    public int getRenewalNoticePeriodDays()
    {
        return renewalNoticePeriodDays;
    }

    public void setRenewalNoticePeriodDays(int renewalNoticePeriodDays)
    {
        this.renewalNoticePeriodDays = renewalNoticePeriodDays;
    }

    public String getContactEmail()
    {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail)
    {
        this.contactEmail = contactEmail;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getNotes()
    {
        return notes;
    }

    public void setNotes(String notes)
    {
        this.notes = notes;
    }
}
