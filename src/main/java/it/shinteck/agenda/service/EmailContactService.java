package it.shinteck.agenda.service;

import it.shinteck.agenda.domain.EmailContact;
import it.shinteck.agenda.repository.EmailContactRepository;
import it.shinteck.agenda.service.dto.EmailContactDTO;
import it.shinteck.agenda.service.mapper.EmailContactMapper;
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
 * Service Implementation for managing {@link EmailContact}.
 */
@Service
@Transactional
public class EmailContactService {

    private final Logger log = LoggerFactory.getLogger(EmailContactService.class);

    private final EmailContactRepository emailContactRepository;

    private final EmailContactMapper emailContactMapper;

    public EmailContactService(EmailContactRepository emailContactRepository, EmailContactMapper emailContactMapper) {
        this.emailContactRepository = emailContactRepository;
        this.emailContactMapper = emailContactMapper;
    }

    /**
     * Save a emailContact.
     *
     * @param emailContactDTO the entity to save.
     * @return the persisted entity.
     */
    public EmailContactDTO save(EmailContactDTO emailContactDTO) {
        log.debug("Request to save EmailContact : {}", emailContactDTO);
        EmailContact emailContact = emailContactMapper.toEntity(emailContactDTO);
        emailContact = emailContactRepository.save(emailContact);
        return emailContactMapper.toDto(emailContact);
    }

    /**
     * Update a emailContact.
     *
     * @param emailContactDTO the entity to save.
     * @return the persisted entity.
     */
    public EmailContactDTO update(EmailContactDTO emailContactDTO) {
        log.debug("Request to save EmailContact : {}", emailContactDTO);
        EmailContact emailContact = emailContactMapper.toEntity(emailContactDTO);
        emailContact = emailContactRepository.save(emailContact);
        return emailContactMapper.toDto(emailContact);
    }

    /**
     * Partially update a emailContact.
     *
     * @param emailContactDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EmailContactDTO> partialUpdate(EmailContactDTO emailContactDTO) {
        log.debug("Request to partially update EmailContact : {}", emailContactDTO);

        return emailContactRepository
            .findById(emailContactDTO.getId())
            .map(existingEmailContact -> {
                emailContactMapper.partialUpdate(existingEmailContact, emailContactDTO);

                return existingEmailContact;
            })
            .map(emailContactRepository::save)
            .map(emailContactMapper::toDto);
    }

    /**
     * Get all the emailContacts.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<EmailContactDTO> findAll() {
        log.debug("Request to get all EmailContacts");
        return emailContactRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(emailContactMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the emailContacts with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<EmailContactDTO> findAllWithEagerRelationships(Pageable pageable) {
        return emailContactRepository.findAllWithEagerRelationships(pageable).map(emailContactMapper::toDto);
    }

    /**
     * Get one emailContact by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EmailContactDTO> findOne(Long id) {
        log.debug("Request to get EmailContact : {}", id);
        return emailContactRepository.findOneWithEagerRelationships(id).map(emailContactMapper::toDto);
    }

    /**
     * Delete the emailContact by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete EmailContact : {}", id);
        emailContactRepository.deleteById(id);
    }
}
