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
 * Criteria class for the {@link com.mycompany.myapp.domain.PointAllocationRule} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.PointAllocationRuleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /point-allocation-rules?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PointAllocationRuleCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter lowerLimit;

    private LongFilter upperLimit;

    private FloatFilter equivalenceOfAPoint;

    private Boolean distinct;

    public PointAllocationRuleCriteria() {}

    public PointAllocationRuleCriteria(PointAllocationRuleCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.lowerLimit = other.lowerLimit == null ? null : other.lowerLimit.copy();
        this.upperLimit = other.upperLimit == null ? null : other.upperLimit.copy();
        this.equivalenceOfAPoint = other.equivalenceOfAPoint == null ? null : other.equivalenceOfAPoint.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PointAllocationRuleCriteria copy() {
        return new PointAllocationRuleCriteria(this);
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

    public LongFilter getLowerLimit() {
        return lowerLimit;
    }

    public LongFilter lowerLimit() {
        if (lowerLimit == null) {
            lowerLimit = new LongFilter();
        }
        return lowerLimit;
    }

    public void setLowerLimit(LongFilter lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public LongFilter getUpperLimit() {
        return upperLimit;
    }

    public LongFilter upperLimit() {
        if (upperLimit == null) {
            upperLimit = new LongFilter();
        }
        return upperLimit;
    }

    public void setUpperLimit(LongFilter upperLimit) {
        this.upperLimit = upperLimit;
    }

    public FloatFilter getEquivalenceOfAPoint() {
        return equivalenceOfAPoint;
    }

    public FloatFilter equivalenceOfAPoint() {
        if (equivalenceOfAPoint == null) {
            equivalenceOfAPoint = new FloatFilter();
        }
        return equivalenceOfAPoint;
    }

    public void setEquivalenceOfAPoint(FloatFilter equivalenceOfAPoint) {
        this.equivalenceOfAPoint = equivalenceOfAPoint;
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
        final PointAllocationRuleCriteria that = (PointAllocationRuleCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(lowerLimit, that.lowerLimit) &&
            Objects.equals(upperLimit, that.upperLimit) &&
            Objects.equals(equivalenceOfAPoint, that.equivalenceOfAPoint) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lowerLimit, upperLimit, equivalenceOfAPoint, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PointAllocationRuleCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (lowerLimit != null ? "lowerLimit=" + lowerLimit + ", " : "") +
            (upperLimit != null ? "upperLimit=" + upperLimit + ", " : "") +
            (equivalenceOfAPoint != null ? "equivalenceOfAPoint=" + equivalenceOfAPoint + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
