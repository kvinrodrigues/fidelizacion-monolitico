package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Client;
import com.mycompany.myapp.domain.Nacionality;
import com.mycompany.myapp.repository.NacionalityRepository;
import com.mycompany.myapp.service.criteria.NacionalityCriteria;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link NacionalityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NacionalityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/nacionalities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NacionalityRepository nacionalityRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNacionalityMockMvc;

    private Nacionality nacionality;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Nacionality createEntity(EntityManager em) {
        Nacionality nacionality = new Nacionality().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return nacionality;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Nacionality createUpdatedEntity(EntityManager em) {
        Nacionality nacionality = new Nacionality().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return nacionality;
    }

    @BeforeEach
    public void initTest() {
        nacionality = createEntity(em);
    }

    @Test
    @Transactional
    void createNacionality() throws Exception {
        int databaseSizeBeforeCreate = nacionalityRepository.findAll().size();
        // Create the Nacionality
        restNacionalityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nacionality)))
            .andExpect(status().isCreated());

        // Validate the Nacionality in the database
        List<Nacionality> nacionalityList = nacionalityRepository.findAll();
        assertThat(nacionalityList).hasSize(databaseSizeBeforeCreate + 1);
        Nacionality testNacionality = nacionalityList.get(nacionalityList.size() - 1);
        assertThat(testNacionality.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testNacionality.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createNacionalityWithExistingId() throws Exception {
        // Create the Nacionality with an existing ID
        nacionality.setId(1L);

        int databaseSizeBeforeCreate = nacionalityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNacionalityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nacionality)))
            .andExpect(status().isBadRequest());

        // Validate the Nacionality in the database
        List<Nacionality> nacionalityList = nacionalityRepository.findAll();
        assertThat(nacionalityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = nacionalityRepository.findAll().size();
        // set the field null
        nacionality.setName(null);

        // Create the Nacionality, which fails.

        restNacionalityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nacionality)))
            .andExpect(status().isBadRequest());

        List<Nacionality> nacionalityList = nacionalityRepository.findAll();
        assertThat(nacionalityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = nacionalityRepository.findAll().size();
        // set the field null
        nacionality.setDescription(null);

        // Create the Nacionality, which fails.

        restNacionalityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nacionality)))
            .andExpect(status().isBadRequest());

        List<Nacionality> nacionalityList = nacionalityRepository.findAll();
        assertThat(nacionalityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNacionalities() throws Exception {
        // Initialize the database
        nacionalityRepository.saveAndFlush(nacionality);

        // Get all the nacionalityList
        restNacionalityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nacionality.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getNacionality() throws Exception {
        // Initialize the database
        nacionalityRepository.saveAndFlush(nacionality);

        // Get the nacionality
        restNacionalityMockMvc
            .perform(get(ENTITY_API_URL_ID, nacionality.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nacionality.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNacionalitiesByIdFiltering() throws Exception {
        // Initialize the database
        nacionalityRepository.saveAndFlush(nacionality);

        Long id = nacionality.getId();

        defaultNacionalityShouldBeFound("id.equals=" + id);
        defaultNacionalityShouldNotBeFound("id.notEquals=" + id);

        defaultNacionalityShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultNacionalityShouldNotBeFound("id.greaterThan=" + id);

        defaultNacionalityShouldBeFound("id.lessThanOrEqual=" + id);
        defaultNacionalityShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNacionalitiesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        nacionalityRepository.saveAndFlush(nacionality);

        // Get all the nacionalityList where name equals to DEFAULT_NAME
        defaultNacionalityShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the nacionalityList where name equals to UPDATED_NAME
        defaultNacionalityShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNacionalitiesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        nacionalityRepository.saveAndFlush(nacionality);

        // Get all the nacionalityList where name not equals to DEFAULT_NAME
        defaultNacionalityShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the nacionalityList where name not equals to UPDATED_NAME
        defaultNacionalityShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNacionalitiesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        nacionalityRepository.saveAndFlush(nacionality);

        // Get all the nacionalityList where name in DEFAULT_NAME or UPDATED_NAME
        defaultNacionalityShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the nacionalityList where name equals to UPDATED_NAME
        defaultNacionalityShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNacionalitiesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        nacionalityRepository.saveAndFlush(nacionality);

        // Get all the nacionalityList where name is not null
        defaultNacionalityShouldBeFound("name.specified=true");

        // Get all the nacionalityList where name is null
        defaultNacionalityShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllNacionalitiesByNameContainsSomething() throws Exception {
        // Initialize the database
        nacionalityRepository.saveAndFlush(nacionality);

        // Get all the nacionalityList where name contains DEFAULT_NAME
        defaultNacionalityShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the nacionalityList where name contains UPDATED_NAME
        defaultNacionalityShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNacionalitiesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        nacionalityRepository.saveAndFlush(nacionality);

        // Get all the nacionalityList where name does not contain DEFAULT_NAME
        defaultNacionalityShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the nacionalityList where name does not contain UPDATED_NAME
        defaultNacionalityShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNacionalitiesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        nacionalityRepository.saveAndFlush(nacionality);

        // Get all the nacionalityList where description equals to DEFAULT_DESCRIPTION
        defaultNacionalityShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the nacionalityList where description equals to UPDATED_DESCRIPTION
        defaultNacionalityShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllNacionalitiesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        nacionalityRepository.saveAndFlush(nacionality);

        // Get all the nacionalityList where description not equals to DEFAULT_DESCRIPTION
        defaultNacionalityShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the nacionalityList where description not equals to UPDATED_DESCRIPTION
        defaultNacionalityShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllNacionalitiesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        nacionalityRepository.saveAndFlush(nacionality);

        // Get all the nacionalityList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultNacionalityShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the nacionalityList where description equals to UPDATED_DESCRIPTION
        defaultNacionalityShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllNacionalitiesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        nacionalityRepository.saveAndFlush(nacionality);

        // Get all the nacionalityList where description is not null
        defaultNacionalityShouldBeFound("description.specified=true");

        // Get all the nacionalityList where description is null
        defaultNacionalityShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllNacionalitiesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        nacionalityRepository.saveAndFlush(nacionality);

        // Get all the nacionalityList where description contains DEFAULT_DESCRIPTION
        defaultNacionalityShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the nacionalityList where description contains UPDATED_DESCRIPTION
        defaultNacionalityShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllNacionalitiesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        nacionalityRepository.saveAndFlush(nacionality);

        // Get all the nacionalityList where description does not contain DEFAULT_DESCRIPTION
        defaultNacionalityShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the nacionalityList where description does not contain UPDATED_DESCRIPTION
        defaultNacionalityShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllNacionalitiesByClientIsEqualToSomething() throws Exception {
        // Initialize the database
        nacionalityRepository.saveAndFlush(nacionality);
        Client client;
        if (TestUtil.findAll(em, Client.class).isEmpty()) {
            client = ClientResourceIT.createEntity(em);
            em.persist(client);
            em.flush();
        } else {
            client = TestUtil.findAll(em, Client.class).get(0);
        }
        em.persist(client);
        em.flush();
        nacionality.addClient(client);
        nacionalityRepository.saveAndFlush(nacionality);
        Long clientId = client.getId();

        // Get all the nacionalityList where client equals to clientId
        defaultNacionalityShouldBeFound("clientId.equals=" + clientId);

        // Get all the nacionalityList where client equals to (clientId + 1)
        defaultNacionalityShouldNotBeFound("clientId.equals=" + (clientId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNacionalityShouldBeFound(String filter) throws Exception {
        restNacionalityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nacionality.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restNacionalityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNacionalityShouldNotBeFound(String filter) throws Exception {
        restNacionalityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNacionalityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNacionality() throws Exception {
        // Get the nacionality
        restNacionalityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewNacionality() throws Exception {
        // Initialize the database
        nacionalityRepository.saveAndFlush(nacionality);

        int databaseSizeBeforeUpdate = nacionalityRepository.findAll().size();

        // Update the nacionality
        Nacionality updatedNacionality = nacionalityRepository.findById(nacionality.getId()).get();
        // Disconnect from session so that the updates on updatedNacionality are not directly saved in db
        em.detach(updatedNacionality);
        updatedNacionality.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restNacionalityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNacionality.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedNacionality))
            )
            .andExpect(status().isOk());

        // Validate the Nacionality in the database
        List<Nacionality> nacionalityList = nacionalityRepository.findAll();
        assertThat(nacionalityList).hasSize(databaseSizeBeforeUpdate);
        Nacionality testNacionality = nacionalityList.get(nacionalityList.size() - 1);
        assertThat(testNacionality.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testNacionality.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingNacionality() throws Exception {
        int databaseSizeBeforeUpdate = nacionalityRepository.findAll().size();
        nacionality.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNacionalityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nacionality.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nacionality))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nacionality in the database
        List<Nacionality> nacionalityList = nacionalityRepository.findAll();
        assertThat(nacionalityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNacionality() throws Exception {
        int databaseSizeBeforeUpdate = nacionalityRepository.findAll().size();
        nacionality.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNacionalityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nacionality))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nacionality in the database
        List<Nacionality> nacionalityList = nacionalityRepository.findAll();
        assertThat(nacionalityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNacionality() throws Exception {
        int databaseSizeBeforeUpdate = nacionalityRepository.findAll().size();
        nacionality.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNacionalityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nacionality)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Nacionality in the database
        List<Nacionality> nacionalityList = nacionalityRepository.findAll();
        assertThat(nacionalityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNacionalityWithPatch() throws Exception {
        // Initialize the database
        nacionalityRepository.saveAndFlush(nacionality);

        int databaseSizeBeforeUpdate = nacionalityRepository.findAll().size();

        // Update the nacionality using partial update
        Nacionality partialUpdatedNacionality = new Nacionality();
        partialUpdatedNacionality.setId(nacionality.getId());

        partialUpdatedNacionality.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restNacionalityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNacionality.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNacionality))
            )
            .andExpect(status().isOk());

        // Validate the Nacionality in the database
        List<Nacionality> nacionalityList = nacionalityRepository.findAll();
        assertThat(nacionalityList).hasSize(databaseSizeBeforeUpdate);
        Nacionality testNacionality = nacionalityList.get(nacionalityList.size() - 1);
        assertThat(testNacionality.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testNacionality.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateNacionalityWithPatch() throws Exception {
        // Initialize the database
        nacionalityRepository.saveAndFlush(nacionality);

        int databaseSizeBeforeUpdate = nacionalityRepository.findAll().size();

        // Update the nacionality using partial update
        Nacionality partialUpdatedNacionality = new Nacionality();
        partialUpdatedNacionality.setId(nacionality.getId());

        partialUpdatedNacionality.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restNacionalityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNacionality.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNacionality))
            )
            .andExpect(status().isOk());

        // Validate the Nacionality in the database
        List<Nacionality> nacionalityList = nacionalityRepository.findAll();
        assertThat(nacionalityList).hasSize(databaseSizeBeforeUpdate);
        Nacionality testNacionality = nacionalityList.get(nacionalityList.size() - 1);
        assertThat(testNacionality.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testNacionality.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingNacionality() throws Exception {
        int databaseSizeBeforeUpdate = nacionalityRepository.findAll().size();
        nacionality.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNacionalityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nacionality.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nacionality))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nacionality in the database
        List<Nacionality> nacionalityList = nacionalityRepository.findAll();
        assertThat(nacionalityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNacionality() throws Exception {
        int databaseSizeBeforeUpdate = nacionalityRepository.findAll().size();
        nacionality.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNacionalityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nacionality))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nacionality in the database
        List<Nacionality> nacionalityList = nacionalityRepository.findAll();
        assertThat(nacionalityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNacionality() throws Exception {
        int databaseSizeBeforeUpdate = nacionalityRepository.findAll().size();
        nacionality.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNacionalityMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(nacionality))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Nacionality in the database
        List<Nacionality> nacionalityList = nacionalityRepository.findAll();
        assertThat(nacionalityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNacionality() throws Exception {
        // Initialize the database
        nacionalityRepository.saveAndFlush(nacionality);

        int databaseSizeBeforeDelete = nacionalityRepository.findAll().size();

        // Delete the nacionality
        restNacionalityMockMvc
            .perform(delete(ENTITY_API_URL_ID, nacionality.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Nacionality> nacionalityList = nacionalityRepository.findAll();
        assertThat(nacionalityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
