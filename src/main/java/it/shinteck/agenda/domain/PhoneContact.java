package it.shinteck.agenda.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.shinteck.agenda.domain.enumeration.PhoneContactType;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A PhoneContact.
 */
@Entity
@Table(name = "phone_contact")
public class PhoneContact implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PhoneContactType type;

    @NotNull
    @Column(name = "phone", nullable = false)
    private String phone;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "phoneContacts", "emailContacts", "appointments" }, allowSetters = true)
    private Contact contact;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PhoneContact id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PhoneContactType getType() {
        return this.type;
    }

    public PhoneContact type(PhoneContactType type) {
        this.setType(type);
        return this;
    }

    public void setType(PhoneContactType type) {
        this.type = type;
    }

    public String getPhone() {
        return this.phone;
    }

    public PhoneContact phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Contact getContact() {
        return this.contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public PhoneContact contact(Contact contact) {
        this.setContact(contact);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PhoneContact)) {
            return false;
        }
        return id != null && id.equals(((PhoneContact) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PhoneContact{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", phone='" + getPhone() + "'" +
            "}";
    }
}
