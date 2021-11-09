package com.mycompany.myapp.web.rest.dto;

public class UseOfPointsDto {

    private Long clientId;
    private Long usageConceptId;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getUsageConceptId() {
        return usageConceptId;
    }

    public void setUsageConceptId(Long usageConceptId) {
        this.usageConceptId = usageConceptId;
    }
}
