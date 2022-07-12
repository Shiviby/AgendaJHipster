package it.shinteck.agenda.web.rest;

import it.shinteck.agenda.repository.PhoneContactRepository;
import it.shinteck.agenda.service.PhoneContactQueryService;
import it.shinteck.agenda.service.PhoneContactService;
import it.shinteck.agenda.service.criteria.PhoneContactCriteria;
import it.shinteck.agenda.service.dto.PhoneContactDTO;
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
 * REST controller for managing {@link it.shinteck.agenda.domain.PhoneContact}.
 */
@RestController
@RequestMapping("/api")
public class PhoneContactResource {

    private final Logger log = LoggerFactory.getLogger(PhoneContactResource.class);

    private static final String ENTITY_NAME = "phoneContact";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PhoneContactService phoneContactService;

    private final PhoneContactRepository phoneContactRepository;

    private final PhoneContactQueryService phoneContactQueryService;

    public PhoneContactResource(
        PhoneContactService phoneContactService,
        PhoneContactRepository phoneContactRepository,
        PhoneContactQueryService phoneContactQueryService
    ) {
        this.phoneContactService = phoneContactService;
        this.phoneContactRepository = phoneContactRepository;
        this.phoneContactQueryService = phoneContactQueryService;
    }

    /**
     * {@code POST  /phone-contacts} : Create a new phoneContact.
     *
     * @param phoneContactDTO the phoneContactDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new phoneContactDTO, or with status {@code 400 (Bad Request)} if the phoneContact has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/phone-contacts")
    public ResponseEntity<PhoneContactDTO> createPhoneContact(@Valid @RequestBody PhoneContactDTO phoneContactDTO)
        throws URISyntaxException {
        log.debug("REST request to save PhoneContact : {}", phoneContactDTO);
        if (phoneContactDTO.getId() != null) {
            throw new BadRequestAlertException("A new phoneContact cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PhoneContactDTO result = phoneContactService.save(phoneContactDTO);
        return ResponseEntity
            .created(new URI("/api/phone-contacts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /phone-contacts/:id} : Updates an existing phoneContact.
     *
     * @param id the id of the phoneContactDTO to save.
     * @param phoneContactDTO the phoneContactDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated phoneContactDTO,
     * or with status {@code 400 (Bad Request)} if the phoneContactDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the phoneContactDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/phone-contacts/{id}")
    public ResponseEntity<PhoneContactDTO> updatePhoneContact(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PhoneContactDTO phoneContactDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PhoneContact : {}, {}", id, phoneContactDTO);
        if (phoneContactDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, phoneContactDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!phoneContactRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PhoneContactDTO result = phoneContactService.update(phoneContactDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, phoneContactDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /phone-contacts/:id} : Partial updates given fields of an existing phoneContact, field will ignore if it is null
     *
     * @param id the id of the phoneContactDTO to save.
     * @param phoneContactDTO the phoneContactDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated phoneContactDTO,
     * or with status {@code 400 (Bad Request)} if the phoneContactDTO is not valid,
     * or with status {@code 404 (Not Found)} if the phoneContactDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the phoneContactDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/phone-contacts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PhoneContactDTO> partialUpdatePhoneContact(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PhoneContactDTO phoneContactDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PhoneContact partially : {}, {}", id, phoneContactDTO);
        if (phoneContactDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, phoneContactDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!phoneContactRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PhoneContactDTO> result = phoneContactService.partialUpdate(phoneContactDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, phoneContactDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /phone-contacts} : get all the phoneContacts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of phoneContacts in body.
     */
    @GetMapping("/phone-contacts")
    public ResponseEntity<List<PhoneContactDTO>> getAllPhoneContacts(PhoneContactCriteria criteria) {
        log.debug("REST request to get PhoneContacts by criteria: {}", criteria);
        List<PhoneContactDTO> entityList = phoneContactQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /phone-contacts/count} : count all the phoneContacts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/phone-contacts/count")
    public ResponseEntity<Long> countPhoneContacts(PhoneContactCriteria criteria) {
        log.debug("REST request to count PhoneContacts by criteria: {}", criteria);
        return ResponseEntity.ok().body(phoneContactQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /phone-contacts/:id} : get the "id" phoneContact.
     *
     * @param id the id of the phoneContactDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the phoneContactDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/phone-contacts/{id}")
    public ResponseEntity<PhoneContactDTO> getPhoneContact(@PathVariable Long id) {
        log.debug("REST request to get PhoneContact : {}", id);
        Optional<PhoneContactDTO> phoneContactDTO = phoneContactService.findOne(id);
        return ResponseUtil.wrapOrNotFound(phoneContactDTO);
    }

    /**
     * {@code DELETE  /phone-contacts/:id} : delete the "id" phoneContact.
     *
     * @param id the id of the phoneContactDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/phone-contacts/{id}")
    public ResponseEntity<Void> deletePhoneContact(@PathVariable Long id) {
        log.debug("REST request to delete PhoneContact : {}", id);
        phoneContactService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
