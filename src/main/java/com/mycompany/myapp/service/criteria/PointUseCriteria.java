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
 * Criteria class for the {@link com.mycompany.myapp.domain.PointUse} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.PointUseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /point-uses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PointUseCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter scoreUsed;

    private InstantFilter eventDate;

    private LongFilter pointUseDetailId;

    private LongFilter clientId;

    private LongFilter pointUsageConceptId;

    private Boolean distinct;

    public PointUseCriteria() {}

    public PointUseCriteria(PointUseCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.scoreUsed = other.scoreUsed == null ? null : other.scoreUsed.copy();
        this.eventDate = other.eventDate == null ? null : other.eventDate.copy();
        this.pointUseDetailId = other.pointUseDetailId == null ? null : other.pointUseDetailId.copy();
        this.clientId = other.clientId == null ? null : other.clientId.copy();
        this.pointUsageConceptId = other.pointUsageConceptId == null ? null : other.pointUsageConceptId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PointUseCriteria copy() {
        return new PointUseCriteria(this);
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

    public InstantFilter getEventDate() {
        return eventDate;
    }

    public InstantFilter eventDate() {
        if (eventDate == null) {
            eventDate = new InstantFilter();
        }
        return eventDate;
    }

    public void setEventDate(InstantFilter eventDate) {
        this.eventDate = eventDate;
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

    public LongFilter getPointUsageConceptId() {
        return pointUsageConceptId;
    }

    public LongFilter pointUsageConceptId() {
        if (pointUsageConceptId == null) {
            pointUsageConceptId = new LongFilter();
        }
        return pointUsageConceptId;
    }

    public void setPointUsageConceptId(LongFilter pointUsageConceptId) {
        this.pointUsageConceptId = pointUsageConceptId;
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
        final PointUseCriteria that = (PointUseCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(scoreUsed, that.scoreUsed) &&
            Objects.equals(eventDate, that.eventDate) &&
            Objects.equals(pointUseDetailId, that.pointUseDetailId) &&
            Objects.equals(clientId, that.clientId) &&
            Objects.equals(pointUsageConceptId, that.pointUsageConceptId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, scoreUsed, eventDate, pointUseDetailId, clientId, pointUsageConceptId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PointUseCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (scoreUsed != null ? "scoreUsed=" + scoreUsed + ", " : "") +
            (eventDate != null ? "eventDate=" + eventDate + ", " : "") +
            (pointUseDetailId != null ? "pointUseDetailId=" + pointUseDetailId + ", " : "") +
            (clientId != null ? "clientId=" + clientId + ", " : "") +
            (pointUsageConceptId != null ? "pointUsageConceptId=" + pointUsageConceptId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
