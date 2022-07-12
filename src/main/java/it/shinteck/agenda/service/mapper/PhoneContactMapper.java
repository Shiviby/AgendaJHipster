package it.shinteck.agenda.service.mapper;

import it.shinteck.agenda.domain.Contact;
import it.shinteck.agenda.domain.PhoneContact;
import it.shinteck.agenda.service.dto.ContactDTO;
import it.shinteck.agenda.service.dto.PhoneContactDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PhoneContact} and its DTO {@link PhoneContactDTO}.
 */
@Mapper(componentModel = "spring")
public interface PhoneContactMapper extends EntityMapper<PhoneContactDTO, PhoneContact> {
    @Mapping(target = "contact", source = "contact", qualifiedByName = "contactTitle")
    PhoneContactDTO toDto(PhoneContact s);

    @Named("contactTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    ContactDTO toDtoContactTitle(Contact contact);
}
