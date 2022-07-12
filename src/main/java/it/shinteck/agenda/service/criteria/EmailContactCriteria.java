package it.shinteck.agenda.service.criteria;

import it.shinteck.agenda.domain.enumeration.EmailContactType;
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
 * Criteria class for the {@link it.shinteck.agenda.domain.EmailContact} entity. This class is used
 * in {@link it.shinteck.agenda.web.rest.EmailContactResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /email-contacts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class EmailContactCriteria implements Serializable, Criteria {

    /**
     * Class for filtering EmailContactType
     */
    public static class EmailContactTypeFilter extends Filter<EmailContactType> {

        public EmailContactTypeFilter() {}

        public EmailContactTypeFilter(EmailContactTypeFilter filter) {
            super(filter);
        }

        @Override
        public EmailContactTypeFilter copy() {
            return new EmailContactTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private EmailContactTypeFilter type;

    private StringFilter email;

    private LongFilter contactId;

    private Boolean distinct;

    public EmailContactCriteria() {}

    public EmailContactCriteria(EmailContactCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.contactId = other.contactId == null ? null : other.contactId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EmailContactCriteria copy() {
        return new EmailContactCriteria(this);
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

    public EmailContactTypeFilter getType() {
        return type;
    }

    public EmailContactTypeFilter type() {
        if (type == null) {
            type = new EmailContactTypeFilter();
        }
        return type;
    }

    public void setType(EmailContactTypeFilter type) {
        this.type = type;
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

    public LongFilter getContactId() {
        return contactId;
    }

    public LongFilter contactId() {
        if (contactId == null) {
            contactId = new LongFilter();
        }
        return contactId;
    }

    public void setContactId(LongFilter contactId) {
        this.contactId = contactId;
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
        final EmailContactCriteria that = (EmailContactCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(type, that.type) &&
            Objects.equals(email, that.email) &&
            Objects.equals(contactId, that.contactId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, email, contactId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmailContactCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (contactId != null ? "contactId=" + contactId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
