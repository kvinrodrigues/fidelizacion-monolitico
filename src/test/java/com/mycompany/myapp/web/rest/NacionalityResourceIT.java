package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Nacionality;
import com.mycompany.myapp.repository.NacionalityRepository;
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
