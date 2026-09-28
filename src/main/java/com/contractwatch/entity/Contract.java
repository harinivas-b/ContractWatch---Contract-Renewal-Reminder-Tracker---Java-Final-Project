package com.contractwatch.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "contracts")
public class Contract
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "vendor_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Vendor vendor;

    @NotBlank
    private String serviceName;

    @NotNull
    private LocalDate contractStartDate;

    @NotNull
    private LocalDate endDate;

    @Min(0)
    private int renewalNoticePeriodDays = 30;

    private boolean renewalReviewFlag;

    @Email
    private String contactEmail;

    private String status = "ACTIVE";

    @Column(length = 1500)
    private String notes;

    public Contract()
    {
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Vendor getVendor()
    {
        return vendor;
    }

    public void setVendor(Vendor vendor)
    {
        this.vendor = vendor;
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

    public boolean isRenewalReviewFlag()
    {
        return renewalReviewFlag;
    }

    public void setRenewalReviewFlag(boolean renewalReviewFlag)
    {
        this.renewalReviewFlag = renewalReviewFlag;
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
