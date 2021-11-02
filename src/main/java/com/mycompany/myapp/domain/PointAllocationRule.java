package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A PointAllocationRule.
 */
@Entity
@Table(name = "point_allocation_rule")
public class PointAllocationRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "lower_limit")
    private Long lowerLimit;

    @Column(name = "upper_limit")
    private Long upperLimit;

    @NotNull
    @Column(name = "equivalence_of_a_point", nullable = false)
    private Float equivalenceOfAPoint;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public boolean isInTheRange(float amount) {
        long convertedAmount = (long) amount;
        return convertedAmount >= lowerLimit && convertedAmount <= upperLimit;
    }

    public Long getId() {
        return this.id;
    }

    public PointAllocationRule id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLowerLimit() {
        return this.lowerLimit;
    }

    public PointAllocationRule lowerLimit(Long lowerLimit) {
        this.setLowerLimit(lowerLimit);
        return this;
    }

    public void setLowerLimit(Long lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public Long getUpperLimit() {
        return this.upperLimit;
    }

    public PointAllocationRule upperLimit(Long upperLimit) {
        this.setUpperLimit(upperLimit);
        return this;
    }

    public void setUpperLimit(Long upperLimit) {
        this.upperLimit = upperLimit;
    }

    public Float getEquivalenceOfAPoint() {
        return this.equivalenceOfAPoint;
    }

    public PointAllocationRule equivalenceOfAPoint(Float equivalenceOfAPoint) {
        this.setEquivalenceOfAPoint(equivalenceOfAPoint);
        return this;
    }

    public void setEquivalenceOfAPoint(Float equivalenceOfAPoint) {
        this.equivalenceOfAPoint = equivalenceOfAPoint;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PointAllocationRule)) {
            return false;
        }
        return id != null && id.equals(((PointAllocationRule) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PointAllocationRule{" +
            "id=" + getId() +
            ", lowerLimit=" + getLowerLimit() +
            ", upperLimit=" + getUpperLimit() +
            ", equivalenceOfAPoint=" + getEquivalenceOfAPoint() +
            "}";
    }
}
