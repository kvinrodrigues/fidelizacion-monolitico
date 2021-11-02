package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A PointUseDet.
 */
@Entity
@Table(name = "point_use_det")
public class PointUseDet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "score_used", nullable = false)
    private Long scoreUsed;

    @ManyToOne
    @JsonIgnoreProperties(value = { "pointUseDetails", "client", "pointUsageConcept" }, allowSetters = true)
    private PointUse pointUse;

    @ManyToOne
    @JsonIgnoreProperties(value = { "pointUseDetails", "client" }, allowSetters = true)
    private BagOfPoint bagOfPoint;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PointUseDet id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getScoreUsed() {
        return this.scoreUsed;
    }

    public PointUseDet scoreUsed(Long scoreUsed) {
        this.setScoreUsed(scoreUsed);
        return this;
    }

    public void setScoreUsed(Long scoreUsed) {
        this.scoreUsed = scoreUsed;
    }

    public PointUse getPointUse() {
        return this.pointUse;
    }

    public void setPointUse(PointUse pointUse) {
        this.pointUse = pointUse;
    }

    public PointUseDet pointUse(PointUse pointUse) {
        this.setPointUse(pointUse);
        return this;
    }

    public BagOfPoint getBagOfPoint() {
        return this.bagOfPoint;
    }

    public void setBagOfPoint(BagOfPoint bagOfPoint) {
        this.bagOfPoint = bagOfPoint;
    }

    public PointUseDet bagOfPoint(BagOfPoint bagOfPoint) {
        this.setBagOfPoint(bagOfPoint);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PointUseDet)) {
            return false;
        }
        return id != null && id.equals(((PointUseDet) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PointUseDet{" +
            "id=" + getId() +
            ", scoreUsed=" + getScoreUsed() +
            "}";
    }
}
