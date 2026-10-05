package com.contractwatch.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class RenewalDecision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int contractId;
    private String decision;
    private LocalDate decisionDate;
    private LocalDate newEndDate;
    private String notes;

    public RenewalDecision() { }

    public RenewalDecision(int id, int contractId, String decision, LocalDate decisionDate,
                           LocalDate newEndDate, String notes) {
        this.id = id;
        this.contractId = contractId;
        this.decision = decision;
        this.decisionDate = decisionDate;
        this.newEndDate = newEndDate;
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
    public int getContractId()
    {
        return contractId;
    }
    public void setContractId(int contractId)
    {
        this.contractId = contractId;
    }
    public String getDecision()
    {
        return decision;
    }
    public void setDecision(String decision)
    {
        this.decision = decision;
    }
    public LocalDate getDecisionDate()
    {
        return decisionDate;
    }
    public void setDecisionDate(LocalDate decisionDate)
    {
        this.decisionDate = decisionDate;
    }
    public LocalDate getNewEndDate()
    {
        return newEndDate;
    }
    public void setNewEndDate(LocalDate newEndDate)
    {
        this.newEndDate = newEndDate;
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
