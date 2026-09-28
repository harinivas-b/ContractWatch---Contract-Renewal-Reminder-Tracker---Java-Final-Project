package com.contractwatch.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

@Entity
@Table(name = "renewal_decisions")
public class RenewalDecision
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "contract_id", nullable = false)
    @JsonIgnore
    private Contract contract;

    @NotBlank
    private String decision;

    private LocalDate decisionDate = LocalDate.now();

    private LocalDate newEndDate;

    private String notes;

    public RenewalDecision()
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

    public Contract getContract()
    {
        return contract;
    }

    public void setContract(Contract contract)
    {
        this.contract = contract;
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
