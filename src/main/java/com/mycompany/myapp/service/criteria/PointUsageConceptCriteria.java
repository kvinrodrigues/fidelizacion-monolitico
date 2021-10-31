package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.PointUsageConcept} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.PointUsageConceptResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /point-usage-concepts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PointUsageConceptCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter description;

    private LongFilter requiredPoints;

    private LongFilter pointUseId;

    private Boolean distinct;

    public PointUsageConceptCriteria() {}

    public PointUsageConceptCriteria(PointUsageConceptCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.requiredPoints = other.requiredPoints == null ? null : other.requiredPoints.copy();
        this.pointUseId = other.pointUseId == null ? null : other.pointUseId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PointUsageConceptCriteria copy() {
        return new PointUsageConceptCriteria(this);
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

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LongFilter getRequiredPoints() {
        return requiredPoints;
    }

    public LongFilter requiredPoints() {
        if (requiredPoints == null) {
            requiredPoints = new LongFilter();
        }
        return requiredPoints;
    }

    public void setRequiredPoints(LongFilter requiredPoints) {
        this.requiredPoints = requiredPoints;
    }

    public LongFilter getPointUseId() {
        return pointUseId;
    }

    public LongFilter pointUseId() {
        if (pointUseId == null) {
            pointUseId = new LongFilter();
        }
        return pointUseId;
    }

    public void setPointUseId(LongFilter pointUseId) {
        this.pointUseId = pointUseId;
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
        final PointUsageConceptCriteria that = (PointUsageConceptCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(description, that.description) &&
            Objects.equals(requiredPoints, that.requiredPoints) &&
            Objects.equals(pointUseId, that.pointUseId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, requiredPoints, pointUseId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PointUsageConceptCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (requiredPoints != null ? "requiredPoints=" + requiredPoints + ", " : "") +
            (pointUseId != null ? "pointUseId=" + pointUseId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
