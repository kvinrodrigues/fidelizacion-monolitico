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
 * Criteria class for the {@link com.mycompany.myapp.domain.Client} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.ClientResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /clients?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ClientCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter lastName;

    private StringFilter documentNumber;

    private StringFilter email;

    private StringFilter phoneNumber;

    private InstantFilter birthDate;

    private LongFilter bagOfPointId;

    private LongFilter pointUseId;

    private LongFilter documentTypeId;

    private LongFilter nacionalityId;

    private Boolean distinct;

    public ClientCriteria() {}

    public ClientCriteria(ClientCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.documentNumber = other.documentNumber == null ? null : other.documentNumber.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.phoneNumber = other.phoneNumber == null ? null : other.phoneNumber.copy();
        this.birthDate = other.birthDate == null ? null : other.birthDate.copy();
        this.bagOfPointId = other.bagOfPointId == null ? null : other.bagOfPointId.copy();
        this.pointUseId = other.pointUseId == null ? null : other.pointUseId.copy();
        this.documentTypeId = other.documentTypeId == null ? null : other.documentTypeId.copy();
        this.nacionalityId = other.nacionalityId == null ? null : other.nacionalityId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ClientCriteria copy() {
        return new ClientCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public StringFilter lastName() {
        if (lastName == null) {
            lastName = new StringFilter();
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getDocumentNumber() {
        return documentNumber;
    }

    public StringFilter documentNumber() {
        if (documentNumber == null) {
            documentNumber = new StringFilter();
        }
        return documentNumber;
    }

    public void setDocumentNumber(StringFilter documentNumber) {
        this.documentNumber = documentNumber;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public StringFilter phoneNumber() {
        if (phoneNumber == null) {
            phoneNumber = new StringFilter();
        }
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public InstantFilter getBirthDate() {
        return birthDate;
    }

    public InstantFilter birthDate() {
        if (birthDate == null) {
            birthDate = new InstantFilter();
        }
        return birthDate;
    }

    public void setBirthDate(InstantFilter birthDate) {
        this.birthDate = birthDate;
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

    public LongFilter getDocumentTypeId() {
        return documentTypeId;
    }

    public LongFilter documentTypeId() {
        if (documentTypeId == null) {
            documentTypeId = new LongFilter();
        }
        return documentTypeId;
    }

    public void setDocumentTypeId(LongFilter documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public LongFilter getNacionalityId() {
        return nacionalityId;
    }

    public LongFilter nacionalityId() {
        if (nacionalityId == null) {
            nacionalityId = new LongFilter();
        }
        return nacionalityId;
    }

    public void setNacionalityId(LongFilter nacionalityId) {
        this.nacionalityId = nacionalityId;
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
        final ClientCriteria that = (ClientCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(documentNumber, that.documentNumber) &&
            Objects.equals(email, that.email) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(birthDate, that.birthDate) &&
            Objects.equals(bagOfPointId, that.bagOfPointId) &&
            Objects.equals(pointUseId, that.pointUseId) &&
            Objects.equals(documentTypeId, that.documentTypeId) &&
            Objects.equals(nacionalityId, that.nacionalityId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            lastName,
            documentNumber,
            email,
            phoneNumber,
            birthDate,
            bagOfPointId,
            pointUseId,
            documentTypeId,
            nacionalityId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClientCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (lastName != null ? "lastName=" + lastName + ", " : "") +
            (documentNumber != null ? "documentNumber=" + documentNumber + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (phoneNumber != null ? "phoneNumber=" + phoneNumber + ", " : "") +
            (birthDate != null ? "birthDate=" + birthDate + ", " : "") +
            (bagOfPointId != null ? "bagOfPointId=" + bagOfPointId + ", " : "") +
            (pointUseId != null ? "pointUseId=" + pointUseId + ", " : "") +
            (documentTypeId != null ? "documentTypeId=" + documentTypeId + ", " : "") +
            (nacionalityId != null ? "nacionalityId=" + nacionalityId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
