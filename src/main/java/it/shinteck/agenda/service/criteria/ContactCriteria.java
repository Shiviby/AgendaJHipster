package it.shinteck.agenda.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link it.shinteck.agenda.domain.Contact} entity. This class is used
 * in {@link it.shinteck.agenda.web.rest.ContactResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /contacts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class ContactCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private LongFilter phoneContactId;

    private LongFilter emailContactId;

    private LongFilter appointmentId;

    private Boolean distinct;

    public ContactCriteria() {}

    public ContactCriteria(ContactCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.phoneContactId = other.phoneContactId == null ? null : other.phoneContactId.copy();
        this.emailContactId = other.emailContactId == null ? null : other.emailContactId.copy();
        this.appointmentId = other.appointmentId == null ? null : other.appointmentId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ContactCriteria copy() {
        return new ContactCriteria(this);
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

    public StringFilter getTitle() {
        return title;
    }

    public StringFilter title() {
        if (title == null) {
            title = new StringFilter();
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public LongFilter getPhoneContactId() {
        return phoneContactId;
    }

    public LongFilter phoneContactId() {
        if (phoneContactId == null) {
            phoneContactId = new LongFilter();
        }
        return phoneContactId;
    }

    public void setPhoneContactId(LongFilter phoneContactId) {
        this.phoneContactId = phoneContactId;
    }

    public LongFilter getEmailContactId() {
        return emailContactId;
    }

    public LongFilter emailContactId() {
        if (emailContactId == null) {
            emailContactId = new LongFilter();
        }
        return emailContactId;
    }

    public void setEmailContactId(LongFilter emailContactId) {
        this.emailContactId = emailContactId;
    }

    public LongFilter getAppointmentId() {
        return appointmentId;
    }

    public LongFilter appointmentId() {
        if (appointmentId == null) {
            appointmentId = new LongFilter();
        }
        return appointmentId;
    }

    public void setAppointmentId(LongFilter appointmentId) {
        this.appointmentId = appointmentId;
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
        final ContactCriteria that = (ContactCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(phoneContactId, that.phoneContactId) &&
            Objects.equals(emailContactId, that.emailContactId) &&
            Objects.equals(appointmentId, that.appointmentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, phoneContactId, emailContactId, appointmentId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (phoneContactId != null ? "phoneContactId=" + phoneContactId + ", " : "") +
            (emailContactId != null ? "emailContactId=" + emailContactId + ", " : "") +
            (appointmentId != null ? "appointmentId=" + appointmentId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
