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
 * Criteria class for the {@link com.mycompany.myapp.domain.PointUseDet} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.PointUseDetResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /point-use-dets?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PointUseDetCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter scoreUsed;

    private LongFilter pointUseId;

    private LongFilter bagOfPointId;

    private Boolean distinct;

    public PointUseDetCriteria() {}

    public PointUseDetCriteria(PointUseDetCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.scoreUsed = other.scoreUsed == null ? null : other.scoreUsed.copy();
        this.pointUseId = other.pointUseId == null ? null : other.pointUseId.copy();
        this.bagOfPointId = other.bagOfPointId == null ? null : other.bagOfPointId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PointUseDetCriteria copy() {
        return new PointUseDetCriteria(this);
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

    public LongFilter getBagOfPointId() {
        return bagOfPointId;
    }

    public LongFilter bagOfPointId() {
        if (bagOfPointId == null) {
            bagOfPointId = new LongFilter();
        }
        return bagOfPointId;
    }

    public void setBagOfPointId(LongFilter bagOfPointId) {
        this.bagOfPointId = bagOfPointId;
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
        final PointUseDetCriteria that = (PointUseDetCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(scoreUsed, that.scoreUsed) &&
            Objects.equals(pointUseId, that.pointUseId) &&
            Objects.equals(bagOfPointId, that.bagOfPointId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, scoreUsed, pointUseId, bagOfPointId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PointUseDetCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (scoreUsed != null ? "scoreUsed=" + scoreUsed + ", " : "") +
            (pointUseId != null ? "pointUseId=" + pointUseId + ", " : "") +
            (bagOfPointId != null ? "bagOfPointId=" + bagOfPointId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
