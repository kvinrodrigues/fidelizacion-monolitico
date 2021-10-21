package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.BagOfPoint;
import com.mycompany.myapp.repository.BagOfPointRepository;
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
 * Integration tests for the {@link BagOfPointResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BagOfPointResourceIT {

    private static final Instant DEFAULT_ASIGNATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ASIGNATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXPIRATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_ASSIGNED_SCORE = 1L;
    private static final Long UPDATED_ASSIGNED_SCORE = 2L;

    private static final Long DEFAULT_SCORE_USED = 1L;
    private static final Long UPDATED_SCORE_USED = 2L;

    private static final Long DEFAULT_SCORE_BALANCE = 1L;
    private static final Long UPDATED_SCORE_BALANCE = 2L;

    private static final Float DEFAULT_OPERATION_AMOUNT = 1F;
    private static final Float UPDATED_OPERATION_AMOUNT = 2F;

    private static final String ENTITY_API_URL = "/api/bag-of-points";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BagOfPointRepository bagOfPointRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBagOfPointMockMvc;

    private BagOfPoint bagOfPoint;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BagOfPoint createEntity(EntityManager em) {
        BagOfPoint bagOfPoint = new BagOfPoint()
            .asignationDate(DEFAULT_ASIGNATION_DATE)
            .expirationDate(DEFAULT_EXPIRATION_DATE)
            .assignedScore(DEFAULT_ASSIGNED_SCORE)
            .scoreUsed(DEFAULT_SCORE_USED)
            .scoreBalance(DEFAULT_SCORE_BALANCE)
            .operationAmount(DEFAULT_OPERATION_AMOUNT);
        return bagOfPoint;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BagOfPoint createUpdatedEntity(EntityManager em) {
        BagOfPoint bagOfPoint = new BagOfPoint()
            .asignationDate(UPDATED_ASIGNATION_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .assignedScore(UPDATED_ASSIGNED_SCORE)
            .scoreUsed(UPDATED_SCORE_USED)
            .scoreBalance(UPDATED_SCORE_BALANCE)
            .operationAmount(UPDATED_OPERATION_AMOUNT);
        return bagOfPoint;
    }

    @BeforeEach
    public void initTest() {
        bagOfPoint = createEntity(em);
    }

    @Test
    @Transactional
    void createBagOfPoint() throws Exception {
        int databaseSizeBeforeCreate = bagOfPointRepository.findAll().size();
        // Create the BagOfPoint
        restBagOfPointMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bagOfPoint)))
            .andExpect(status().isCreated());

        // Validate the BagOfPoint in the database
        List<BagOfPoint> bagOfPointList = bagOfPointRepository.findAll();
        assertThat(bagOfPointList).hasSize(databaseSizeBeforeCreate + 1);
        BagOfPoint testBagOfPoint = bagOfPointList.get(bagOfPointList.size() - 1);
        assertThat(testBagOfPoint.getAsignationDate()).isEqualTo(DEFAULT_ASIGNATION_DATE);
        assertThat(testBagOfPoint.getExpirationDate()).isEqualTo(DEFAULT_EXPIRATION_DATE);
        assertThat(testBagOfPoint.getAssignedScore()).isEqualTo(DEFAULT_ASSIGNED_SCORE);
        assertThat(testBagOfPoint.getScoreUsed()).isEqualTo(DEFAULT_SCORE_USED);
        assertThat(testBagOfPoint.getScoreBalance()).isEqualTo(DEFAULT_SCORE_BALANCE);
        assertThat(testBagOfPoint.getOperationAmount()).isEqualTo(DEFAULT_OPERATION_AMOUNT);
    }

    @Test
    @Transactional
    void createBagOfPointWithExistingId() throws Exception {
        // Create the BagOfPoint with an existing ID
        bagOfPoint.setId(1L);

        int databaseSizeBeforeCreate = bagOfPointRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBagOfPointMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bagOfPoint)))
            .andExpect(status().isBadRequest());

        // Validate the BagOfPoint in the database
        List<BagOfPoint> bagOfPointList = bagOfPointRepository.findAll();
        assertThat(bagOfPointList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAsignationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = bagOfPointRepository.findAll().size();
        // set the field null
        bagOfPoint.setAsignationDate(null);

        // Create the BagOfPoint, which fails.

        restBagOfPointMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bagOfPoint)))
            .andExpect(status().isBadRequest());

        List<BagOfPoint> bagOfPointList = bagOfPointRepository.findAll();
        assertThat(bagOfPointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExpirationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = bagOfPointRepository.findAll().size();
        // set the field null
        bagOfPoint.setExpirationDate(null);

        // Create the BagOfPoint, which fails.

        restBagOfPointMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bagOfPoint)))
            .andExpect(status().isBadRequest());

        List<BagOfPoint> bagOfPointList = bagOfPointRepository.findAll();
        assertThat(bagOfPointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAssignedScoreIsRequired() throws Exception {
        int databaseSizeBeforeTest = bagOfPointRepository.findAll().size();
        // set the field null
        bagOfPoint.setAssignedScore(null);

        // Create the BagOfPoint, which fails.

        restBagOfPointMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bagOfPoint)))
            .andExpect(status().isBadRequest());

        List<BagOfPoint> bagOfPointList = bagOfPointRepository.findAll();
        assertThat(bagOfPointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkScoreUsedIsRequired() throws Exception {
        int databaseSizeBeforeTest = bagOfPointRepository.findAll().size();
        // set the field null
        bagOfPoint.setScoreUsed(null);

        // Create the BagOfPoint, which fails.

        restBagOfPointMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bagOfPoint)))
            .andExpect(status().isBadRequest());

        List<BagOfPoint> bagOfPointList = bagOfPointRepository.findAll();
        assertThat(bagOfPointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkScoreBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = bagOfPointRepository.findAll().size();
        // set the field null
        bagOfPoint.setScoreBalance(null);

        // Create the BagOfPoint, which fails.

        restBagOfPointMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bagOfPoint)))
            .andExpect(status().isBadRequest());

        List<BagOfPoint> bagOfPointList = bagOfPointRepository.findAll();
        assertThat(bagOfPointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOperationAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = bagOfPointRepository.findAll().size();
        // set the field null
        bagOfPoint.setOperationAmount(null);

        // Create the BagOfPoint, which fails.

        restBagOfPointMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bagOfPoint)))
            .andExpect(status().isBadRequest());

        List<BagOfPoint> bagOfPointList = bagOfPointRepository.findAll();
        assertThat(bagOfPointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBagOfPoints() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        // Get all the bagOfPointList
        restBagOfPointMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bagOfPoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].asignationDate").value(hasItem(DEFAULT_ASIGNATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].assignedScore").value(hasItem(DEFAULT_ASSIGNED_SCORE.intValue())))
            .andExpect(jsonPath("$.[*].scoreUsed").value(hasItem(DEFAULT_SCORE_USED.intValue())))
            .andExpect(jsonPath("$.[*].scoreBalance").value(hasItem(DEFAULT_SCORE_BALANCE.intValue())))
            .andExpect(jsonPath("$.[*].operationAmount").value(hasItem(DEFAULT_OPERATION_AMOUNT.doubleValue())));
    }

    @Test
    @Transactional
    void getBagOfPoint() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        // Get the bagOfPoint
        restBagOfPointMockMvc
            .perform(get(ENTITY_API_URL_ID, bagOfPoint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bagOfPoint.getId().intValue()))
            .andExpect(jsonPath("$.asignationDate").value(DEFAULT_ASIGNATION_DATE.toString()))
            .andExpect(jsonPath("$.expirationDate").value(DEFAULT_EXPIRATION_DATE.toString()))
            .andExpect(jsonPath("$.assignedScore").value(DEFAULT_ASSIGNED_SCORE.intValue()))
            .andExpect(jsonPath("$.scoreUsed").value(DEFAULT_SCORE_USED.intValue()))
            .andExpect(jsonPath("$.scoreBalance").value(DEFAULT_SCORE_BALANCE.intValue()))
            .andExpect(jsonPath("$.operationAmount").value(DEFAULT_OPERATION_AMOUNT.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingBagOfPoint() throws Exception {
        // Get the bagOfPoint
        restBagOfPointMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBagOfPoint() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        int databaseSizeBeforeUpdate = bagOfPointRepository.findAll().size();

        // Update the bagOfPoint
        BagOfPoint updatedBagOfPoint = bagOfPointRepository.findById(bagOfPoint.getId()).get();
        // Disconnect from session so that the updates on updatedBagOfPoint are not directly saved in db
        em.detach(updatedBagOfPoint);
        updatedBagOfPoint
            .asignationDate(UPDATED_ASIGNATION_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .assignedScore(UPDATED_ASSIGNED_SCORE)
            .scoreUsed(UPDATED_SCORE_USED)
            .scoreBalance(UPDATED_SCORE_BALANCE)
            .operationAmount(UPDATED_OPERATION_AMOUNT);

        restBagOfPointMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBagOfPoint.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBagOfPoint))
            )
            .andExpect(status().isOk());

        // Validate the BagOfPoint in the database
        List<BagOfPoint> bagOfPointList = bagOfPointRepository.findAll();
        assertThat(bagOfPointList).hasSize(databaseSizeBeforeUpdate);
        BagOfPoint testBagOfPoint = bagOfPointList.get(bagOfPointList.size() - 1);
        assertThat(testBagOfPoint.getAsignationDate()).isEqualTo(UPDATED_ASIGNATION_DATE);
        assertThat(testBagOfPoint.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testBagOfPoint.getAssignedScore()).isEqualTo(UPDATED_ASSIGNED_SCORE);
        assertThat(testBagOfPoint.getScoreUsed()).isEqualTo(UPDATED_SCORE_USED);
        assertThat(testBagOfPoint.getScoreBalance()).isEqualTo(UPDATED_SCORE_BALANCE);
        assertThat(testBagOfPoint.getOperationAmount()).isEqualTo(UPDATED_OPERATION_AMOUNT);
    }

    @Test
    @Transactional
    void putNonExistingBagOfPoint() throws Exception {
        int databaseSizeBeforeUpdate = bagOfPointRepository.findAll().size();
        bagOfPoint.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBagOfPointMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bagOfPoint.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bagOfPoint))
            )
            .andExpect(status().isBadRequest());

        // Validate the BagOfPoint in the database
        List<BagOfPoint> bagOfPointList = bagOfPointRepository.findAll();
        assertThat(bagOfPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBagOfPoint() throws Exception {
        int databaseSizeBeforeUpdate = bagOfPointRepository.findAll().size();
        bagOfPoint.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBagOfPointMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bagOfPoint))
            )
            .andExpect(status().isBadRequest());

        // Validate the BagOfPoint in the database
        List<BagOfPoint> bagOfPointList = bagOfPointRepository.findAll();
        assertThat(bagOfPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBagOfPoint() throws Exception {
        int databaseSizeBeforeUpdate = bagOfPointRepository.findAll().size();
        bagOfPoint.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBagOfPointMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bagOfPoint)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BagOfPoint in the database
        List<BagOfPoint> bagOfPointList = bagOfPointRepository.findAll();
        assertThat(bagOfPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBagOfPointWithPatch() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        int databaseSizeBeforeUpdate = bagOfPointRepository.findAll().size();

        // Update the bagOfPoint using partial update
        BagOfPoint partialUpdatedBagOfPoint = new BagOfPoint();
        partialUpdatedBagOfPoint.setId(bagOfPoint.getId());

        partialUpdatedBagOfPoint
            .asignationDate(UPDATED_ASIGNATION_DATE)
            .scoreUsed(UPDATED_SCORE_USED)
            .operationAmount(UPDATED_OPERATION_AMOUNT);

        restBagOfPointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBagOfPoint.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBagOfPoint))
            )
            .andExpect(status().isOk());

        // Validate the BagOfPoint in the database
        List<BagOfPoint> bagOfPointList = bagOfPointRepository.findAll();
        assertThat(bagOfPointList).hasSize(databaseSizeBeforeUpdate);
        BagOfPoint testBagOfPoint = bagOfPointList.get(bagOfPointList.size() - 1);
        assertThat(testBagOfPoint.getAsignationDate()).isEqualTo(UPDATED_ASIGNATION_DATE);
        assertThat(testBagOfPoint.getExpirationDate()).isEqualTo(DEFAULT_EXPIRATION_DATE);
        assertThat(testBagOfPoint.getAssignedScore()).isEqualTo(DEFAULT_ASSIGNED_SCORE);
        assertThat(testBagOfPoint.getScoreUsed()).isEqualTo(UPDATED_SCORE_USED);
        assertThat(testBagOfPoint.getScoreBalance()).isEqualTo(DEFAULT_SCORE_BALANCE);
        assertThat(testBagOfPoint.getOperationAmount()).isEqualTo(UPDATED_OPERATION_AMOUNT);
    }

    @Test
    @Transactional
    void fullUpdateBagOfPointWithPatch() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        int databaseSizeBeforeUpdate = bagOfPointRepository.findAll().size();

        // Update the bagOfPoint using partial update
        BagOfPoint partialUpdatedBagOfPoint = new BagOfPoint();
        partialUpdatedBagOfPoint.setId(bagOfPoint.getId());

        partialUpdatedBagOfPoint
            .asignationDate(UPDATED_ASIGNATION_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .assignedScore(UPDATED_ASSIGNED_SCORE)
            .scoreUsed(UPDATED_SCORE_USED)
            .scoreBalance(UPDATED_SCORE_BALANCE)
            .operationAmount(UPDATED_OPERATION_AMOUNT);

        restBagOfPointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBagOfPoint.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBagOfPoint))
            )
            .andExpect(status().isOk());

        // Validate the BagOfPoint in the database
        List<BagOfPoint> bagOfPointList = bagOfPointRepository.findAll();
        assertThat(bagOfPointList).hasSize(databaseSizeBeforeUpdate);
        BagOfPoint testBagOfPoint = bagOfPointList.get(bagOfPointList.size() - 1);
        assertThat(testBagOfPoint.getAsignationDate()).isEqualTo(UPDATED_ASIGNATION_DATE);
        assertThat(testBagOfPoint.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testBagOfPoint.getAssignedScore()).isEqualTo(UPDATED_ASSIGNED_SCORE);
        assertThat(testBagOfPoint.getScoreUsed()).isEqualTo(UPDATED_SCORE_USED);
        assertThat(testBagOfPoint.getScoreBalance()).isEqualTo(UPDATED_SCORE_BALANCE);
        assertThat(testBagOfPoint.getOperationAmount()).isEqualTo(UPDATED_OPERATION_AMOUNT);
    }

    @Test
    @Transactional
    void patchNonExistingBagOfPoint() throws Exception {
        int databaseSizeBeforeUpdate = bagOfPointRepository.findAll().size();
        bagOfPoint.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBagOfPointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bagOfPoint.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bagOfPoint))
            )
            .andExpect(status().isBadRequest());

        // Validate the BagOfPoint in the database
        List<BagOfPoint> bagOfPointList = bagOfPointRepository.findAll();
        assertThat(bagOfPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBagOfPoint() throws Exception {
        int databaseSizeBeforeUpdate = bagOfPointRepository.findAll().size();
        bagOfPoint.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBagOfPointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bagOfPoint))
            )
            .andExpect(status().isBadRequest());

        // Validate the BagOfPoint in the database
        List<BagOfPoint> bagOfPointList = bagOfPointRepository.findAll();
        assertThat(bagOfPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBagOfPoint() throws Exception {
        int databaseSizeBeforeUpdate = bagOfPointRepository.findAll().size();
        bagOfPoint.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBagOfPointMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(bagOfPoint))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BagOfPoint in the database
        List<BagOfPoint> bagOfPointList = bagOfPointRepository.findAll();
        assertThat(bagOfPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBagOfPoint() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        int databaseSizeBeforeDelete = bagOfPointRepository.findAll().size();

        // Delete the bagOfPoint
        restBagOfPointMockMvc
            .perform(delete(ENTITY_API_URL_ID, bagOfPoint.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BagOfPoint> bagOfPointList = bagOfPointRepository.findAll();
        assertThat(bagOfPointList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
