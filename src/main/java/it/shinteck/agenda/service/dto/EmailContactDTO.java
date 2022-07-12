package it.shinteck.agenda.service.dto;

import it.shinteck.agenda.domain.enumeration.EmailContactType;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link it.shinteck.agenda.domain.EmailContact} entity.
 */
public class EmailContactDTO implements Serializable {

    private Long id;

    @NotNull
    private EmailContactType type;

    @NotNull
    private String email;

    private ContactDTO contact;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmailContactType getType() {
        return type;
    }

    public void setType(EmailContactType type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ContactDTO getContact() {
        return contact;
    }

    public void setContact(ContactDTO contact) {
        this.contact = contact;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmailContactDTO)) {
            return false;
        }

        EmailContactDTO emailContactDTO = (EmailContactDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, emailContactDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmailContactDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", email='" + getEmail() + "'" +
            ", contact=" + getContact() +
            "}";
    }
}
