package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Client.
 */
@Entity
@Table(name = "client")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Column(name = "document_number", nullable = false)
    private String documentNumber;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @NotNull
    @Column(name = "birth_date", nullable = false)
    private Instant birthDate;

    @OneToMany(mappedBy = "client")
    @JsonIgnoreProperties(value = { "pointUseDetails", "client" }, allowSetters = true)
    private Set<BagOfPoint> bagOfPoints = new HashSet<>();

    @OneToMany(mappedBy = "client")
    @JsonIgnoreProperties(value = { "pointUseDetails", "client", "pointUsageConcept" }, allowSetters = true)
    private Set<PointUse> pointUses = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "clients" }, allowSetters = true)
    private DocumentType documentType;

    @ManyToOne
    @JsonIgnoreProperties(value = { "clients" }, allowSetters = true)
    private Nacionality nacionality;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Client id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Client name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Client lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDocumentNumber() {
        return this.documentNumber;
    }

    public Client documentNumber(String documentNumber) {
        this.setDocumentNumber(documentNumber);
        return this;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getEmail() {
        return this.email;
    }

    public Client email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Client phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Instant getBirthDate() {
        return this.birthDate;
    }

    public Client birthDate(Instant birthDate) {
        this.setBirthDate(birthDate);
        return this;
    }

    public void setBirthDate(Instant birthDate) {
        this.birthDate = birthDate;
    }

    public Set<BagOfPoint> getBagOfPoints() {
        return this.bagOfPoints;
    }

    public void setBagOfPoints(Set<BagOfPoint> bagOfPoints) {
        if (this.bagOfPoints != null) {
            this.bagOfPoints.forEach(i -> i.setClient(null));
        }
        if (bagOfPoints != null) {
            bagOfPoints.forEach(i -> i.setClient(this));
        }
        this.bagOfPoints = bagOfPoints;
    }

    public Client bagOfPoints(Set<BagOfPoint> bagOfPoints) {
        this.setBagOfPoints(bagOfPoints);
        return this;
    }

    public Client addBagOfPoint(BagOfPoint bagOfPoint) {
        this.bagOfPoints.add(bagOfPoint);
        bagOfPoint.setClient(this);
        return this;
    }

    public Client removeBagOfPoint(BagOfPoint bagOfPoint) {
        this.bagOfPoints.remove(bagOfPoint);
        bagOfPoint.setClient(null);
        return this;
    }

    public Set<PointUse> getPointUses() {
        return this.pointUses;
    }

    public void setPointUses(Set<PointUse> pointUses) {
        if (this.pointUses != null) {
            this.pointUses.forEach(i -> i.setClient(null));
        }
        if (pointUses != null) {
            pointUses.forEach(i -> i.setClient(this));
        }
        this.pointUses = pointUses;
    }

    public Client pointUses(Set<PointUse> pointUses) {
        this.setPointUses(pointUses);
        return this;
    }

    public Client addPointUse(PointUse pointUse) {
        this.pointUses.add(pointUse);
        pointUse.setClient(this);
        return this;
    }

    public Client removePointUse(PointUse pointUse) {
        this.pointUses.remove(pointUse);
        pointUse.setClient(null);
        return this;
    }

    public DocumentType getDocumentType() {
        return this.documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public Client documentType(DocumentType documentType) {
        this.setDocumentType(documentType);
        return this;
    }

    public Nacionality getNacionality() {
        return this.nacionality;
    }

    public void setNacionality(Nacionality nacionality) {
        this.nacionality = nacionality;
    }

    public Client nacionality(Nacionality nacionality) {
        this.setNacionality(nacionality);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Client)) {
            return false;
        }
        return id != null && id.equals(((Client) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Client{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", documentNumber='" + getDocumentNumber() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            "}";
    }
}
