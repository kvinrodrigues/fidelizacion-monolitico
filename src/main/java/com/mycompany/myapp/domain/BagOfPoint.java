package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A BagOfPoint.
 */
@Entity
@Table(name = "bag_of_point")
public class BagOfPoint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "asignation_date", nullable = false)
    private Instant asignationDate;

    @NotNull
    @Column(name = "expiration_date", nullable = false)
    private Instant expirationDate;

    @NotNull
    @Column(name = "assigned_score", nullable = false)
    private Long assignedScore;

    @NotNull
    @Column(name = "score_used", nullable = false)
    private Long scoreUsed;

    @NotNull
    @Column(name = "score_balance", nullable = false)
    private Long scoreBalance;

    @NotNull
    @Column(name = "operation_amount", nullable = false)
    private Float operationAmount;

    @NotNull
    @Column(name = "state", nullable = false)
    private String state;

    @OneToMany(mappedBy = "bagOfPoint")
    @JsonIgnoreProperties(value = { "pointUse", "bagOfPoint" }, allowSetters = true)
    private Set<PointUseDet> pointUseDetails = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "bagOfPoints", "pointUses", "documentType", "nacionality" }, allowSetters = true)
    private Client client;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BagOfPoint id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getAsignationDate() {
        return this.asignationDate;
    }

    public BagOfPoint asignationDate(Instant asignationDate) {
        this.setAsignationDate(asignationDate);
        return this;
    }

    public void setAsignationDate(Instant asignationDate) {
        this.asignationDate = asignationDate;
    }

    public Instant getExpirationDate() {
        return this.expirationDate;
    }

    public BagOfPoint expirationDate(Instant expirationDate) {
        this.setExpirationDate(expirationDate);
        return this;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Long getAssignedScore() {
        return this.assignedScore;
    }

    public BagOfPoint assignedScore(Long assignedScore) {
        this.setAssignedScore(assignedScore);
        return this;
    }

    public void setAssignedScore(Long assignedScore) {
        this.assignedScore = assignedScore;
    }

    public Long getScoreUsed() {
        return this.scoreUsed;
    }

    public BagOfPoint scoreUsed(Long scoreUsed) {
        this.setScoreUsed(scoreUsed);
        return this;
    }

    public void setScoreUsed(Long scoreUsed) {
        this.scoreUsed = scoreUsed;
    }

    public Long getScoreBalance() {
        return this.scoreBalance;
    }

    public BagOfPoint scoreBalance(Long scoreBalance) {
        this.setScoreBalance(scoreBalance);
        return this;
    }

    public void setScoreBalance(Long scoreBalance) {
        this.scoreBalance = scoreBalance;
    }

    public Float getOperationAmount() {
        return this.operationAmount;
    }

    public BagOfPoint operationAmount(Float operationAmount) {
        this.setOperationAmount(operationAmount);
        return this;
    }

    public void setOperationAmount(Float operationAmount) {
        this.operationAmount = operationAmount;
    }

    public String getState() {
        return this.state;
    }

    public BagOfPoint state(String state) {
        this.setState(state);
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Set<PointUseDet> getPointUseDetails() {
        return this.pointUseDetails;
    }

    public void setPointUseDetails(Set<PointUseDet> pointUseDets) {
        if (this.pointUseDetails != null) {
            this.pointUseDetails.forEach(i -> i.setBagOfPoint(null));
        }
        if (pointUseDets != null) {
            pointUseDets.forEach(i -> i.setBagOfPoint(this));
        }
        this.pointUseDetails = pointUseDets;
    }

    public BagOfPoint pointUseDetails(Set<PointUseDet> pointUseDets) {
        this.setPointUseDetails(pointUseDets);
        return this;
    }

    public BagOfPoint addPointUseDetail(PointUseDet pointUseDet) {
        this.pointUseDetails.add(pointUseDet);
        pointUseDet.setBagOfPoint(this);
        return this;
    }

    public BagOfPoint removePointUseDetail(PointUseDet pointUseDet) {
        this.pointUseDetails.remove(pointUseDet);
        pointUseDet.setBagOfPoint(null);
        return this;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public BagOfPoint client(Client client) {
        this.setClient(client);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BagOfPoint)) {
            return false;
        }
        return id != null && id.equals(((BagOfPoint) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BagOfPoint{" +
            "id=" + getId() +
            ", asignationDate='" + getAsignationDate() + "'" +
            ", expirationDate='" + getExpirationDate() + "'" +
            ", assignedScore=" + getAssignedScore() +
            ", scoreUsed=" + getScoreUsed() +
            ", scoreBalance=" + getScoreBalance() +
            ", operationAmount=" + getOperationAmount() +
            ", state='" + getState() + "'" +
            "}";
    }
}
