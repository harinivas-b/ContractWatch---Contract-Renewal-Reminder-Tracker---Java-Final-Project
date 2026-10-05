package com.contractwatch.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int vendorId;
    private String serviceName;
    private LocalDate contractStartDate;
    private LocalDate endDate;
    private int renewalNoticePeriodDays;
    private boolean renewalReviewFlag;
    private String contactEmail;
    private String status;
    private String notes;

    public Contract() { }

    public Contract(int id, int vendorId, String serviceName, LocalDate contractStartDate,
                    LocalDate endDate, int renewalNoticePeriodDays, boolean renewalReviewFlag,
                    String contactEmail, String status, String notes) {
        this.id = id;
        this.vendorId = vendorId;
        this.serviceName = serviceName;
        this.contractStartDate = contractStartDate;
        this.endDate = endDate;
        this.renewalNoticePeriodDays = renewalNoticePeriodDays;
        this.renewalReviewFlag = renewalReviewFlag;
        this.contactEmail = contactEmail;
        this.status = status;
        this.notes = notes;
    }

    public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
    }
    public int getVendorId()
    {
        return vendorId;
    }
    public void setVendorId(int vendorId)
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
