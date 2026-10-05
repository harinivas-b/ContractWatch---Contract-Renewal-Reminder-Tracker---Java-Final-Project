package com.contractwatch.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class ContractDocumentReference {
    @Id
    private int id;
    private int contractId;
    private String title;
    private String referenceUrl;
    private String description;
    private LocalDateTime createdAt;

    public ContractDocumentReference() { }

    public ContractDocumentReference(int id, int contractId, String title, String referenceUrl,
                                     String description, LocalDateTime createdAt) {
        this.id = id;
        this.contractId = contractId;
        this.title = title;
        this.referenceUrl = referenceUrl;
        this.description = description;
        this.createdAt = createdAt;
    }

    public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
    }
    public int getContractId()
    {
        return contractId;
    }
    public void setContractId(int contractId)
    {
        this.contractId = contractId;
    }
    public String getTitle()
    {
        return title;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }
    public String getReferenceUrl()
    {
        return referenceUrl;
    }
    public void setReferenceUrl(String referenceUrl)
    {
        this.referenceUrl = referenceUrl;
    }
    public String getDescription()
    {
        return description;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }
    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }
}
