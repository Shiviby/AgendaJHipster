package it.shinteck.agenda.web.rest;

import it.shinteck.agenda.repository.EmailContactRepository;
import it.shinteck.agenda.service.EmailContactQueryService;
import it.shinteck.agenda.service.EmailContactService;
import it.shinteck.agenda.service.criteria.EmailContactCriteria;
import it.shinteck.agenda.service.dto.EmailContactDTO;
import it.shinteck.agenda.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link it.shinteck.agenda.domain.EmailContact}.
 */
@RestController
@RequestMapping("/api")
public class EmailContactResource {

    private final Logger log = LoggerFactory.getLogger(EmailContactResource.class);

    private static final String ENTITY_NAME = "emailContact";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmailContactService emailContactService;

    private final EmailContactRepository emailContactRepository;

    private final EmailContactQueryService emailContactQueryService;

    public EmailContactResource(
        EmailContactService emailContactService,
        EmailContactRepository emailContactRepository,
        EmailContactQueryService emailContactQueryService
    ) {
        this.emailContactService = emailContactService;
        this.emailContactRepository = emailContactRepository;
        this.emailContactQueryService = emailContactQueryService;
    }

    /**
     * {@code POST  /email-contacts} : Create a new emailContact.
     *
     * @param emailContactDTO the emailContactDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new emailContactDTO, or with status {@code 400 (Bad Request)} if the emailContact has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/email-contacts")
    public ResponseEntity<EmailContactDTO> createEmailContact(@Valid @RequestBody EmailContactDTO emailContactDTO)
        throws URISyntaxException {
        log.debug("REST request to save EmailContact : {}", emailContactDTO);
        if (emailContactDTO.getId() != null) {
            throw new BadRequestAlertException("A new emailContact cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmailContactDTO result = emailContactService.save(emailContactDTO);
        return ResponseEntity
            .created(new URI("/api/email-contacts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /email-contacts/:id} : Updates an existing emailContact.
     *
     * @param id the id of the emailContactDTO to save.
     * @param emailContactDTO the emailContactDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emailContactDTO,
     * or with status {@code 400 (Bad Request)} if the emailContactDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the emailContactDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/email-contacts/{id}")
    public ResponseEntity<EmailContactDTO> updateEmailContact(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EmailContactDTO emailContactDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EmailContact : {}, {}", id, emailContactDTO);
        if (emailContactDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emailContactDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emailContactRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EmailContactDTO result = emailContactService.update(emailContactDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, emailContactDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /email-contacts/:id} : Partial updates given fields of an existing emailContact, field will ignore if it is null
     *
     * @param id the id of the emailContactDTO to save.
     * @param emailContactDTO the emailContactDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emailContactDTO,
     * or with status {@code 400 (Bad Request)} if the emailContactDTO is not valid,
     * or with status {@code 404 (Not Found)} if the emailContactDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the emailContactDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/email-contacts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmailContactDTO> partialUpdateEmailContact(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EmailContactDTO emailContactDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EmailContact partially : {}, {}", id, emailContactDTO);
        if (emailContactDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emailContactDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emailContactRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmailContactDTO> result = emailContactService.partialUpdate(emailContactDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, emailContactDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /email-contacts} : get all the emailContacts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of emailContacts in body.
     */
    @GetMapping("/email-contacts")
    public ResponseEntity<List<EmailContactDTO>> getAllEmailContacts(EmailContactCriteria criteria) {
        log.debug("REST request to get EmailContacts by criteria: {}", criteria);
        List<EmailContactDTO> entityList = emailContactQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /email-contacts/count} : count all the emailContacts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/email-contacts/count")
    public ResponseEntity<Long> countEmailContacts(EmailContactCriteria criteria) {
        log.debug("REST request to count EmailContacts by criteria: {}", criteria);
        return ResponseEntity.ok().body(emailContactQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /email-contacts/:id} : get the "id" emailContact.
     *
     * @param id the id of the emailContactDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the emailContactDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/email-contacts/{id}")
    public ResponseEntity<EmailContactDTO> getEmailContact(@PathVariable Long id) {
        log.debug("REST request to get EmailContact : {}", id);
        Optional<EmailContactDTO> emailContactDTO = emailContactService.findOne(id);
        return ResponseUtil.wrapOrNotFound(emailContactDTO);
    }

    /**
     * {@code DELETE  /email-contacts/:id} : delete the "id" emailContact.
     *
     * @param id the id of the emailContactDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/email-contacts/{id}")
    public ResponseEntity<Void> deleteEmailContact(@PathVariable Long id) {
        log.debug("REST request to delete EmailContact : {}", id);
        emailContactService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
