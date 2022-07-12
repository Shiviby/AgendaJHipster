package it.shinteck.agenda.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import it.shinteck.agenda.IntegrationTest;
import it.shinteck.agenda.domain.Contact;
import it.shinteck.agenda.domain.EmailContact;
import it.shinteck.agenda.domain.enumeration.EmailContactType;
import it.shinteck.agenda.repository.EmailContactRepository;
import it.shinteck.agenda.service.EmailContactService;
import it.shinteck.agenda.service.criteria.EmailContactCriteria;
import it.shinteck.agenda.service.dto.EmailContactDTO;
import it.shinteck.agenda.service.mapper.EmailContactMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EmailContactResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EmailContactResourceIT {

    private static final EmailContactType DEFAULT_TYPE = EmailContactType.HOME;
    private static final EmailContactType UPDATED_TYPE = EmailContactType.WORK;

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/email-contacts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmailContactRepository emailContactRepository;

    @Mock
    private EmailContactRepository emailContactRepositoryMock;

    @Autowired
    private EmailContactMapper emailContactMapper;

    @Mock
    private EmailContactService emailContactServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmailContactMockMvc;

    private EmailContact emailContact;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmailContact createEntity(EntityManager em) {
        EmailContact emailContact = new EmailContact().type(DEFAULT_TYPE).email(DEFAULT_EMAIL);
        // Add required entity
        Contact contact;
        if (TestUtil.findAll(em, Contact.class).isEmpty()) {
            contact = ContactResourceIT.createEntity(em);
            em.persist(contact);
            em.flush();
        } else {
            contact = TestUtil.findAll(em, Contact.class).get(0);
        }
        emailContact.setContact(contact);
        return emailContact;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmailContact createUpdatedEntity(EntityManager em) {
        EmailContact emailContact = new EmailContact().type(UPDATED_TYPE).email(UPDATED_EMAIL);
        // Add required entity
        Contact contact;
        if (TestUtil.findAll(em, Contact.class).isEmpty()) {
            contact = ContactResourceIT.createUpdatedEntity(em);
            em.persist(contact);
            em.flush();
        } else {
            contact = TestUtil.findAll(em, Contact.class).get(0);
        }
        emailContact.setContact(contact);
        return emailContact;
    }

    @BeforeEach
    public void initTest() {
        emailContact = createEntity(em);
    }

    @Test
    @Transactional
    void createEmailContact() throws Exception {
        int databaseSizeBeforeCreate = emailContactRepository.findAll().size();
        // Create the EmailContact
        EmailContactDTO emailContactDTO = emailContactMapper.toDto(emailContact);
        restEmailContactMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(emailContactDTO))
            )
            .andExpect(status().isCreated());

        // Validate the EmailContact in the database
        List<EmailContact> emailContactList = emailContactRepository.findAll();
        assertThat(emailContactList).hasSize(databaseSizeBeforeCreate + 1);
        EmailContact testEmailContact = emailContactList.get(emailContactList.size() - 1);
        assertThat(testEmailContact.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testEmailContact.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void createEmailContactWithExistingId() throws Exception {
        // Create the EmailContact with an existing ID
        emailContact.setId(1L);
        EmailContactDTO emailContactDTO = emailContactMapper.toDto(emailContact);

        int databaseSizeBeforeCreate = emailContactRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmailContactMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(emailContactDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailContact in the database
        List<EmailContact> emailContactList = emailContactRepository.findAll();
        assertThat(emailContactList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = emailContactRepository.findAll().size();
        // set the field null
        emailContact.setType(null);

        // Create the EmailContact, which fails.
        EmailContactDTO emailContactDTO = emailContactMapper.toDto(emailContact);

        restEmailContactMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(emailContactDTO))
            )
            .andExpect(status().isBadRequest());

        List<EmailContact> emailContactList = emailContactRepository.findAll();
        assertThat(emailContactList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = emailContactRepository.findAll().size();
        // set the field null
        emailContact.setEmail(null);

        // Create the EmailContact, which fails.
        EmailContactDTO emailContactDTO = emailContactMapper.toDto(emailContact);

        restEmailContactMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(emailContactDTO))
            )
            .andExpect(status().isBadRequest());

        List<EmailContact> emailContactList = emailContactRepository.findAll();
        assertThat(emailContactList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmailContacts() throws Exception {
        // Initialize the database
        emailContactRepository.saveAndFlush(emailContact);

        // Get all the emailContactList
        restEmailContactMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emailContact.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmailContactsWithEagerRelationshipsIsEnabled() throws Exception {
        when(emailContactServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmailContactMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(emailContactServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmailContactsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(emailContactServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmailContactMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(emailContactServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getEmailContact() throws Exception {
        // Initialize the database
        emailContactRepository.saveAndFlush(emailContact);

        // Get the emailContact
        restEmailContactMockMvc
            .perform(get(ENTITY_API_URL_ID, emailContact.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(emailContact.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    void getEmailContactsByIdFiltering() throws Exception {
        // Initialize the database
        emailContactRepository.saveAndFlush(emailContact);

        Long id = emailContact.getId();

        defaultEmailContactShouldBeFound("id.equals=" + id);
        defaultEmailContactShouldNotBeFound("id.notEquals=" + id);

        defaultEmailContactShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEmailContactShouldNotBeFound("id.greaterThan=" + id);

        defaultEmailContactShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEmailContactShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEmailContactsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        emailContactRepository.saveAndFlush(emailContact);

        // Get all the emailContactList where type equals to DEFAULT_TYPE
        defaultEmailContactShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the emailContactList where type equals to UPDATED_TYPE
        defaultEmailContactShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllEmailContactsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        emailContactRepository.saveAndFlush(emailContact);

        // Get all the emailContactList where type not equals to DEFAULT_TYPE
        defaultEmailContactShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the emailContactList where type not equals to UPDATED_TYPE
        defaultEmailContactShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllEmailContactsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        emailContactRepository.saveAndFlush(emailContact);

        // Get all the emailContactList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultEmailContactShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the emailContactList where type equals to UPDATED_TYPE
        defaultEmailContactShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllEmailContactsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        emailContactRepository.saveAndFlush(emailContact);

        // Get all the emailContactList where type is not null
        defaultEmailContactShouldBeFound("type.specified=true");

        // Get all the emailContactList where type is null
        defaultEmailContactShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailContactsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        emailContactRepository.saveAndFlush(emailContact);

        // Get all the emailContactList where email equals to DEFAULT_EMAIL
        defaultEmailContactShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the emailContactList where email equals to UPDATED_EMAIL
        defaultEmailContactShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmailContactsByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        emailContactRepository.saveAndFlush(emailContact);

        // Get all the emailContactList where email not equals to DEFAULT_EMAIL
        defaultEmailContactShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the emailContactList where email not equals to UPDATED_EMAIL
        defaultEmailContactShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmailContactsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        emailContactRepository.saveAndFlush(emailContact);

        // Get all the emailContactList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultEmailContactShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the emailContactList where email equals to UPDATED_EMAIL
        defaultEmailContactShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmailContactsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        emailContactRepository.saveAndFlush(emailContact);

        // Get all the emailContactList where email is not null
        defaultEmailContactShouldBeFound("email.specified=true");

        // Get all the emailContactList where email is null
        defaultEmailContactShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailContactsByEmailContainsSomething() throws Exception {
        // Initialize the database
        emailContactRepository.saveAndFlush(emailContact);

        // Get all the emailContactList where email contains DEFAULT_EMAIL
        defaultEmailContactShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the emailContactList where email contains UPDATED_EMAIL
        defaultEmailContactShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmailContactsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        emailContactRepository.saveAndFlush(emailContact);

        // Get all the emailContactList where email does not contain DEFAULT_EMAIL
        defaultEmailContactShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the emailContactList where email does not contain UPDATED_EMAIL
        defaultEmailContactShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmailContactsByContactIsEqualToSomething() throws Exception {
        // Initialize the database
        emailContactRepository.saveAndFlush(emailContact);
        Contact contact;
        if (TestUtil.findAll(em, Contact.class).isEmpty()) {
            contact = ContactResourceIT.createEntity(em);
            em.persist(contact);
            em.flush();
        } else {
            contact = TestUtil.findAll(em, Contact.class).get(0);
        }
        em.persist(contact);
        em.flush();
        emailContact.setContact(contact);
        emailContactRepository.saveAndFlush(emailContact);
        Long contactId = contact.getId();

        // Get all the emailContactList where contact equals to contactId
        defaultEmailContactShouldBeFound("contactId.equals=" + contactId);

        // Get all the emailContactList where contact equals to (contactId + 1)
        defaultEmailContactShouldNotBeFound("contactId.equals=" + (contactId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmailContactShouldBeFound(String filter) throws Exception {
        restEmailContactMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emailContact.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));

        // Check, that the count call also returns 1
        restEmailContactMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmailContactShouldNotBeFound(String filter) throws Exception {
        restEmailContactMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmailContactMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEmailContact() throws Exception {
        // Get the emailContact
        restEmailContactMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEmailContact() throws Exception {
        // Initialize the database
        emailContactRepository.saveAndFlush(emailContact);

        int databaseSizeBeforeUpdate = emailContactRepository.findAll().size();

        // Update the emailContact
        EmailContact updatedEmailContact = emailContactRepository.findById(emailContact.getId()).get();
        // Disconnect from session so that the updates on updatedEmailContact are not directly saved in db
        em.detach(updatedEmailContact);
        updatedEmailContact.type(UPDATED_TYPE).email(UPDATED_EMAIL);
        EmailContactDTO emailContactDTO = emailContactMapper.toDto(updatedEmailContact);

        restEmailContactMockMvc
            .perform(
                put(ENTITY_API_URL_ID, emailContactDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(emailContactDTO))
            )
            .andExpect(status().isOk());

        // Validate the EmailContact in the database
        List<EmailContact> emailContactList = emailContactRepository.findAll();
        assertThat(emailContactList).hasSize(databaseSizeBeforeUpdate);
        EmailContact testEmailContact = emailContactList.get(emailContactList.size() - 1);
        assertThat(testEmailContact.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testEmailContact.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void putNonExistingEmailContact() throws Exception {
        int databaseSizeBeforeUpdate = emailContactRepository.findAll().size();
        emailContact.setId(count.incrementAndGet());

        // Create the EmailContact
        EmailContactDTO emailContactDTO = emailContactMapper.toDto(emailContact);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmailContactMockMvc
            .perform(
                put(ENTITY_API_URL_ID, emailContactDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(emailContactDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailContact in the database
        List<EmailContact> emailContactList = emailContactRepository.findAll();
        assertThat(emailContactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmailContact() throws Exception {
        int databaseSizeBeforeUpdate = emailContactRepository.findAll().size();
        emailContact.setId(count.incrementAndGet());

        // Create the EmailContact
        EmailContactDTO emailContactDTO = emailContactMapper.toDto(emailContact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailContactMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(emailContactDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailContact in the database
        List<EmailContact> emailContactList = emailContactRepository.findAll();
        assertThat(emailContactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmailContact() throws Exception {
        int databaseSizeBeforeUpdate = emailContactRepository.findAll().size();
        emailContact.setId(count.incrementAndGet());

        // Create the EmailContact
        EmailContactDTO emailContactDTO = emailContactMapper.toDto(emailContact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailContactMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(emailContactDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmailContact in the database
        List<EmailContact> emailContactList = emailContactRepository.findAll();
        assertThat(emailContactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmailContactWithPatch() throws Exception {
        // Initialize the database
        emailContactRepository.saveAndFlush(emailContact);

        int databaseSizeBeforeUpdate = emailContactRepository.findAll().size();

        // Update the emailContact using partial update
        EmailContact partialUpdatedEmailContact = new EmailContact();
        partialUpdatedEmailContact.setId(emailContact.getId());

        restEmailContactMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmailContact.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmailContact))
            )
            .andExpect(status().isOk());

        // Validate the EmailContact in the database
        List<EmailContact> emailContactList = emailContactRepository.findAll();
        assertThat(emailContactList).hasSize(databaseSizeBeforeUpdate);
        EmailContact testEmailContact = emailContactList.get(emailContactList.size() - 1);
        assertThat(testEmailContact.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testEmailContact.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void fullUpdateEmailContactWithPatch() throws Exception {
        // Initialize the database
        emailContactRepository.saveAndFlush(emailContact);

        int databaseSizeBeforeUpdate = emailContactRepository.findAll().size();

        // Update the emailContact using partial update
        EmailContact partialUpdatedEmailContact = new EmailContact();
        partialUpdatedEmailContact.setId(emailContact.getId());

        partialUpdatedEmailContact.type(UPDATED_TYPE).email(UPDATED_EMAIL);

        restEmailContactMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmailContact.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmailContact))
            )
            .andExpect(status().isOk());

        // Validate the EmailContact in the database
        List<EmailContact> emailContactList = emailContactRepository.findAll();
        assertThat(emailContactList).hasSize(databaseSizeBeforeUpdate);
        EmailContact testEmailContact = emailContactList.get(emailContactList.size() - 1);
        assertThat(testEmailContact.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testEmailContact.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void patchNonExistingEmailContact() throws Exception {
        int databaseSizeBeforeUpdate = emailContactRepository.findAll().size();
        emailContact.setId(count.incrementAndGet());

        // Create the EmailContact
        EmailContactDTO emailContactDTO = emailContactMapper.toDto(emailContact);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmailContactMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, emailContactDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(emailContactDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailContact in the database
        List<EmailContact> emailContactList = emailContactRepository.findAll();
        assertThat(emailContactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmailContact() throws Exception {
        int databaseSizeBeforeUpdate = emailContactRepository.findAll().size();
        emailContact.setId(count.incrementAndGet());

        // Create the EmailContact
        EmailContactDTO emailContactDTO = emailContactMapper.toDto(emailContact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailContactMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(emailContactDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailContact in the database
        List<EmailContact> emailContactList = emailContactRepository.findAll();
        assertThat(emailContactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmailContact() throws Exception {
        int databaseSizeBeforeUpdate = emailContactRepository.findAll().size();
        emailContact.setId(count.incrementAndGet());

        // Create the EmailContact
        EmailContactDTO emailContactDTO = emailContactMapper.toDto(emailContact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailContactMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(emailContactDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmailContact in the database
        List<EmailContact> emailContactList = emailContactRepository.findAll();
        assertThat(emailContactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmailContact() throws Exception {
        // Initialize the database
        emailContactRepository.saveAndFlush(emailContact);

        int databaseSizeBeforeDelete = emailContactRepository.findAll().size();

        // Delete the emailContact
        restEmailContactMockMvc
            .perform(delete(ENTITY_API_URL_ID, emailContact.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EmailContact> emailContactList = emailContactRepository.findAll();
        assertThat(emailContactList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
