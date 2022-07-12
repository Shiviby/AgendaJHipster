package it.shinteck.agenda.service.mapper;

import it.shinteck.agenda.domain.Contact;
import it.shinteck.agenda.service.dto.ContactDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Contact} and its DTO {@link ContactDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContactMapper extends EntityMapper<ContactDTO, Contact> {}
