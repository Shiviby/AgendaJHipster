package it.shinteck.agenda.service.dto;

import it.shinteck.agenda.domain.enumeration.PhoneContactType;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link it.shinteck.agenda.domain.PhoneContact} entity.
 */
public class PhoneContactDTO implements Serializable {

    private Long id;

    @NotNull
    private PhoneContactType type;

    @NotNull
    private String phone;

    private ContactDTO contact;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PhoneContactType getType() {
        return type;
    }

    public void setType(PhoneContactType type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
        if (!(o instanceof PhoneContactDTO)) {
            return false;
        }

        PhoneContactDTO phoneContactDTO = (PhoneContactDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, phoneContactDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PhoneContactDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", phone='" + getPhone() + "'" +
            ", contact=" + getContact() +
            "}";
    }
}
