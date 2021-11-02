package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.BagOfPoint} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.BagOfPointResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /bag-of-points?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BagOfPointCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter asignationDate;

    private InstantFilter expirationDate;

    private LongFilter assignedScore;

    private LongFilter scoreUsed;

    private LongFilter scoreBalance;

    private FloatFilter operationAmount;

    private StringFilter state;

    private LongFilter pointUseDetailId;

    private LongFilter clientId;

    private Boolean distinct;

    public BagOfPointCriteria() {}

    public BagOfPointCriteria(BagOfPointCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.asignationDate = other.asignationDate == null ? null : other.asignationDate.copy();
        this.expirationDate = other.expirationDate == null ? null : other.expirationDate.copy();
        this.assignedScore = other.assignedScore == null ? null : other.assignedScore.copy();
        this.scoreUsed = other.scoreUsed == null ? null : other.scoreUsed.copy();
        this.scoreBalance = other.scoreBalance == null ? null : other.scoreBalance.copy();
        this.operationAmount = other.operationAmount == null ? null : other.operationAmount.copy();
        this.state = other.state == null ? null : other.state.copy();
        this.pointUseDetailId = other.pointUseDetailId == null ? null : other.pointUseDetailId.copy();
        this.clientId = other.clientId == null ? null : other.clientId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public BagOfPointCriteria copy() {
        return new BagOfPointCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getAsignationDate() {
        return asignationDate;
    }

    public InstantFilter asignationDate() {
        if (asignationDate == null) {
            asignationDate = new InstantFilter();
        }
        return asignationDate;
    }

    public void setAsignationDate(InstantFilter asignationDate) {
        this.asignationDate = asignationDate;
    }

    public InstantFilter getExpirationDate() {
        return expirationDate;
    }

    public InstantFilter expirationDate() {
        if (expirationDate == null) {
            expirationDate = new InstantFilter();
        }
        return expirationDate;
    }

    public void setExpirationDate(InstantFilter expirationDate) {
        this.expirationDate = expirationDate;
    }

    public LongFilter getAssignedScore() {
        return assignedScore;
    }

    public LongFilter assignedScore() {
        if (assignedScore == null) {
            assignedScore = new LongFilter();
        }
        return assignedScore;
    }

    public void setAssignedScore(LongFilter assignedScore) {
        this.assignedScore = assignedScore;
    }

    public LongFilter getScoreUsed() {
        return scoreUsed;
    }

    public LongFilter scoreUsed() {
        if (scoreUsed == null) {
            scoreUsed = new LongFilter();
        }
        return scoreUsed;
    }

    public void setScoreUsed(LongFilter scoreUsed) {
        this.scoreUsed = scoreUsed;
    }

    public LongFilter getScoreBalance() {
        return scoreBalance;
    }

    public LongFilter scoreBalance() {
        if (scoreBalance == null) {
            scoreBalance = new LongFilter();
        }
        return scoreBalance;
    }

    public void setScoreBalance(LongFilter scoreBalance) {
        this.scoreBalance = scoreBalance;
    }

    public FloatFilter getOperationAmount() {
        return operationAmount;
    }

    public FloatFilter operationAmount() {
        if (operationAmount == null) {
            operationAmount = new FloatFilter();
        }
        return operationAmount;
    }

    public void setOperationAmount(FloatFilter operationAmount) {
        this.operationAmount = operationAmount;
    }

    public StringFilter getState() {
        return state;
    }

    public StringFilter state() {
        if (state == null) {
            state = new StringFilter();
        }
        return state;
    }

    public void setState(StringFilter state) {
        this.state = state;
    }

    public LongFilter getPointUseDetailId() {
        return pointUseDetailId;
    }

    public LongFilter pointUseDetailId() {
        if (pointUseDetailId == null) {
            pointUseDetailId = new LongFilter();
        }
        return pointUseDetailId;
    }

    public void setPointUseDetailId(LongFilter pointUseDetailId) {
        this.pointUseDetailId = pointUseDetailId;
    }

    public LongFilter getClientId() {
        return clientId;
    }

    public LongFilter clientId() {
        if (clientId == null) {
            clientId = new LongFilter();
        }
        return clientId;
    }

    public void setClientId(LongFilter clientId) {
        this.clientId = clientId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BagOfPointCriteria that = (BagOfPointCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(asignationDate, that.asignationDate) &&
            Objects.equals(expirationDate, that.expirationDate) &&
            Objects.equals(assignedScore, that.assignedScore) &&
            Objects.equals(scoreUsed, that.scoreUsed) &&
            Objects.equals(scoreBalance, that.scoreBalance) &&
            Objects.equals(operationAmount, that.operationAmount) &&
            Objects.equals(state, that.state) &&
            Objects.equals(pointUseDetailId, that.pointUseDetailId) &&
            Objects.equals(clientId, that.clientId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            asignationDate,
            expirationDate,
            assignedScore,
            scoreUsed,
            scoreBalance,
            operationAmount,
            state,
            pointUseDetailId,
            clientId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BagOfPointCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (asignationDate != null ? "asignationDate=" + asignationDate + ", " : "") +
            (expirationDate != null ? "expirationDate=" + expirationDate + ", " : "") +
            (assignedScore != null ? "assignedScore=" + assignedScore + ", " : "") +
            (scoreUsed != null ? "scoreUsed=" + scoreUsed + ", " : "") +
            (scoreBalance != null ? "scoreBalance=" + scoreBalance + ", " : "") +
            (operationAmount != null ? "operationAmount=" + operationAmount + ", " : "") +
            (state != null ? "state=" + state + ", " : "") +
            (pointUseDetailId != null ? "pointUseDetailId=" + pointUseDetailId + ", " : "") +
            (clientId != null ? "clientId=" + clientId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
