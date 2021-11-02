package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A PointUsageConcept.
 */
@Entity
@Table(name = "point_usage_concept")
public class PointUsageConcept implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "required_points")
    private Long requiredPoints;

    @OneToMany(mappedBy = "pointUsageConcept")
    @JsonIgnoreProperties(value = { "pointUseDetails", "client", "pointUsageConcept" }, allowSetters = true)
    private Set<PointUse> pointUses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PointUsageConcept id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public PointUsageConcept description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getRequiredPoints() {
        return this.requiredPoints;
    }

    public PointUsageConcept requiredPoints(Long requiredPoints) {
        this.setRequiredPoints(requiredPoints);
        return this;
    }

    public void setRequiredPoints(Long requiredPoints) {
        this.requiredPoints = requiredPoints;
    }

    public Set<PointUse> getPointUses() {
        return this.pointUses;
    }

    public void setPointUses(Set<PointUse> pointUses) {
        if (this.pointUses != null) {
            this.pointUses.forEach(i -> i.setPointUsageConcept(null));
        }
        if (pointUses != null) {
            pointUses.forEach(i -> i.setPointUsageConcept(this));
        }
        this.pointUses = pointUses;
    }

    public PointUsageConcept pointUses(Set<PointUse> pointUses) {
        this.setPointUses(pointUses);
        return this;
    }

    public PointUsageConcept addPointUse(PointUse pointUse) {
        this.pointUses.add(pointUse);
        pointUse.setPointUsageConcept(this);
        return this;
    }

    public PointUsageConcept removePointUse(PointUse pointUse) {
        this.pointUses.remove(pointUse);
        pointUse.setPointUsageConcept(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PointUsageConcept)) {
            return false;
        }
        return id != null && id.equals(((PointUsageConcept) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PointUsageConcept{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", requiredPoints=" + getRequiredPoints() +
            "}";
    }
}
