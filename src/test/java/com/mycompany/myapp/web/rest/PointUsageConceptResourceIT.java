package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.PointUsageConcept;
import com.mycompany.myapp.domain.PointUse;
import com.mycompany.myapp.repository.PointUsageConceptRepository;
import com.mycompany.myapp.service.criteria.PointUsageConceptCriteria;
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
 * Integration tests for the {@link PointUsageConceptResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PointUsageConceptResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Long DEFAULT_REQUIRED_POINTS = 1L;
    private static final Long UPDATED_REQUIRED_POINTS = 2L;
    private static final Long SMALLER_REQUIRED_POINTS = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/point-usage-concepts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PointUsageConceptRepository pointUsageConceptRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPointUsageConceptMockMvc;

    private PointUsageConcept pointUsageConcept;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PointUsageConcept createEntity(EntityManager em) {
        PointUsageConcept pointUsageConcept = new PointUsageConcept()
            .description(DEFAULT_DESCRIPTION)
            .requiredPoints(DEFAULT_REQUIRED_POINTS);
        return pointUsageConcept;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PointUsageConcept createUpdatedEntity(EntityManager em) {
        PointUsageConcept pointUsageConcept = new PointUsageConcept()
            .description(UPDATED_DESCRIPTION)
            .requiredPoints(UPDATED_REQUIRED_POINTS);
        return pointUsageConcept;
    }

    @BeforeEach
    public void initTest() {
        pointUsageConcept = createEntity(em);
    }

    @Test
    @Transactional
    void createPointUsageConcept() throws Exception {
        int databaseSizeBeforeCreate = pointUsageConceptRepository.findAll().size();
        // Create the PointUsageConcept
        restPointUsageConceptMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pointUsageConcept))
            )
            .andExpect(status().isCreated());

        // Validate the PointUsageConcept in the database
        List<PointUsageConcept> pointUsageConceptList = pointUsageConceptRepository.findAll();
        assertThat(pointUsageConceptList).hasSize(databaseSizeBeforeCreate + 1);
        PointUsageConcept testPointUsageConcept = pointUsageConceptList.get(pointUsageConceptList.size() - 1);
        assertThat(testPointUsageConcept.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPointUsageConcept.getRequiredPoints()).isEqualTo(DEFAULT_REQUIRED_POINTS);
    }

    @Test
    @Transactional
    void createPointUsageConceptWithExistingId() throws Exception {
        // Create the PointUsageConcept with an existing ID
        pointUsageConcept.setId(1L);

        int databaseSizeBeforeCreate = pointUsageConceptRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPointUsageConceptMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pointUsageConcept))
            )
            .andExpect(status().isBadRequest());

        // Validate the PointUsageConcept in the database
        List<PointUsageConcept> pointUsageConceptList = pointUsageConceptRepository.findAll();
        assertThat(pointUsageConceptList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPointUsageConcepts() throws Exception {
        // Initialize the database
        pointUsageConceptRepository.saveAndFlush(pointUsageConcept);

        // Get all the pointUsageConceptList
        restPointUsageConceptMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pointUsageConcept.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].requiredPoints").value(hasItem(DEFAULT_REQUIRED_POINTS.intValue())));
    }

    @Test
    @Transactional
    void getPointUsageConcept() throws Exception {
        // Initialize the database
        pointUsageConceptRepository.saveAndFlush(pointUsageConcept);

        // Get the pointUsageConcept
        restPointUsageConceptMockMvc
            .perform(get(ENTITY_API_URL_ID, pointUsageConcept.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pointUsageConcept.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.requiredPoints").value(DEFAULT_REQUIRED_POINTS.intValue()));
    }

    @Test
    @Transactional
    void getPointUsageConceptsByIdFiltering() throws Exception {
        // Initialize the database
        pointUsageConceptRepository.saveAndFlush(pointUsageConcept);

        Long id = pointUsageConcept.getId();

        defaultPointUsageConceptShouldBeFound("id.equals=" + id);
        defaultPointUsageConceptShouldNotBeFound("id.notEquals=" + id);

        defaultPointUsageConceptShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPointUsageConceptShouldNotBeFound("id.greaterThan=" + id);

        defaultPointUsageConceptShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPointUsageConceptShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPointUsageConceptsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        pointUsageConceptRepository.saveAndFlush(pointUsageConcept);

        // Get all the pointUsageConceptList where description equals to DEFAULT_DESCRIPTION
        defaultPointUsageConceptShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the pointUsageConceptList where description equals to UPDATED_DESCRIPTION
        defaultPointUsageConceptShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPointUsageConceptsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pointUsageConceptRepository.saveAndFlush(pointUsageConcept);

        // Get all the pointUsageConceptList where description not equals to DEFAULT_DESCRIPTION
        defaultPointUsageConceptShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the pointUsageConceptList where description not equals to UPDATED_DESCRIPTION
        defaultPointUsageConceptShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPointUsageConceptsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        pointUsageConceptRepository.saveAndFlush(pointUsageConcept);

        // Get all the pointUsageConceptList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultPointUsageConceptShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the pointUsageConceptList where description equals to UPDATED_DESCRIPTION
        defaultPointUsageConceptShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPointUsageConceptsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        pointUsageConceptRepository.saveAndFlush(pointUsageConcept);

        // Get all the pointUsageConceptList where description is not null
        defaultPointUsageConceptShouldBeFound("description.specified=true");

        // Get all the pointUsageConceptList where description is null
        defaultPointUsageConceptShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllPointUsageConceptsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        pointUsageConceptRepository.saveAndFlush(pointUsageConcept);

        // Get all the pointUsageConceptList where description contains DEFAULT_DESCRIPTION
        defaultPointUsageConceptShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the pointUsageConceptList where description contains UPDATED_DESCRIPTION
        defaultPointUsageConceptShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPointUsageConceptsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        pointUsageConceptRepository.saveAndFlush(pointUsageConcept);

        // Get all the pointUsageConceptList where description does not contain DEFAULT_DESCRIPTION
        defaultPointUsageConceptShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the pointUsageConceptList where description does not contain UPDATED_DESCRIPTION
        defaultPointUsageConceptShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPointUsageConceptsByRequiredPointsIsEqualToSomething() throws Exception {
        // Initialize the database
        pointUsageConceptRepository.saveAndFlush(pointUsageConcept);

        // Get all the pointUsageConceptList where requiredPoints equals to DEFAULT_REQUIRED_POINTS
        defaultPointUsageConceptShouldBeFound("requiredPoints.equals=" + DEFAULT_REQUIRED_POINTS);

        // Get all the pointUsageConceptList where requiredPoints equals to UPDATED_REQUIRED_POINTS
        defaultPointUsageConceptShouldNotBeFound("requiredPoints.equals=" + UPDATED_REQUIRED_POINTS);
    }

    @Test
    @Transactional
    void getAllPointUsageConceptsByRequiredPointsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pointUsageConceptRepository.saveAndFlush(pointUsageConcept);

        // Get all the pointUsageConceptList where requiredPoints not equals to DEFAULT_REQUIRED_POINTS
        defaultPointUsageConceptShouldNotBeFound("requiredPoints.notEquals=" + DEFAULT_REQUIRED_POINTS);

        // Get all the pointUsageConceptList where requiredPoints not equals to UPDATED_REQUIRED_POINTS
        defaultPointUsageConceptShouldBeFound("requiredPoints.notEquals=" + UPDATED_REQUIRED_POINTS);
    }

    @Test
    @Transactional
    void getAllPointUsageConceptsByRequiredPointsIsInShouldWork() throws Exception {
        // Initialize the database
        pointUsageConceptRepository.saveAndFlush(pointUsageConcept);

        // Get all the pointUsageConceptList where requiredPoints in DEFAULT_REQUIRED_POINTS or UPDATED_REQUIRED_POINTS
        defaultPointUsageConceptShouldBeFound("requiredPoints.in=" + DEFAULT_REQUIRED_POINTS + "," + UPDATED_REQUIRED_POINTS);

        // Get all the pointUsageConceptList where requiredPoints equals to UPDATED_REQUIRED_POINTS
        defaultPointUsageConceptShouldNotBeFound("requiredPoints.in=" + UPDATED_REQUIRED_POINTS);
    }

    @Test
    @Transactional
    void getAllPointUsageConceptsByRequiredPointsIsNullOrNotNull() throws Exception {
        // Initialize the database
        pointUsageConceptRepository.saveAndFlush(pointUsageConcept);

        // Get all the pointUsageConceptList where requiredPoints is not null
        defaultPointUsageConceptShouldBeFound("requiredPoints.specified=true");

        // Get all the pointUsageConceptList where requiredPoints is null
        defaultPointUsageConceptShouldNotBeFound("requiredPoints.specified=false");
    }

    @Test
    @Transactional
    void getAllPointUsageConceptsByRequiredPointsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pointUsageConceptRepository.saveAndFlush(pointUsageConcept);

        // Get all the pointUsageConceptList where requiredPoints is greater than or equal to DEFAULT_REQUIRED_POINTS
        defaultPointUsageConceptShouldBeFound("requiredPoints.greaterThanOrEqual=" + DEFAULT_REQUIRED_POINTS);

        // Get all the pointUsageConceptList where requiredPoints is greater than or equal to UPDATED_REQUIRED_POINTS
        defaultPointUsageConceptShouldNotBeFound("requiredPoints.greaterThanOrEqual=" + UPDATED_REQUIRED_POINTS);
    }

    @Test
    @Transactional
    void getAllPointUsageConceptsByRequiredPointsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pointUsageConceptRepository.saveAndFlush(pointUsageConcept);

        // Get all the pointUsageConceptList where requiredPoints is less than or equal to DEFAULT_REQUIRED_POINTS
        defaultPointUsageConceptShouldBeFound("requiredPoints.lessThanOrEqual=" + DEFAULT_REQUIRED_POINTS);

        // Get all the pointUsageConceptList where requiredPoints is less than or equal to SMALLER_REQUIRED_POINTS
        defaultPointUsageConceptShouldNotBeFound("requiredPoints.lessThanOrEqual=" + SMALLER_REQUIRED_POINTS);
    }

    @Test
    @Transactional
    void getAllPointUsageConceptsByRequiredPointsIsLessThanSomething() throws Exception {
        // Initialize the database
        pointUsageConceptRepository.saveAndFlush(pointUsageConcept);

        // Get all the pointUsageConceptList where requiredPoints is less than DEFAULT_REQUIRED_POINTS
        defaultPointUsageConceptShouldNotBeFound("requiredPoints.lessThan=" + DEFAULT_REQUIRED_POINTS);

        // Get all the pointUsageConceptList where requiredPoints is less than UPDATED_REQUIRED_POINTS
        defaultPointUsageConceptShouldBeFound("requiredPoints.lessThan=" + UPDATED_REQUIRED_POINTS);
    }

    @Test
    @Transactional
    void getAllPointUsageConceptsByRequiredPointsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pointUsageConceptRepository.saveAndFlush(pointUsageConcept);

        // Get all the pointUsageConceptList where requiredPoints is greater than DEFAULT_REQUIRED_POINTS
        defaultPointUsageConceptShouldNotBeFound("requiredPoints.greaterThan=" + DEFAULT_REQUIRED_POINTS);

        // Get all the pointUsageConceptList where requiredPoints is greater than SMALLER_REQUIRED_POINTS
        defaultPointUsageConceptShouldBeFound("requiredPoints.greaterThan=" + SMALLER_REQUIRED_POINTS);
    }

    @Test
    @Transactional
    void getAllPointUsageConceptsByPointUseIsEqualToSomething() throws Exception {
        // Initialize the database
        pointUsageConceptRepository.saveAndFlush(pointUsageConcept);
        PointUse pointUse;
        if (TestUtil.findAll(em, PointUse.class).isEmpty()) {
            pointUse = PointUseResourceIT.createEntity(em);
            em.persist(pointUse);
            em.flush();
        } else {
            pointUse = TestUtil.findAll(em, PointUse.class).get(0);
        }
        em.persist(pointUse);
        em.flush();
        pointUsageConcept.addPointUse(pointUse);
        pointUsageConceptRepository.saveAndFlush(pointUsageConcept);
        Long pointUseId = pointUse.getId();

        // Get all the pointUsageConceptList where pointUse equals to pointUseId
        defaultPointUsageConceptShouldBeFound("pointUseId.equals=" + pointUseId);

        // Get all the pointUsageConceptList where pointUse equals to (pointUseId + 1)
        defaultPointUsageConceptShouldNotBeFound("pointUseId.equals=" + (pointUseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPointUsageConceptShouldBeFound(String filter) throws Exception {
        restPointUsageConceptMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pointUsageConcept.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].requiredPoints").value(hasItem(DEFAULT_REQUIRED_POINTS.intValue())));

        // Check, that the count call also returns 1
        restPointUsageConceptMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPointUsageConceptShouldNotBeFound(String filter) throws Exception {
        restPointUsageConceptMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPointUsageConceptMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPointUsageConcept() throws Exception {
        // Get the pointUsageConcept
        restPointUsageConceptMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPointUsageConcept() throws Exception {
        // Initialize the database
        pointUsageConceptRepository.saveAndFlush(pointUsageConcept);

        int databaseSizeBeforeUpdate = pointUsageConceptRepository.findAll().size();

        // Update the pointUsageConcept
        PointUsageConcept updatedPointUsageConcept = pointUsageConceptRepository.findById(pointUsageConcept.getId()).get();
        // Disconnect from session so that the updates on updatedPointUsageConcept are not directly saved in db
        em.detach(updatedPointUsageConcept);
        updatedPointUsageConcept.description(UPDATED_DESCRIPTION).requiredPoints(UPDATED_REQUIRED_POINTS);

        restPointUsageConceptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPointUsageConcept.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPointUsageConcept))
            )
            .andExpect(status().isOk());

        // Validate the PointUsageConcept in the database
        List<PointUsageConcept> pointUsageConceptList = pointUsageConceptRepository.findAll();
        assertThat(pointUsageConceptList).hasSize(databaseSizeBeforeUpdate);
        PointUsageConcept testPointUsageConcept = pointUsageConceptList.get(pointUsageConceptList.size() - 1);
        assertThat(testPointUsageConcept.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPointUsageConcept.getRequiredPoints()).isEqualTo(UPDATED_REQUIRED_POINTS);
    }

    @Test
    @Transactional
    void putNonExistingPointUsageConcept() throws Exception {
        int databaseSizeBeforeUpdate = pointUsageConceptRepository.findAll().size();
        pointUsageConcept.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPointUsageConceptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pointUsageConcept.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pointUsageConcept))
            )
            .andExpect(status().isBadRequest());

        // Validate the PointUsageConcept in the database
        List<PointUsageConcept> pointUsageConceptList = pointUsageConceptRepository.findAll();
        assertThat(pointUsageConceptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPointUsageConcept() throws Exception {
        int databaseSizeBeforeUpdate = pointUsageConceptRepository.findAll().size();
        pointUsageConcept.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointUsageConceptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pointUsageConcept))
            )
            .andExpect(status().isBadRequest());

        // Validate the PointUsageConcept in the database
        List<PointUsageConcept> pointUsageConceptList = pointUsageConceptRepository.findAll();
        assertThat(pointUsageConceptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPointUsageConcept() throws Exception {
        int databaseSizeBeforeUpdate = pointUsageConceptRepository.findAll().size();
        pointUsageConcept.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointUsageConceptMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pointUsageConcept))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PointUsageConcept in the database
        List<PointUsageConcept> pointUsageConceptList = pointUsageConceptRepository.findAll();
        assertThat(pointUsageConceptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePointUsageConceptWithPatch() throws Exception {
        // Initialize the database
        pointUsageConceptRepository.saveAndFlush(pointUsageConcept);

        int databaseSizeBeforeUpdate = pointUsageConceptRepository.findAll().size();

        // Update the pointUsageConcept using partial update
        PointUsageConcept partialUpdatedPointUsageConcept = new PointUsageConcept();
        partialUpdatedPointUsageConcept.setId(pointUsageConcept.getId());

        restPointUsageConceptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPointUsageConcept.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPointUsageConcept))
            )
            .andExpect(status().isOk());

        // Validate the PointUsageConcept in the database
        List<PointUsageConcept> pointUsageConceptList = pointUsageConceptRepository.findAll();
        assertThat(pointUsageConceptList).hasSize(databaseSizeBeforeUpdate);
        PointUsageConcept testPointUsageConcept = pointUsageConceptList.get(pointUsageConceptList.size() - 1);
        assertThat(testPointUsageConcept.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPointUsageConcept.getRequiredPoints()).isEqualTo(DEFAULT_REQUIRED_POINTS);
    }

    @Test
    @Transactional
    void fullUpdatePointUsageConceptWithPatch() throws Exception {
        // Initialize the database
        pointUsageConceptRepository.saveAndFlush(pointUsageConcept);

        int databaseSizeBeforeUpdate = pointUsageConceptRepository.findAll().size();

        // Update the pointUsageConcept using partial update
        PointUsageConcept partialUpdatedPointUsageConcept = new PointUsageConcept();
        partialUpdatedPointUsageConcept.setId(pointUsageConcept.getId());

        partialUpdatedPointUsageConcept.description(UPDATED_DESCRIPTION).requiredPoints(UPDATED_REQUIRED_POINTS);

        restPointUsageConceptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPointUsageConcept.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPointUsageConcept))
            )
            .andExpect(status().isOk());

        // Validate the PointUsageConcept in the database
        List<PointUsageConcept> pointUsageConceptList = pointUsageConceptRepository.findAll();
        assertThat(pointUsageConceptList).hasSize(databaseSizeBeforeUpdate);
        PointUsageConcept testPointUsageConcept = pointUsageConceptList.get(pointUsageConceptList.size() - 1);
        assertThat(testPointUsageConcept.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPointUsageConcept.getRequiredPoints()).isEqualTo(UPDATED_REQUIRED_POINTS);
    }

    @Test
    @Transactional
    void patchNonExistingPointUsageConcept() throws Exception {
        int databaseSizeBeforeUpdate = pointUsageConceptRepository.findAll().size();
        pointUsageConcept.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPointUsageConceptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pointUsageConcept.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pointUsageConcept))
            )
            .andExpect(status().isBadRequest());

        // Validate the PointUsageConcept in the database
        List<PointUsageConcept> pointUsageConceptList = pointUsageConceptRepository.findAll();
        assertThat(pointUsageConceptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPointUsageConcept() throws Exception {
        int databaseSizeBeforeUpdate = pointUsageConceptRepository.findAll().size();
        pointUsageConcept.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointUsageConceptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pointUsageConcept))
            )
            .andExpect(status().isBadRequest());

        // Validate the PointUsageConcept in the database
        List<PointUsageConcept> pointUsageConceptList = pointUsageConceptRepository.findAll();
        assertThat(pointUsageConceptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPointUsageConcept() throws Exception {
        int databaseSizeBeforeUpdate = pointUsageConceptRepository.findAll().size();
        pointUsageConcept.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointUsageConceptMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pointUsageConcept))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PointUsageConcept in the database
        List<PointUsageConcept> pointUsageConceptList = pointUsageConceptRepository.findAll();
        assertThat(pointUsageConceptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePointUsageConcept() throws Exception {
        // Initialize the database
        pointUsageConceptRepository.saveAndFlush(pointUsageConcept);

        int databaseSizeBeforeDelete = pointUsageConceptRepository.findAll().size();

        // Delete the pointUsageConcept
        restPointUsageConceptMockMvc
            .perform(delete(ENTITY_API_URL_ID, pointUsageConcept.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PointUsageConcept> pointUsageConceptList = pointUsageConceptRepository.findAll();
        assertThat(pointUsageConceptList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
