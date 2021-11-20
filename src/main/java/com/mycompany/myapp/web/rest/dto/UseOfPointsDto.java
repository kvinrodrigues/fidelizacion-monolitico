package com.mycompany.myapp.web.rest.dto;

import com.mycompany.myapp.domain.Client;
import com.mycompany.myapp.domain.PointUsageConcept;

public class UseOfPointsDto {

    private Client client;
    private PointUsageConcept pointUsageConcept;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public PointUsageConcept getPointUsageConcept() {
        return pointUsageConcept;
    }

    public void setPointUsageConcept(PointUsageConcept pointUsageConcept) {
        this.pointUsageConcept = pointUsageConcept;
    }

    @Override
    public String toString() {
        return "UseOfPointsDto{" + "client=" + client + ", pointUsageConcept=" + pointUsageConcept + '}';
    }
}
