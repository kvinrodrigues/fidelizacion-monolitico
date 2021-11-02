package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A PointUse.
 */
@Entity
@Table(name = "point_use")
public class PointUse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "score_used", nullable = false)
    private Long scoreUsed;

    @NotNull
    @Column(name = "event_date", nullable = false)
    private Instant eventDate;

    @OneToMany(mappedBy = "pointUse")
    @JsonIgnoreProperties(value = { "pointUse", "bagOfPoint" }, allowSetters = true)
    private Set<PointUseDet> pointUseDetails = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "bagOfPoints", "pointUses", "documentType", "nacionality" }, allowSetters = true)
    private Client client;

    @ManyToOne
    @JsonIgnoreProperties(value = { "pointUses" }, allowSetters = true)
    private PointUsageConcept pointUsageConcept;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PointUse id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getScoreUsed() {
        return this.scoreUsed;
    }

    public PointUse scoreUsed(Long scoreUsed) {
        this.setScoreUsed(scoreUsed);
        return this;
    }

    public void setScoreUsed(Long scoreUsed) {
        this.scoreUsed = scoreUsed;
    }

    public Instant getEventDate() {
        return this.eventDate;
    }

    public PointUse eventDate(Instant eventDate) {
        this.setEventDate(eventDate);
        return this;
    }

    public void setEventDate(Instant eventDate) {
        this.eventDate = eventDate;
    }

    public Set<PointUseDet> getPointUseDetails() {
        return this.pointUseDetails;
    }

    public void setPointUseDetails(Set<PointUseDet> pointUseDets) {
        if (this.pointUseDetails != null) {
            this.pointUseDetails.forEach(i -> i.setPointUse(null));
        }
        if (pointUseDets != null) {
            pointUseDets.forEach(i -> i.setPointUse(this));
        }
        this.pointUseDetails = pointUseDets;
    }

    public PointUse pointUseDetails(Set<PointUseDet> pointUseDets) {
        this.setPointUseDetails(pointUseDets);
        return this;
    }

    public PointUse addPointUseDetail(PointUseDet pointUseDet) {
        this.pointUseDetails.add(pointUseDet);
        pointUseDet.setPointUse(this);
        return this;
    }

    public PointUse removePointUseDetail(PointUseDet pointUseDet) {
        this.pointUseDetails.remove(pointUseDet);
        pointUseDet.setPointUse(null);
        return this;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public PointUse client(Client client) {
        this.setClient(client);
        return this;
    }

    public PointUsageConcept getPointUsageConcept() {
        return this.pointUsageConcept;
    }

    public void setPointUsageConcept(PointUsageConcept pointUsageConcept) {
        this.pointUsageConcept = pointUsageConcept;
    }

    public PointUse pointUsageConcept(PointUsageConcept pointUsageConcept) {
        this.setPointUsageConcept(pointUsageConcept);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PointUse)) {
            return false;
        }
        return id != null && id.equals(((PointUse) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PointUse{" +
            "id=" + getId() +
            ", scoreUsed=" + getScoreUsed() +
            ", eventDate='" + getEventDate() + "'" +
            "}";
    }
}
