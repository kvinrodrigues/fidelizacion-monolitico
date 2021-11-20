package com.mycompany.myapp.web.rest.dto;

import com.mycompany.myapp.domain.Client;

public class BagOfPointDto {

    public Client client;
    public Float operationAmount;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Float getOperationAmount() {
        return operationAmount;
    }

    public void setOperationAmount(Float operationAmount) {
        this.operationAmount = operationAmount;
    }
}
