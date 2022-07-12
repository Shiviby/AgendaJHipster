package it.shinteck.agenda.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Contact.
 */
@Entity
@Table(name = "contact")
public class Contact implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "avatar")
    private byte[] avatar;

    @Column(name = "avatar_content_type")
    private String avatarContentType;

    @OneToMany(mappedBy = "contact")
    @JsonIgnoreProperties(value = { "contact" }, allowSetters = true)
    private Set<PhoneContact> phoneContacts = new HashSet<>();

    @OneToMany(mappedBy = "contact")
    @JsonIgnoreProperties(value = { "contact" }, allowSetters = true)
    private Set<EmailContact> emailContacts = new HashSet<>();

    @ManyToMany(mappedBy = "contacts")
    @JsonIgnoreProperties(value = { "contacts" }, allowSetters = true)
    private Set<Appointment> appointments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Contact id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Contact title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte[] getAvatar() {
        return this.avatar;
    }

    public Contact avatar(byte[] avatar) {
        this.setAvatar(avatar);
        return this;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public String getAvatarContentType() {
        return this.avatarContentType;
    }

    public Contact avatarContentType(String avatarContentType) {
        this.avatarContentType = avatarContentType;
        return this;
    }

    public void setAvatarContentType(String avatarContentType) {
        this.avatarContentType = avatarContentType;
    }

    public Set<PhoneContact> getPhoneContacts() {
        return this.phoneContacts;
    }

    public void setPhoneContacts(Set<PhoneContact> phoneContacts) {
        if (this.phoneContacts != null) {
            this.phoneContacts.forEach(i -> i.setContact(null));
        }
        if (phoneContacts != null) {
            phoneContacts.forEach(i -> i.setContact(this));
        }
        this.phoneContacts = phoneContacts;
    }

    public Contact phoneContacts(Set<PhoneContact> phoneContacts) {
        this.setPhoneContacts(phoneContacts);
        return this;
    }

    public Contact addPhoneContact(PhoneContact phoneContact) {
        this.phoneContacts.add(phoneContact);
        phoneContact.setContact(this);
        return this;
    }

    public Contact removePhoneContact(PhoneContact phoneContact) {
        this.phoneContacts.remove(phoneContact);
        phoneContact.setContact(null);
        return this;
    }

    public Set<EmailContact> getEmailContacts() {
        return this.emailContacts;
    }

    public void setEmailContacts(Set<EmailContact> emailContacts) {
        if (this.emailContacts != null) {
            this.emailContacts.forEach(i -> i.setContact(null));
        }
        if (emailContacts != null) {
            emailContacts.forEach(i -> i.setContact(this));
        }
        this.emailContacts = emailContacts;
    }

    public Contact emailContacts(Set<EmailContact> emailContacts) {
        this.setEmailContacts(emailContacts);
        return this;
    }

    public Contact addEmailContact(EmailContact emailContact) {
        this.emailContacts.add(emailContact);
        emailContact.setContact(this);
        return this;
    }

    public Contact removeEmailContact(EmailContact emailContact) {
        this.emailContacts.remove(emailContact);
        emailContact.setContact(null);
        return this;
    }

    public Set<Appointment> getAppointments() {
        return this.appointments;
    }

    public void setAppointments(Set<Appointment> appointments) {
        if (this.appointments != null) {
            this.appointments.forEach(i -> i.removeContact(this));
        }
        if (appointments != null) {
            appointments.forEach(i -> i.addContact(this));
        }
        this.appointments = appointments;
    }

    public Contact appointments(Set<Appointment> appointments) {
        this.setAppointments(appointments);
        return this;
    }

    public Contact addAppointment(Appointment appointment) {
        this.appointments.add(appointment);
        appointment.getContacts().add(this);
        return this;
    }

    public Contact removeAppointment(Appointment appointment) {
        this.appointments.remove(appointment);
        appointment.getContacts().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contact)) {
            return false;
        }
        return id != null && id.equals(((Contact) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Contact{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", avatar='" + getAvatar() + "'" +
            ", avatarContentType='" + getAvatarContentType() + "'" +
            "}";
    }
}
