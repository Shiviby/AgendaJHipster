package it.shinteck.agenda.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.shinteck.agenda.domain.enumeration.EmailContactType;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A EmailContact.
 */
@Entity
@Table(name = "email_contact")
public class EmailContact implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private EmailContactType type;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "phoneContacts", "emailContacts", "appointments" }, allowSetters = true)
    private Contact contact;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EmailContact id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmailContactType getType() {
        return this.type;
    }

    public EmailContact type(EmailContactType type) {
        this.setType(type);
        return this;
    }

    public void setType(EmailContactType type) {
        this.type = type;
    }

    public String getEmail() {
        return this.email;
    }

    public EmailContact email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Contact getContact() {
        return this.contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public EmailContact contact(Contact contact) {
        this.setContact(contact);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmailContact)) {
            return false;
        }
        return id != null && id.equals(((EmailContact) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmailContact{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
