package it.shinteck.agenda.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import it.shinteck.agenda.IntegrationTest;
import it.shinteck.agenda.domain.Contact;
import it.shinteck.agenda.domain.PhoneContact;
import it.shinteck.agenda.domain.enumeration.PhoneContactType;
import it.shinteck.agenda.repository.PhoneContactRepository;
import it.shinteck.agenda.service.PhoneContactService;
import it.shinteck.agenda.service.criteria.PhoneContactCriteria;
import it.shinteck.agenda.service.dto.PhoneContactDTO;
import it.shinteck.agenda.service.mapper.PhoneContactMapper;
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
 * Integration tests for the {@link PhoneContactResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PhoneContactResourceIT {

    private static final PhoneContactType DEFAULT_TYPE = PhoneContactType.HOME;
    private static final PhoneContactType UPDATED_TYPE = PhoneContactType.PERSONAL;

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/phone-contacts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PhoneContactRepository phoneContactRepository;

    @Mock
    private PhoneContactRepository phoneContactRepositoryMock;

    @Autowired
    private PhoneContactMapper phoneContactMapper;

    @Mock
    private PhoneContactService phoneContactServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPhoneContactMockMvc;

    private PhoneContact phoneContact;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PhoneContact createEntity(EntityManager em) {
        PhoneContact phoneContact = new PhoneContact().type(DEFAULT_TYPE).phone(DEFAULT_PHONE);
        // Add required entity
        Contact contact;
        if (TestUtil.findAll(em, Contact.class).isEmpty()) {
            contact = ContactResourceIT.createEntity(em);
            em.persist(contact);
            em.flush();
        } else {
            contact = TestUtil.findAll(em, Contact.class).get(0);
        }
        phoneContact.setContact(contact);
        return phoneContact;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PhoneContact createUpdatedEntity(EntityManager em) {
        PhoneContact phoneContact = new PhoneContact().type(UPDATED_TYPE).phone(UPDATED_PHONE);
        // Add required entity
        Contact contact;
        if (TestUtil.findAll(em, Contact.class).isEmpty()) {
            contact = ContactResourceIT.createUpdatedEntity(em);
            em.persist(contact);
            em.flush();
        } else {
            contact = TestUtil.findAll(em, Contact.class).get(0);
        }
        phoneContact.setContact(contact);
        return phoneContact;
    }

    @BeforeEach
    public void initTest() {
        phoneContact = createEntity(em);
    }

    @Test
    @Transactional
    void createPhoneContact() throws Exception {
        int databaseSizeBeforeCreate = phoneContactRepository.findAll().size();
        // Create the PhoneContact
        PhoneContactDTO phoneContactDTO = phoneContactMapper.toDto(phoneContact);
        restPhoneContactMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(phoneContactDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PhoneContact in the database
        List<PhoneContact> phoneContactList = phoneContactRepository.findAll();
        assertThat(phoneContactList).hasSize(databaseSizeBeforeCreate + 1);
        PhoneContact testPhoneContact = phoneContactList.get(phoneContactList.size() - 1);
        assertThat(testPhoneContact.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testPhoneContact.getPhone()).isEqualTo(DEFAULT_PHONE);
    }

    @Test
    @Transactional
    void createPhoneContactWithExistingId() throws Exception {
        // Create the PhoneContact with an existing ID
        phoneContact.setId(1L);
        PhoneContactDTO phoneContactDTO = phoneContactMapper.toDto(phoneContact);

        int databaseSizeBeforeCreate = phoneContactRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPhoneContactMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(phoneContactDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PhoneContact in the database
        List<PhoneContact> phoneContactList = phoneContactRepository.findAll();
        assertThat(phoneContactList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = phoneContactRepository.findAll().size();
        // set the field null
        phoneContact.setType(null);

        // Create the PhoneContact, which fails.
        PhoneContactDTO phoneContactDTO = phoneContactMapper.toDto(phoneContact);

        restPhoneContactMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(phoneContactDTO))
            )
            .andExpect(status().isBadRequest());

        List<PhoneContact> phoneContactList = phoneContactRepository.findAll();
        assertThat(phoneContactList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = phoneContactRepository.findAll().size();
        // set the field null
        phoneContact.setPhone(null);

        // Create the PhoneContact, which fails.
        PhoneContactDTO phoneContactDTO = phoneContactMapper.toDto(phoneContact);

        restPhoneContactMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(phoneContactDTO))
            )
            .andExpect(status().isBadRequest());

        List<PhoneContact> phoneContactList = phoneContactRepository.findAll();
        assertThat(phoneContactList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPhoneContacts() throws Exception {
        // Initialize the database
        phoneContactRepository.saveAndFlush(phoneContact);

        // Get all the phoneContactList
        restPhoneContactMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(phoneContact.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPhoneContactsWithEagerRelationshipsIsEnabled() throws Exception {
        when(phoneContactServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPhoneContactMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(phoneContactServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPhoneContactsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(phoneContactServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPhoneContactMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(phoneContactServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getPhoneContact() throws Exception {
        // Initialize the database
        phoneContactRepository.saveAndFlush(phoneContact);

        // Get the phoneContact
        restPhoneContactMockMvc
            .perform(get(ENTITY_API_URL_ID, phoneContact.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(phoneContact.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE));
    }

    @Test
    @Transactional
    void getPhoneContactsByIdFiltering() throws Exception {
        // Initialize the database
        phoneContactRepository.saveAndFlush(phoneContact);

        Long id = phoneContact.getId();

        defaultPhoneContactShouldBeFound("id.equals=" + id);
        defaultPhoneContactShouldNotBeFound("id.notEquals=" + id);

        defaultPhoneContactShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPhoneContactShouldNotBeFound("id.greaterThan=" + id);

        defaultPhoneContactShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPhoneContactShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPhoneContactsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        phoneContactRepository.saveAndFlush(phoneContact);

        // Get all the phoneContactList where type equals to DEFAULT_TYPE
        defaultPhoneContactShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the phoneContactList where type equals to UPDATED_TYPE
        defaultPhoneContactShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllPhoneContactsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        phoneContactRepository.saveAndFlush(phoneContact);

        // Get all the phoneContactList where type not equals to DEFAULT_TYPE
        defaultPhoneContactShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the phoneContactList where type not equals to UPDATED_TYPE
        defaultPhoneContactShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllPhoneContactsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        phoneContactRepository.saveAndFlush(phoneContact);

        // Get all the phoneContactList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultPhoneContactShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the phoneContactList where type equals to UPDATED_TYPE
        defaultPhoneContactShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllPhoneContactsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        phoneContactRepository.saveAndFlush(phoneContact);

        // Get all the phoneContactList where type is not null
        defaultPhoneContactShouldBeFound("type.specified=true");

        // Get all the phoneContactList where type is null
        defaultPhoneContactShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllPhoneContactsByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        phoneContactRepository.saveAndFlush(phoneContact);

        // Get all the phoneContactList where phone equals to DEFAULT_PHONE
        defaultPhoneContactShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the phoneContactList where phone equals to UPDATED_PHONE
        defaultPhoneContactShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllPhoneContactsByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        phoneContactRepository.saveAndFlush(phoneContact);

        // Get all the phoneContactList where phone not equals to DEFAULT_PHONE
        defaultPhoneContactShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the phoneContactList where phone not equals to UPDATED_PHONE
        defaultPhoneContactShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllPhoneContactsByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        phoneContactRepository.saveAndFlush(phoneContact);

        // Get all the phoneContactList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultPhoneContactShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the phoneContactList where phone equals to UPDATED_PHONE
        defaultPhoneContactShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllPhoneContactsByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        phoneContactRepository.saveAndFlush(phoneContact);

        // Get all the phoneContactList where phone is not null
        defaultPhoneContactShouldBeFound("phone.specified=true");

        // Get all the phoneContactList where phone is null
        defaultPhoneContactShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    void getAllPhoneContactsByPhoneContainsSomething() throws Exception {
        // Initialize the database
        phoneContactRepository.saveAndFlush(phoneContact);

        // Get all the phoneContactList where phone contains DEFAULT_PHONE
        defaultPhoneContactShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the phoneContactList where phone contains UPDATED_PHONE
        defaultPhoneContactShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllPhoneContactsByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        phoneContactRepository.saveAndFlush(phoneContact);

        // Get all the phoneContactList where phone does not contain DEFAULT_PHONE
        defaultPhoneContactShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the phoneContactList where phone does not contain UPDATED_PHONE
        defaultPhoneContactShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllPhoneContactsByContactIsEqualToSomething() throws Exception {
        // Initialize the database
        phoneContactRepository.saveAndFlush(phoneContact);
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
        phoneContact.setContact(contact);
        phoneContactRepository.saveAndFlush(phoneContact);
        Long contactId = contact.getId();

        // Get all the phoneContactList where contact equals to contactId
        defaultPhoneContactShouldBeFound("contactId.equals=" + contactId);

        // Get all the phoneContactList where contact equals to (contactId + 1)
        defaultPhoneContactShouldNotBeFound("contactId.equals=" + (contactId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPhoneContactShouldBeFound(String filter) throws Exception {
        restPhoneContactMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(phoneContact.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)));

        // Check, that the count call also returns 1
        restPhoneContactMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPhoneContactShouldNotBeFound(String filter) throws Exception {
        restPhoneContactMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPhoneContactMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPhoneContact() throws Exception {
        // Get the phoneContact
        restPhoneContactMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPhoneContact() throws Exception {
        // Initialize the database
        phoneContactRepository.saveAndFlush(phoneContact);

        int databaseSizeBeforeUpdate = phoneContactRepository.findAll().size();

        // Update the phoneContact
        PhoneContact updatedPhoneContact = phoneContactRepository.findById(phoneContact.getId()).get();
        // Disconnect from session so that the updates on updatedPhoneContact are not directly saved in db
        em.detach(updatedPhoneContact);
        updatedPhoneContact.type(UPDATED_TYPE).phone(UPDATED_PHONE);
        PhoneContactDTO phoneContactDTO = phoneContactMapper.toDto(updatedPhoneContact);

        restPhoneContactMockMvc
            .perform(
                put(ENTITY_API_URL_ID, phoneContactDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(phoneContactDTO))
            )
            .andExpect(status().isOk());

        // Validate the PhoneContact in the database
        List<PhoneContact> phoneContactList = phoneContactRepository.findAll();
        assertThat(phoneContactList).hasSize(databaseSizeBeforeUpdate);
        PhoneContact testPhoneContact = phoneContactList.get(phoneContactList.size() - 1);
        assertThat(testPhoneContact.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPhoneContact.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    void putNonExistingPhoneContact() throws Exception {
        int databaseSizeBeforeUpdate = phoneContactRepository.findAll().size();
        phoneContact.setId(count.incrementAndGet());

        // Create the PhoneContact
        PhoneContactDTO phoneContactDTO = phoneContactMapper.toDto(phoneContact);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhoneContactMockMvc
            .perform(
                put(ENTITY_API_URL_ID, phoneContactDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(phoneContactDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PhoneContact in the database
        List<PhoneContact> phoneContactList = phoneContactRepository.findAll();
        assertThat(phoneContactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPhoneContact() throws Exception {
        int databaseSizeBeforeUpdate = phoneContactRepository.findAll().size();
        phoneContact.setId(count.incrementAndGet());

        // Create the PhoneContact
        PhoneContactDTO phoneContactDTO = phoneContactMapper.toDto(phoneContact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhoneContactMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(phoneContactDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PhoneContact in the database
        List<PhoneContact> phoneContactList = phoneContactRepository.findAll();
        assertThat(phoneContactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPhoneContact() throws Exception {
        int databaseSizeBeforeUpdate = phoneContactRepository.findAll().size();
        phoneContact.setId(count.incrementAndGet());

        // Create the PhoneContact
        PhoneContactDTO phoneContactDTO = phoneContactMapper.toDto(phoneContact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhoneContactMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(phoneContactDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PhoneContact in the database
        List<PhoneContact> phoneContactList = phoneContactRepository.findAll();
        assertThat(phoneContactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePhoneContactWithPatch() throws Exception {
        // Initialize the database
        phoneContactRepository.saveAndFlush(phoneContact);

        int databaseSizeBeforeUpdate = phoneContactRepository.findAll().size();

        // Update the phoneContact using partial update
        PhoneContact partialUpdatedPhoneContact = new PhoneContact();
        partialUpdatedPhoneContact.setId(phoneContact.getId());

        restPhoneContactMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPhoneContact.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPhoneContact))
            )
            .andExpect(status().isOk());

        // Validate the PhoneContact in the database
        List<PhoneContact> phoneContactList = phoneContactRepository.findAll();
        assertThat(phoneContactList).hasSize(databaseSizeBeforeUpdate);
        PhoneContact testPhoneContact = phoneContactList.get(phoneContactList.size() - 1);
        assertThat(testPhoneContact.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testPhoneContact.getPhone()).isEqualTo(DEFAULT_PHONE);
    }

    @Test
    @Transactional
    void fullUpdatePhoneContactWithPatch() throws Exception {
        // Initialize the database
        phoneContactRepository.saveAndFlush(phoneContact);

        int databaseSizeBeforeUpdate = phoneContactRepository.findAll().size();

        // Update the phoneContact using partial update
        PhoneContact partialUpdatedPhoneContact = new PhoneContact();
        partialUpdatedPhoneContact.setId(phoneContact.getId());

        partialUpdatedPhoneContact.type(UPDATED_TYPE).phone(UPDATED_PHONE);

        restPhoneContactMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPhoneContact.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPhoneContact))
            )
            .andExpect(status().isOk());

        // Validate the PhoneContact in the database
        List<PhoneContact> phoneContactList = phoneContactRepository.findAll();
        assertThat(phoneContactList).hasSize(databaseSizeBeforeUpdate);
        PhoneContact testPhoneContact = phoneContactList.get(phoneContactList.size() - 1);
        assertThat(testPhoneContact.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPhoneContact.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    void patchNonExistingPhoneContact() throws Exception {
        int databaseSizeBeforeUpdate = phoneContactRepository.findAll().size();
        phoneContact.setId(count.incrementAndGet());

        // Create the PhoneContact
        PhoneContactDTO phoneContactDTO = phoneContactMapper.toDto(phoneContact);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhoneContactMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, phoneContactDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(phoneContactDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PhoneContact in the database
        List<PhoneContact> phoneContactList = phoneContactRepository.findAll();
        assertThat(phoneContactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPhoneContact() throws Exception {
        int databaseSizeBeforeUpdate = phoneContactRepository.findAll().size();
        phoneContact.setId(count.incrementAndGet());

        // Create the PhoneContact
        PhoneContactDTO phoneContactDTO = phoneContactMapper.toDto(phoneContact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhoneContactMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(phoneContactDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PhoneContact in the database
        List<PhoneContact> phoneContactList = phoneContactRepository.findAll();
        assertThat(phoneContactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPhoneContact() throws Exception {
        int databaseSizeBeforeUpdate = phoneContactRepository.findAll().size();
        phoneContact.setId(count.incrementAndGet());

        // Create the PhoneContact
        PhoneContactDTO phoneContactDTO = phoneContactMapper.toDto(phoneContact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhoneContactMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(phoneContactDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PhoneContact in the database
        List<PhoneContact> phoneContactList = phoneContactRepository.findAll();
        assertThat(phoneContactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePhoneContact() throws Exception {
        // Initialize the database
        phoneContactRepository.saveAndFlush(phoneContact);

        int databaseSizeBeforeDelete = phoneContactRepository.findAll().size();

        // Delete the phoneContact
        restPhoneContactMockMvc
            .perform(delete(ENTITY_API_URL_ID, phoneContact.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PhoneContact> phoneContactList = phoneContactRepository.findAll();
        assertThat(phoneContactList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
