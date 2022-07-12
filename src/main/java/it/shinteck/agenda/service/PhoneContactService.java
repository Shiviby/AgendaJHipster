package it.shinteck.agenda.service;

import it.shinteck.agenda.domain.PhoneContact;
import it.shinteck.agenda.repository.PhoneContactRepository;
import it.shinteck.agenda.service.dto.PhoneContactDTO;
import it.shinteck.agenda.service.mapper.PhoneContactMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PhoneContact}.
 */
@Service
@Transactional
public class PhoneContactService {

    private final Logger log = LoggerFactory.getLogger(PhoneContactService.class);

    private final PhoneContactRepository phoneContactRepository;

    private final PhoneContactMapper phoneContactMapper;

    public PhoneContactService(PhoneContactRepository phoneContactRepository, PhoneContactMapper phoneContactMapper) {
        this.phoneContactRepository = phoneContactRepository;
        this.phoneContactMapper = phoneContactMapper;
    }

    /**
     * Save a phoneContact.
     *
     * @param phoneContactDTO the entity to save.
     * @return the persisted entity.
     */
    public PhoneContactDTO save(PhoneContactDTO phoneContactDTO) {
        log.debug("Request to save PhoneContact : {}", phoneContactDTO);
        PhoneContact phoneContact = phoneContactMapper.toEntity(phoneContactDTO);
        phoneContact = phoneContactRepository.save(phoneContact);
        return phoneContactMapper.toDto(phoneContact);
    }

    /**
     * Update a phoneContact.
     *
     * @param phoneContactDTO the entity to save.
     * @return the persisted entity.
     */
    public PhoneContactDTO update(PhoneContactDTO phoneContactDTO) {
        log.debug("Request to save PhoneContact : {}", phoneContactDTO);
        PhoneContact phoneContact = phoneContactMapper.toEntity(phoneContactDTO);
        phoneContact = phoneContactRepository.save(phoneContact);
        return phoneContactMapper.toDto(phoneContact);
    }

    /**
     * Partially update a phoneContact.
     *
     * @param phoneContactDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PhoneContactDTO> partialUpdate(PhoneContactDTO phoneContactDTO) {
        log.debug("Request to partially update PhoneContact : {}", phoneContactDTO);

        return phoneContactRepository
            .findById(phoneContactDTO.getId())
            .map(existingPhoneContact -> {
                phoneContactMapper.partialUpdate(existingPhoneContact, phoneContactDTO);

                return existingPhoneContact;
            })
            .map(phoneContactRepository::save)
            .map(phoneContactMapper::toDto);
    }

    /**
     * Get all the phoneContacts.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PhoneContactDTO> findAll() {
        log.debug("Request to get all PhoneContacts");
        return phoneContactRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(phoneContactMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the phoneContacts with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PhoneContactDTO> findAllWithEagerRelationships(Pageable pageable) {
        return phoneContactRepository.findAllWithEagerRelationships(pageable).map(phoneContactMapper::toDto);
    }

    /**
     * Get one phoneContact by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PhoneContactDTO> findOne(Long id) {
        log.debug("Request to get PhoneContact : {}", id);
        return phoneContactRepository.findOneWithEagerRelationships(id).map(phoneContactMapper::toDto);
    }

    /**
     * Delete the phoneContact by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PhoneContact : {}", id);
        phoneContactRepository.deleteById(id);
    }
}
