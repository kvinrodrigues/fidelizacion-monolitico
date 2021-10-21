package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ExpirationPoint;
import com.mycompany.myapp.repository.ExpirationPointRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link ExpirationPointResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExpirationPointResourceIT {

    private static final Instant DEFAULT_VALIDITY_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VALIDITY_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_VALIDITY_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VALIDITY_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_SCORE_DURATION_DAYS = 1L;
    private static final Long UPDATED_SCORE_DURATION_DAYS = 2L;

    private static final String ENTITY_API_URL = "/api/expiration-points";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExpirationPointRepository expirationPointRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExpirationPointMockMvc;

    private ExpirationPoint expirationPoint;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExpirationPoint createEntity(EntityManager em) {
        ExpirationPoint expirationPoint = new ExpirationPoint()
            .validityStartDate(DEFAULT_VALIDITY_START_DATE)
            .validityEndDate(DEFAULT_VALIDITY_END_DATE)
            .scoreDurationDays(DEFAULT_SCORE_DURATION_DAYS);
        return expirationPoint;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExpirationPoint createUpdatedEntity(EntityManager em) {
        ExpirationPoint expirationPoint = new ExpirationPoint()
            .validityStartDate(UPDATED_VALIDITY_START_DATE)
            .validityEndDate(UPDATED_VALIDITY_END_DATE)
            .scoreDurationDays(UPDATED_SCORE_DURATION_DAYS);
        return expirationPoint;
    }

    @BeforeEach
    public void initTest() {
        expirationPoint = createEntity(em);
    }

    @Test
    @Transactional
    void createExpirationPoint() throws Exception {
        int databaseSizeBeforeCreate = expirationPointRepository.findAll().size();
        // Create the ExpirationPoint
        restExpirationPointMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(expirationPoint))
            )
            .andExpect(status().isCreated());

        // Validate the ExpirationPoint in the database
        List<ExpirationPoint> expirationPointList = expirationPointRepository.findAll();
        assertThat(expirationPointList).hasSize(databaseSizeBeforeCreate + 1);
        ExpirationPoint testExpirationPoint = expirationPointList.get(expirationPointList.size() - 1);
        assertThat(testExpirationPoint.getValidityStartDate()).isEqualTo(DEFAULT_VALIDITY_START_DATE);
        assertThat(testExpirationPoint.getValidityEndDate()).isEqualTo(DEFAULT_VALIDITY_END_DATE);
        assertThat(testExpirationPoint.getScoreDurationDays()).isEqualTo(DEFAULT_SCORE_DURATION_DAYS);
    }

    @Test
    @Transactional
    void createExpirationPointWithExistingId() throws Exception {
        // Create the ExpirationPoint with an existing ID
        expirationPoint.setId(1L);

        int databaseSizeBeforeCreate = expirationPointRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExpirationPointMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(expirationPoint))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpirationPoint in the database
        List<ExpirationPoint> expirationPointList = expirationPointRepository.findAll();
        assertThat(expirationPointList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkValidityStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = expirationPointRepository.findAll().size();
        // set the field null
        expirationPoint.setValidityStartDate(null);

        // Create the ExpirationPoint, which fails.

        restExpirationPointMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(expirationPoint))
            )
            .andExpect(status().isBadRequest());

        List<ExpirationPoint> expirationPointList = expirationPointRepository.findAll();
        assertThat(expirationPointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValidityEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = expirationPointRepository.findAll().size();
        // set the field null
        expirationPoint.setValidityEndDate(null);

        // Create the ExpirationPoint, which fails.

        restExpirationPointMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(expirationPoint))
            )
            .andExpect(status().isBadRequest());

        List<ExpirationPoint> expirationPointList = expirationPointRepository.findAll();
        assertThat(expirationPointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkScoreDurationDaysIsRequired() throws Exception {
        int databaseSizeBeforeTest = expirationPointRepository.findAll().size();
        // set the field null
        expirationPoint.setScoreDurationDays(null);

        // Create the ExpirationPoint, which fails.

        restExpirationPointMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(expirationPoint))
            )
            .andExpect(status().isBadRequest());

        List<ExpirationPoint> expirationPointList = expirationPointRepository.findAll();
        assertThat(expirationPointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExpirationPoints() throws Exception {
        // Initialize the database
        expirationPointRepository.saveAndFlush(expirationPoint);

        // Get all the expirationPointList
        restExpirationPointMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expirationPoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].validityStartDate").value(hasItem(DEFAULT_VALIDITY_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].validityEndDate").value(hasItem(DEFAULT_VALIDITY_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].scoreDurationDays").value(hasItem(DEFAULT_SCORE_DURATION_DAYS.intValue())));
    }

    @Test
    @Transactional
    void getExpirationPoint() throws Exception {
        // Initialize the database
        expirationPointRepository.saveAndFlush(expirationPoint);

        // Get the expirationPoint
        restExpirationPointMockMvc
            .perform(get(ENTITY_API_URL_ID, expirationPoint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(expirationPoint.getId().intValue()))
            .andExpect(jsonPath("$.validityStartDate").value(DEFAULT_VALIDITY_START_DATE.toString()))
            .andExpect(jsonPath("$.validityEndDate").value(DEFAULT_VALIDITY_END_DATE.toString()))
            .andExpect(jsonPath("$.scoreDurationDays").value(DEFAULT_SCORE_DURATION_DAYS.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingExpirationPoint() throws Exception {
        // Get the expirationPoint
        restExpirationPointMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewExpirationPoint() throws Exception {
        // Initialize the database
        expirationPointRepository.saveAndFlush(expirationPoint);

        int databaseSizeBeforeUpdate = expirationPointRepository.findAll().size();

        // Update the expirationPoint
        ExpirationPoint updatedExpirationPoint = expirationPointRepository.findById(expirationPoint.getId()).get();
        // Disconnect from session so that the updates on updatedExpirationPoint are not directly saved in db
        em.detach(updatedExpirationPoint);
        updatedExpirationPoint
            .validityStartDate(UPDATED_VALIDITY_START_DATE)
            .validityEndDate(UPDATED_VALIDITY_END_DATE)
            .scoreDurationDays(UPDATED_SCORE_DURATION_DAYS);

        restExpirationPointMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExpirationPoint.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedExpirationPoint))
            )
            .andExpect(status().isOk());

        // Validate the ExpirationPoint in the database
        List<ExpirationPoint> expirationPointList = expirationPointRepository.findAll();
        assertThat(expirationPointList).hasSize(databaseSizeBeforeUpdate);
        ExpirationPoint testExpirationPoint = expirationPointList.get(expirationPointList.size() - 1);
        assertThat(testExpirationPoint.getValidityStartDate()).isEqualTo(UPDATED_VALIDITY_START_DATE);
        assertThat(testExpirationPoint.getValidityEndDate()).isEqualTo(UPDATED_VALIDITY_END_DATE);
        assertThat(testExpirationPoint.getScoreDurationDays()).isEqualTo(UPDATED_SCORE_DURATION_DAYS);
    }

    @Test
    @Transactional
    void putNonExistingExpirationPoint() throws Exception {
        int databaseSizeBeforeUpdate = expirationPointRepository.findAll().size();
        expirationPoint.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpirationPointMockMvc
            .perform(
                put(ENTITY_API_URL_ID, expirationPoint.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(expirationPoint))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpirationPoint in the database
        List<ExpirationPoint> expirationPointList = expirationPointRepository.findAll();
        assertThat(expirationPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExpirationPoint() throws Exception {
        int databaseSizeBeforeUpdate = expirationPointRepository.findAll().size();
        expirationPoint.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpirationPointMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(expirationPoint))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpirationPoint in the database
        List<ExpirationPoint> expirationPointList = expirationPointRepository.findAll();
        assertThat(expirationPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExpirationPoint() throws Exception {
        int databaseSizeBeforeUpdate = expirationPointRepository.findAll().size();
        expirationPoint.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpirationPointMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(expirationPoint))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExpirationPoint in the database
        List<ExpirationPoint> expirationPointList = expirationPointRepository.findAll();
        assertThat(expirationPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExpirationPointWithPatch() throws Exception {
        // Initialize the database
        expirationPointRepository.saveAndFlush(expirationPoint);

        int databaseSizeBeforeUpdate = expirationPointRepository.findAll().size();

        // Update the expirationPoint using partial update
        ExpirationPoint partialUpdatedExpirationPoint = new ExpirationPoint();
        partialUpdatedExpirationPoint.setId(expirationPoint.getId());

        partialUpdatedExpirationPoint.validityStartDate(UPDATED_VALIDITY_START_DATE).validityEndDate(UPDATED_VALIDITY_END_DATE);

        restExpirationPointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExpirationPoint.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExpirationPoint))
            )
            .andExpect(status().isOk());

        // Validate the ExpirationPoint in the database
        List<ExpirationPoint> expirationPointList = expirationPointRepository.findAll();
        assertThat(expirationPointList).hasSize(databaseSizeBeforeUpdate);
        ExpirationPoint testExpirationPoint = expirationPointList.get(expirationPointList.size() - 1);
        assertThat(testExpirationPoint.getValidityStartDate()).isEqualTo(UPDATED_VALIDITY_START_DATE);
        assertThat(testExpirationPoint.getValidityEndDate()).isEqualTo(UPDATED_VALIDITY_END_DATE);
        assertThat(testExpirationPoint.getScoreDurationDays()).isEqualTo(DEFAULT_SCORE_DURATION_DAYS);
    }

    @Test
    @Transactional
    void fullUpdateExpirationPointWithPatch() throws Exception {
        // Initialize the database
        expirationPointRepository.saveAndFlush(expirationPoint);

        int databaseSizeBeforeUpdate = expirationPointRepository.findAll().size();

        // Update the expirationPoint using partial update
        ExpirationPoint partialUpdatedExpirationPoint = new ExpirationPoint();
        partialUpdatedExpirationPoint.setId(expirationPoint.getId());

        partialUpdatedExpirationPoint
            .validityStartDate(UPDATED_VALIDITY_START_DATE)
            .validityEndDate(UPDATED_VALIDITY_END_DATE)
            .scoreDurationDays(UPDATED_SCORE_DURATION_DAYS);

        restExpirationPointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExpirationPoint.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExpirationPoint))
            )
            .andExpect(status().isOk());

        // Validate the ExpirationPoint in the database
        List<ExpirationPoint> expirationPointList = expirationPointRepository.findAll();
        assertThat(expirationPointList).hasSize(databaseSizeBeforeUpdate);
        ExpirationPoint testExpirationPoint = expirationPointList.get(expirationPointList.size() - 1);
        assertThat(testExpirationPoint.getValidityStartDate()).isEqualTo(UPDATED_VALIDITY_START_DATE);
        assertThat(testExpirationPoint.getValidityEndDate()).isEqualTo(UPDATED_VALIDITY_END_DATE);
        assertThat(testExpirationPoint.getScoreDurationDays()).isEqualTo(UPDATED_SCORE_DURATION_DAYS);
    }

    @Test
    @Transactional
    void patchNonExistingExpirationPoint() throws Exception {
        int databaseSizeBeforeUpdate = expirationPointRepository.findAll().size();
        expirationPoint.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpirationPointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, expirationPoint.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(expirationPoint))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpirationPoint in the database
        List<ExpirationPoint> expirationPointList = expirationPointRepository.findAll();
        assertThat(expirationPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExpirationPoint() throws Exception {
        int databaseSizeBeforeUpdate = expirationPointRepository.findAll().size();
        expirationPoint.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpirationPointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(expirationPoint))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpirationPoint in the database
        List<ExpirationPoint> expirationPointList = expirationPointRepository.findAll();
        assertThat(expirationPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExpirationPoint() throws Exception {
        int databaseSizeBeforeUpdate = expirationPointRepository.findAll().size();
        expirationPoint.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpirationPointMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(expirationPoint))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExpirationPoint in the database
        List<ExpirationPoint> expirationPointList = expirationPointRepository.findAll();
        assertThat(expirationPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExpirationPoint() throws Exception {
        // Initialize the database
        expirationPointRepository.saveAndFlush(expirationPoint);

        int databaseSizeBeforeDelete = expirationPointRepository.findAll().size();

        // Delete the expirationPoint
        restExpirationPointMockMvc
            .perform(delete(ENTITY_API_URL_ID, expirationPoint.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExpirationPoint> expirationPointList = expirationPointRepository.findAll();
        assertThat(expirationPointList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
