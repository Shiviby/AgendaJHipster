package it.shinteck.agenda.service.mapper;

import it.shinteck.agenda.domain.Contact;
import it.shinteck.agenda.domain.EmailContact;
import it.shinteck.agenda.service.dto.ContactDTO;
import it.shinteck.agenda.service.dto.EmailContactDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EmailContact} and its DTO {@link EmailContactDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmailContactMapper extends EntityMapper<EmailContactDTO, EmailContact> {
    @Mapping(target = "contact", source = "contact", qualifiedByName = "contactTitle")
    EmailContactDTO toDto(EmailContact s);

    @Named("contactTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    ContactDTO toDtoContactTitle(Contact contact);
}
