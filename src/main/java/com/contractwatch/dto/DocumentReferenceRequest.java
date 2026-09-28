package com.contractwatch.dto;

public class DocumentReferenceRequest
{
    private String title;
    private String referenceUrl;
    private String description;

    public DocumentReferenceRequest()
    {
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
}
