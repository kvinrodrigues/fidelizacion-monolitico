package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A ExpirationPoint.
 */
@Entity
@Table(name = "expiration_point")
public class ExpirationPoint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "validity_start_date", nullable = false)
    private Instant validityStartDate;

    @NotNull
    @Column(name = "validity_end_date", nullable = false)
    private Instant validityEndDate;

    @NotNull
    @Column(name = "score_duration_days", nullable = false)
    private Long scoreDurationDays;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ExpirationPoint id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getValidityStartDate() {
        return this.validityStartDate;
    }

    public ExpirationPoint validityStartDate(Instant validityStartDate) {
        this.setValidityStartDate(validityStartDate);
        return this;
    }

    public void setValidityStartDate(Instant validityStartDate) {
        this.validityStartDate = validityStartDate;
    }

    public Instant getValidityEndDate() {
        return this.validityEndDate;
    }

    public ExpirationPoint validityEndDate(Instant validityEndDate) {
        this.setValidityEndDate(validityEndDate);
        return this;
    }

    public void setValidityEndDate(Instant validityEndDate) {
        this.validityEndDate = validityEndDate;
    }

    public Long getScoreDurationDays() {
        return this.scoreDurationDays;
    }

    public ExpirationPoint scoreDurationDays(Long scoreDurationDays) {
        this.setScoreDurationDays(scoreDurationDays);
        return this;
    }

    public void setScoreDurationDays(Long scoreDurationDays) {
        this.scoreDurationDays = scoreDurationDays;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExpirationPoint)) {
            return false;
        }
        return id != null && id.equals(((ExpirationPoint) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExpirationPoint{" +
            "id=" + getId() +
            ", validityStartDate='" + getValidityStartDate() + "'" +
            ", validityEndDate='" + getValidityEndDate() + "'" +
            ", scoreDurationDays=" + getScoreDurationDays() +
            "}";
    }
}
