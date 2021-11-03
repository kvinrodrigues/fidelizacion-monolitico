package com.mycompany.myapp.web.rest.dto;

public class BackOfPointDto {
    public Long clientId;
    public Float amount;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }
}
