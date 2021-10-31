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
 * Criteria class for the {@link com.mycompany.myapp.domain.ExpirationPoint} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.ExpirationPointResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /expiration-points?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ExpirationPointCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter validityStartDate;

    private InstantFilter validityEndDate;

    private LongFilter scoreDurationDays;

    private Boolean distinct;

    public ExpirationPointCriteria() {}

    public ExpirationPointCriteria(ExpirationPointCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.validityStartDate = other.validityStartDate == null ? null : other.validityStartDate.copy();
        this.validityEndDate = other.validityEndDate == null ? null : other.validityEndDate.copy();
        this.scoreDurationDays = other.scoreDurationDays == null ? null : other.scoreDurationDays.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ExpirationPointCriteria copy() {
        return new ExpirationPointCriteria(this);
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

    public InstantFilter getValidityStartDate() {
        return validityStartDate;
    }

    public InstantFilter validityStartDate() {
        if (validityStartDate == null) {
            validityStartDate = new InstantFilter();
        }
        return validityStartDate;
    }

    public void setValidityStartDate(InstantFilter validityStartDate) {
        this.validityStartDate = validityStartDate;
    }

    public InstantFilter getValidityEndDate() {
        return validityEndDate;
    }

    public InstantFilter validityEndDate() {
        if (validityEndDate == null) {
            validityEndDate = new InstantFilter();
        }
        return validityEndDate;
    }

    public void setValidityEndDate(InstantFilter validityEndDate) {
        this.validityEndDate = validityEndDate;
    }

    public LongFilter getScoreDurationDays() {
        return scoreDurationDays;
    }

    public LongFilter scoreDurationDays() {
        if (scoreDurationDays == null) {
            scoreDurationDays = new LongFilter();
        }
        return scoreDurationDays;
    }

    public void setScoreDurationDays(LongFilter scoreDurationDays) {
        this.scoreDurationDays = scoreDurationDays;
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
        final ExpirationPointCriteria that = (ExpirationPointCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(validityStartDate, that.validityStartDate) &&
            Objects.equals(validityEndDate, that.validityEndDate) &&
            Objects.equals(scoreDurationDays, that.scoreDurationDays) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, validityStartDate, validityEndDate, scoreDurationDays, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExpirationPointCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (validityStartDate != null ? "validityStartDate=" + validityStartDate + ", " : "") +
            (validityEndDate != null ? "validityEndDate=" + validityEndDate + ", " : "") +
            (scoreDurationDays != null ? "scoreDurationDays=" + scoreDurationDays + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
