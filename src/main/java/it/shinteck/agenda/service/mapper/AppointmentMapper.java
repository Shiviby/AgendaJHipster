package it.shinteck.agenda.service.mapper;

import it.shinteck.agenda.domain.Appointment;
import it.shinteck.agenda.domain.Contact;
import it.shinteck.agenda.service.dto.AppointmentDTO;
import it.shinteck.agenda.service.dto.ContactDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Appointment} and its DTO {@link AppointmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface AppointmentMapper extends EntityMapper<AppointmentDTO, Appointment> {
    @Mapping(target = "contacts", source = "contacts", qualifiedByName = "contactTitleSet")
    AppointmentDTO toDto(Appointment s);

    @Mapping(target = "removeContact", ignore = true)
    Appointment toEntity(AppointmentDTO appointmentDTO);

    @Named("contactTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    ContactDTO toDtoContactTitle(Contact contact);

    @Named("contactTitleSet")
    default Set<ContactDTO> toDtoContactTitleSet(Set<Contact> contact) {
        return contact.stream().map(this::toDtoContactTitle).collect(Collectors.toSet());
    }
}
