package com.contractwatch.dto;

import java.time.LocalDate;

public class RenewalDecisionRequest
{
    private String decision;
    private LocalDate newEndDate;
    private String notes;

    public RenewalDecisionRequest()
    {
    }

    public String getDecision()
    {
        return decision;
    }

    public void setDecision(String decision)
    {
        this.decision = decision;
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
