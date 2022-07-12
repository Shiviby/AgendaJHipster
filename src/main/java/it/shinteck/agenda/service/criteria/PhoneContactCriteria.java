package it.shinteck.agenda.service.criteria;

import it.shinteck.agenda.domain.enumeration.PhoneContactType;
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
 * Criteria class for the {@link it.shinteck.agenda.domain.PhoneContact} entity. This class is used
 * in {@link it.shinteck.agenda.web.rest.PhoneContactResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /phone-contacts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class PhoneContactCriteria implements Serializable, Criteria {

    /**
     * Class for filtering PhoneContactType
     */
    public static class PhoneContactTypeFilter extends Filter<PhoneContactType> {

        public PhoneContactTypeFilter() {}

        public PhoneContactTypeFilter(PhoneContactTypeFilter filter) {
            super(filter);
        }

        @Override
        public PhoneContactTypeFilter copy() {
            return new PhoneContactTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private PhoneContactTypeFilter type;

    private StringFilter phone;

    private LongFilter contactId;

    private Boolean distinct;

    public PhoneContactCriteria() {}

    public PhoneContactCriteria(PhoneContactCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.contactId = other.contactId == null ? null : other.contactId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PhoneContactCriteria copy() {
        return new PhoneContactCriteria(this);
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

    public PhoneContactTypeFilter getType() {
        return type;
    }

    public PhoneContactTypeFilter type() {
        if (type == null) {
            type = new PhoneContactTypeFilter();
        }
        return type;
    }

    public void setType(PhoneContactTypeFilter type) {
        this.type = type;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public StringFilter phone() {
        if (phone == null) {
            phone = new StringFilter();
        }
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
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
        final PhoneContactCriteria that = (PhoneContactCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(type, that.type) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(contactId, that.contactId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, phone, contactId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PhoneContactCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (phone != null ? "phone=" + phone + ", " : "") +
            (contactId != null ? "contactId=" + contactId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
