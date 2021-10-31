package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.BagOfPoint;
import com.mycompany.myapp.domain.Client;
import com.mycompany.myapp.domain.PointUseDet;
import com.mycompany.myapp.repository.BagOfPointRepository;
import com.mycompany.myapp.service.criteria.BagOfPointCriteria;
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
    private static final Long SMALLER_ASSIGNED_SCORE = 1L - 1L;

    private static final Long DEFAULT_SCORE_USED = 1L;
    private static final Long UPDATED_SCORE_USED = 2L;
    private static final Long SMALLER_SCORE_USED = 1L - 1L;

    private static final Long DEFAULT_SCORE_BALANCE = 1L;
    private static final Long UPDATED_SCORE_BALANCE = 2L;
    private static final Long SMALLER_SCORE_BALANCE = 1L - 1L;

    private static final Float DEFAULT_OPERATION_AMOUNT = 1F;
    private static final Float UPDATED_OPERATION_AMOUNT = 2F;
    private static final Float SMALLER_OPERATION_AMOUNT = 1F - 1F;

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
    void getBagOfPointsByIdFiltering() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        Long id = bagOfPoint.getId();

        defaultBagOfPointShouldBeFound("id.equals=" + id);
        defaultBagOfPointShouldNotBeFound("id.notEquals=" + id);

        defaultBagOfPointShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBagOfPointShouldNotBeFound("id.greaterThan=" + id);

        defaultBagOfPointShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBagOfPointShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBagOfPointsByAsignationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        // Get all the bagOfPointList where asignationDate equals to DEFAULT_ASIGNATION_DATE
        defaultBagOfPointShouldBeFound("asignationDate.equals=" + DEFAULT_ASIGNATION_DATE);

        // Get all the bagOfPointList where asignationDate equals to UPDATED_ASIGNATION_DATE
        defaultBagOfPointShouldNotBeFound("asignationDate.equals=" + UPDATED_ASIGNATION_DATE);
    }

    @Test
    @Transactional
    void getAllBagOfPointsByAsignationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        // Get all the bagOfPointList where asignationDate not equals to DEFAULT_ASIGNATION_DATE
        defaultBagOfPointShouldNotBeFound("asignationDate.notEquals=" + DEFAULT_ASIGNATION_DATE);

        // Get all the bagOfPointList where asignationDate not equals to UPDATED_ASIGNATION_DATE
        defaultBagOfPointShouldBeFound("asignationDate.notEquals=" + UPDATED_ASIGNATION_DATE);
    }

    @Test
    @Transactional
    void getAllBagOfPointsByAsignationDateIsInShouldWork() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        // Get all the bagOfPointList where asignationDate in DEFAULT_ASIGNATION_DATE or UPDATED_ASIGNATION_DATE
        defaultBagOfPointShouldBeFound("asignationDate.in=" + DEFAULT_ASIGNATION_DATE + "," + UPDATED_ASIGNATION_DATE);

        // Get all the bagOfPointList where asignationDate equals to UPDATED_ASIGNATION_DATE
        defaultBagOfPointShouldNotBeFound("asignationDate.in=" + UPDATED_ASIGNATION_DATE);
    }

    @Test
    @Transactional
    void getAllBagOfPointsByAsignationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        // Get all the bagOfPointList where asignationDate is not null
        defaultBagOfPointShouldBeFound("asignationDate.specified=true");

        // Get all the bagOfPointList where asignationDate is null
        defaultBagOfPointShouldNotBeFound("asignationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllBagOfPointsByExpirationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        // Get all the bagOfPointList where expirationDate equals to DEFAULT_EXPIRATION_DATE
        defaultBagOfPointShouldBeFound("expirationDate.equals=" + DEFAULT_EXPIRATION_DATE);

        // Get all the bagOfPointList where expirationDate equals to UPDATED_EXPIRATION_DATE
        defaultBagOfPointShouldNotBeFound("expirationDate.equals=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllBagOfPointsByExpirationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        // Get all the bagOfPointList where expirationDate not equals to DEFAULT_EXPIRATION_DATE
        defaultBagOfPointShouldNotBeFound("expirationDate.notEquals=" + DEFAULT_EXPIRATION_DATE);

        // Get all the bagOfPointList where expirationDate not equals to UPDATED_EXPIRATION_DATE
        defaultBagOfPointShouldBeFound("expirationDate.notEquals=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllBagOfPointsByExpirationDateIsInShouldWork() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        // Get all the bagOfPointList where expirationDate in DEFAULT_EXPIRATION_DATE or UPDATED_EXPIRATION_DATE
        defaultBagOfPointShouldBeFound("expirationDate.in=" + DEFAULT_EXPIRATION_DATE + "," + UPDATED_EXPIRATION_DATE);

        // Get all the bagOfPointList where expirationDate equals to UPDATED_EXPIRATION_DATE
        defaultBagOfPointShouldNotBeFound("expirationDate.in=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllBagOfPointsByExpirationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        // Get all the bagOfPointList where expirationDate is not null
        defaultBagOfPointShouldBeFound("expirationDate.specified=true");

        // Get all the bagOfPointList where expirationDate is null
        defaultBagOfPointShouldNotBeFound("expirationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllBagOfPointsByAssignedScoreIsEqualToSomething() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        // Get all the bagOfPointList where assignedScore equals to DEFAULT_ASSIGNED_SCORE
        defaultBagOfPointShouldBeFound("assignedScore.equals=" + DEFAULT_ASSIGNED_SCORE);

        // Get all the bagOfPointList where assignedScore equals to UPDATED_ASSIGNED_SCORE
        defaultBagOfPointShouldNotBeFound("assignedScore.equals=" + UPDATED_ASSIGNED_SCORE);
    }

    @Test
    @Transactional
    void getAllBagOfPointsByAssignedScoreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        // Get all the bagOfPointList where assignedScore not equals to DEFAULT_ASSIGNED_SCORE
        defaultBagOfPointShouldNotBeFound("assignedScore.notEquals=" + DEFAULT_ASSIGNED_SCORE);

        // Get all the bagOfPointList where assignedScore not equals to UPDATED_ASSIGNED_SCORE
        defaultBagOfPointShouldBeFound("assignedScore.notEquals=" + UPDATED_ASSIGNED_SCORE);
    }

    @Test
    @Transactional
    void getAllBagOfPointsByAssignedScoreIsInShouldWork() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        // Get all the bagOfPointList where assignedScore in DEFAULT_ASSIGNED_SCORE or UPDATED_ASSIGNED_SCORE
        defaultBagOfPointShouldBeFound("assignedScore.in=" + DEFAULT_ASSIGNED_SCORE + "," + UPDATED_ASSIGNED_SCORE);

        // Get all the bagOfPointList where assignedScore equals to UPDATED_ASSIGNED_SCORE
        defaultBagOfPointShouldNotBeFound("assignedScore.in=" + UPDATED_ASSIGNED_SCORE);
    }

    @Test
    @Transactional
    void getAllBagOfPointsByAssignedScoreIsNullOrNotNull() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        // Get all the bagOfPointList where assignedScore is not null
        defaultBagOfPointShouldBeFound("assignedScore.specified=true");

        // Get all the bagOfPointList where assignedScore is null
        defaultBagOfPointShouldNotBeFound("assignedScore.specified=false");
    }

    @Test
    @Transactional
    void getAllBagOfPointsByAssignedScoreIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        // Get all the bagOfPointList where assignedScore is greater than or equal to DEFAULT_ASSIGNED_SCORE
        defaultBagOfPointShouldBeFound("assignedScore.greaterThanOrEqual=" + DEFAULT_ASSIGNED_SCORE);

        // Get all the bagOfPointList where assignedScore is greater than or equal to UPDATED_ASSIGNED_SCORE
        defaultBagOfPointShouldNotBeFound("assignedScore.greaterThanOrEqual=" + UPDATED_ASSIGNED_SCORE);
    }

    @Test
    @Transactional
    void getAllBagOfPointsByAssignedScoreIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        // Get all the bagOfPointList where assignedScore is less than or equal to DEFAULT_ASSIGNED_SCORE
        defaultBagOfPointShouldBeFound("assignedScore.lessThanOrEqual=" + DEFAULT_ASSIGNED_SCORE);

        // Get all the bagOfPointList where assignedScore is less than or equal to SMALLER_ASSIGNED_SCORE
        defaultBagOfPointShouldNotBeFound("assignedScore.lessThanOrEqual=" + SMALLER_ASSIGNED_SCORE);
    }

    @Test
    @Transactional
    void getAllBagOfPointsByAssignedScoreIsLessThanSomething() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        // Get all the bagOfPointList where assignedScore is less than DEFAULT_ASSIGNED_SCORE
        defaultBagOfPointShouldNotBeFound("assignedScore.lessThan=" + DEFAULT_ASSIGNED_SCORE);

        // Get all the bagOfPointList where assignedScore is less than UPDATED_ASSIGNED_SCORE
        defaultBagOfPointShouldBeFound("assignedScore.lessThan=" + UPDATED_ASSIGNED_SCORE);
    }

    @Test
    @Transactional
    void getAllBagOfPointsByAssignedScoreIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        // Get all the bagOfPointList where assignedScore is greater than DEFAULT_ASSIGNED_SCORE
        defaultBagOfPointShouldNotBeFound("assignedScore.greaterThan=" + DEFAULT_ASSIGNED_SCORE);

        // Get all the bagOfPointList where assignedScore is greater than SMALLER_ASSIGNED_SCORE
        defaultBagOfPointShouldBeFound("assignedScore.greaterThan=" + SMALLER_ASSIGNED_SCORE);
    }

    @Test
    @Transactional
    void getAllBagOfPointsByScoreUsedIsEqualToSomething() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        // Get all the bagOfPointList where scoreUsed equals to DEFAULT_SCORE_USED
        defaultBagOfPointShouldBeFound("scoreUsed.equals=" + DEFAULT_SCORE_USED);

        // Get all the bagOfPointList where scoreUsed equals to UPDATED_SCORE_USED
        defaultBagOfPointShouldNotBeFound("scoreUsed.equals=" + UPDATED_SCORE_USED);
    }

    @Test
    @Transactional
    void getAllBagOfPointsByScoreUsedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        // Get all the bagOfPointList where scoreUsed not equals to DEFAULT_SCORE_USED
        defaultBagOfPointShouldNotBeFound("scoreUsed.notEquals=" + DEFAULT_SCORE_USED);

        // Get all the bagOfPointList where scoreUsed not equals to UPDATED_SCORE_USED
        defaultBagOfPointShouldBeFound("scoreUsed.notEquals=" + UPDATED_SCORE_USED);
    }

    @Test
    @Transactional
    void getAllBagOfPointsByScoreUsedIsInShouldWork() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        // Get all the bagOfPointList where scoreUsed in DEFAULT_SCORE_USED or UPDATED_SCORE_USED
        defaultBagOfPointShouldBeFound("scoreUsed.in=" + DEFAULT_SCORE_USED + "," + UPDATED_SCORE_USED);

        // Get all the bagOfPointList where scoreUsed equals to UPDATED_SCORE_USED
        defaultBagOfPointShouldNotBeFound("scoreUsed.in=" + UPDATED_SCORE_USED);
    }

    @Test
    @Transactional
    void getAllBagOfPointsByScoreUsedIsNullOrNotNull() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        // Get all the bagOfPointList where scoreUsed is not null
        defaultBagOfPointShouldBeFound("scoreUsed.specified=true");

        // Get all the bagOfPointList where scoreUsed is null
        defaultBagOfPointShouldNotBeFound("scoreUsed.specified=false");
    }

    @Test
    @Transactional
    void getAllBagOfPointsByScoreUsedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        // Get all the bagOfPointList where scoreUsed is greater than or equal to DEFAULT_SCORE_USED
        defaultBagOfPointShouldBeFound("scoreUsed.greaterThanOrEqual=" + DEFAULT_SCORE_USED);

        // Get all the bagOfPointList where scoreUsed is greater than or equal to UPDATED_SCORE_USED
        defaultBagOfPointShouldNotBeFound("scoreUsed.greaterThanOrEqual=" + UPDATED_SCORE_USED);
    }

    @Test
    @Transactional
    void getAllBagOfPointsByScoreUsedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        // Get all the bagOfPointList where scoreUsed is less than or equal to DEFAULT_SCORE_USED
        defaultBagOfPointShouldBeFound("scoreUsed.lessThanOrEqual=" + DEFAULT_SCORE_USED);

        // Get all the bagOfPointList where scoreUsed is less than or equal to SMALLER_SCORE_USED
        defaultBagOfPointShouldNotBeFound("scoreUsed.lessThanOrEqual=" + SMALLER_SCORE_USED);
    }

    @Test
    @Transactional
    void getAllBagOfPointsByScoreUsedIsLessThanSomething() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        // Get all the bagOfPointList where scoreUsed is less than DEFAULT_SCORE_USED
        defaultBagOfPointShouldNotBeFound("scoreUsed.lessThan=" + DEFAULT_SCORE_USED);

        // Get all the bagOfPointList where scoreUsed is less than UPDATED_SCORE_USED
        defaultBagOfPointShouldBeFound("scoreUsed.lessThan=" + UPDATED_SCORE_USED);
    }

    @Test
    @Transactional
    void getAllBagOfPointsByScoreUsedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        // Get all the bagOfPointList where scoreUsed is greater than DEFAULT_SCORE_USED
        defaultBagOfPointShouldNotBeFound("scoreUsed.greaterThan=" + DEFAULT_SCORE_USED);

        // Get all the bagOfPointList where scoreUsed is greater than SMALLER_SCORE_USED
        defaultBagOfPointShouldBeFound("scoreUsed.greaterThan=" + SMALLER_SCORE_USED);
    }

    @Test
    @Transactional
    void getAllBagOfPointsByScoreBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        // Get all the bagOfPointList where scoreBalance equals to DEFAULT_SCORE_BALANCE
        defaultBagOfPointShouldBeFound("scoreBalance.equals=" + DEFAULT_SCORE_BALANCE);

        // Get all the bagOfPointList where scoreBalance equals to UPDATED_SCORE_BALANCE
        defaultBagOfPointShouldNotBeFound("scoreBalance.equals=" + UPDATED_SCORE_BALANCE);
    }

    @Test
    @Transactional
    void getAllBagOfPointsByScoreBalanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        // Get all the bagOfPointList where scoreBalance not equals to DEFAULT_SCORE_BALANCE
        defaultBagOfPointShouldNotBeFound("scoreBalance.notEquals=" + DEFAULT_SCORE_BALANCE);

        // Get all the bagOfPointList where scoreBalance not equals to UPDATED_SCORE_BALANCE
        defaultBagOfPointShouldBeFound("scoreBalance.notEquals=" + UPDATED_SCORE_BALANCE);
    }

    @Test
    @Transactional
    void getAllBagOfPointsByScoreBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        // Get all the bagOfPointList where scoreBalance in DEFAULT_SCORE_BALANCE or UPDATED_SCORE_BALANCE
        defaultBagOfPointShouldBeFound("scoreBalance.in=" + DEFAULT_SCORE_BALANCE + "," + UPDATED_SCORE_BALANCE);

        // Get all the bagOfPointList where scoreBalance equals to UPDATED_SCORE_BALANCE
        defaultBagOfPointShouldNotBeFound("scoreBalance.in=" + UPDATED_SCORE_BALANCE);
    }

    @Test
    @Transactional
    void getAllBagOfPointsByScoreBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        // Get all the bagOfPointList where scoreBalance is not null
        defaultBagOfPointShouldBeFound("scoreBalance.specified=true");

        // Get all the bagOfPointList where scoreBalance is null
        defaultBagOfPointShouldNotBeFound("scoreBalance.specified=false");
    }

    @Test
    @Transactional
    void getAllBagOfPointsByScoreBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        // Get all the bagOfPointList where scoreBalance is greater than or equal to DEFAULT_SCORE_BALANCE
        defaultBagOfPointShouldBeFound("scoreBalance.greaterThanOrEqual=" + DEFAULT_SCORE_BALANCE);

        // Get all the bagOfPointList where scoreBalance is greater than or equal to UPDATED_SCORE_BALANCE
        defaultBagOfPointShouldNotBeFound("scoreBalance.greaterThanOrEqual=" + UPDATED_SCORE_BALANCE);
    }

    @Test
    @Transactional
    void getAllBagOfPointsByScoreBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        // Get all the bagOfPointList where scoreBalance is less than or equal to DEFAULT_SCORE_BALANCE
        defaultBagOfPointShouldBeFound("scoreBalance.lessThanOrEqual=" + DEFAULT_SCORE_BALANCE);

        // Get all the bagOfPointList where scoreBalance is less than or equal to SMALLER_SCORE_BALANCE
        defaultBagOfPointShouldNotBeFound("scoreBalance.lessThanOrEqual=" + SMALLER_SCORE_BALANCE);
    }

    @Test
    @Transactional
    void getAllBagOfPointsByScoreBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        // Get all the bagOfPointList where scoreBalance is less than DEFAULT_SCORE_BALANCE
        defaultBagOfPointShouldNotBeFound("scoreBalance.lessThan=" + DEFAULT_SCORE_BALANCE);

        // Get all the bagOfPointList where scoreBalance is less than UPDATED_SCORE_BALANCE
        defaultBagOfPointShouldBeFound("scoreBalance.lessThan=" + UPDATED_SCORE_BALANCE);
    }

    @Test
    @Transactional
    void getAllBagOfPointsByScoreBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        // Get all the bagOfPointList where scoreBalance is greater than DEFAULT_SCORE_BALANCE
        defaultBagOfPointShouldNotBeFound("scoreBalance.greaterThan=" + DEFAULT_SCORE_BALANCE);

        // Get all the bagOfPointList where scoreBalance is greater than SMALLER_SCORE_BALANCE
        defaultBagOfPointShouldBeFound("scoreBalance.greaterThan=" + SMALLER_SCORE_BALANCE);
    }

    @Test
    @Transactional
    void getAllBagOfPointsByOperationAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        // Get all the bagOfPointList where operationAmount equals to DEFAULT_OPERATION_AMOUNT
        defaultBagOfPointShouldBeFound("operationAmount.equals=" + DEFAULT_OPERATION_AMOUNT);

        // Get all the bagOfPointList where operationAmount equals to UPDATED_OPERATION_AMOUNT
        defaultBagOfPointShouldNotBeFound("operationAmount.equals=" + UPDATED_OPERATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllBagOfPointsByOperationAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        // Get all the bagOfPointList where operationAmount not equals to DEFAULT_OPERATION_AMOUNT
        defaultBagOfPointShouldNotBeFound("operationAmount.notEquals=" + DEFAULT_OPERATION_AMOUNT);

        // Get all the bagOfPointList where operationAmount not equals to UPDATED_OPERATION_AMOUNT
        defaultBagOfPointShouldBeFound("operationAmount.notEquals=" + UPDATED_OPERATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllBagOfPointsByOperationAmountIsInShouldWork() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        // Get all the bagOfPointList where operationAmount in DEFAULT_OPERATION_AMOUNT or UPDATED_OPERATION_AMOUNT
        defaultBagOfPointShouldBeFound("operationAmount.in=" + DEFAULT_OPERATION_AMOUNT + "," + UPDATED_OPERATION_AMOUNT);

        // Get all the bagOfPointList where operationAmount equals to UPDATED_OPERATION_AMOUNT
        defaultBagOfPointShouldNotBeFound("operationAmount.in=" + UPDATED_OPERATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllBagOfPointsByOperationAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        // Get all the bagOfPointList where operationAmount is not null
        defaultBagOfPointShouldBeFound("operationAmount.specified=true");

        // Get all the bagOfPointList where operationAmount is null
        defaultBagOfPointShouldNotBeFound("operationAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllBagOfPointsByOperationAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        // Get all the bagOfPointList where operationAmount is greater than or equal to DEFAULT_OPERATION_AMOUNT
        defaultBagOfPointShouldBeFound("operationAmount.greaterThanOrEqual=" + DEFAULT_OPERATION_AMOUNT);

        // Get all the bagOfPointList where operationAmount is greater than or equal to UPDATED_OPERATION_AMOUNT
        defaultBagOfPointShouldNotBeFound("operationAmount.greaterThanOrEqual=" + UPDATED_OPERATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllBagOfPointsByOperationAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        // Get all the bagOfPointList where operationAmount is less than or equal to DEFAULT_OPERATION_AMOUNT
        defaultBagOfPointShouldBeFound("operationAmount.lessThanOrEqual=" + DEFAULT_OPERATION_AMOUNT);

        // Get all the bagOfPointList where operationAmount is less than or equal to SMALLER_OPERATION_AMOUNT
        defaultBagOfPointShouldNotBeFound("operationAmount.lessThanOrEqual=" + SMALLER_OPERATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllBagOfPointsByOperationAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        // Get all the bagOfPointList where operationAmount is less than DEFAULT_OPERATION_AMOUNT
        defaultBagOfPointShouldNotBeFound("operationAmount.lessThan=" + DEFAULT_OPERATION_AMOUNT);

        // Get all the bagOfPointList where operationAmount is less than UPDATED_OPERATION_AMOUNT
        defaultBagOfPointShouldBeFound("operationAmount.lessThan=" + UPDATED_OPERATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllBagOfPointsByOperationAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);

        // Get all the bagOfPointList where operationAmount is greater than DEFAULT_OPERATION_AMOUNT
        defaultBagOfPointShouldNotBeFound("operationAmount.greaterThan=" + DEFAULT_OPERATION_AMOUNT);

        // Get all the bagOfPointList where operationAmount is greater than SMALLER_OPERATION_AMOUNT
        defaultBagOfPointShouldBeFound("operationAmount.greaterThan=" + SMALLER_OPERATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllBagOfPointsByClientIsEqualToSomething() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);
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
        bagOfPoint.addClient(client);
        bagOfPointRepository.saveAndFlush(bagOfPoint);
        Long clientId = client.getId();

        // Get all the bagOfPointList where client equals to clientId
        defaultBagOfPointShouldBeFound("clientId.equals=" + clientId);

        // Get all the bagOfPointList where client equals to (clientId + 1)
        defaultBagOfPointShouldNotBeFound("clientId.equals=" + (clientId + 1));
    }

    @Test
    @Transactional
    void getAllBagOfPointsByPointUseDetailIsEqualToSomething() throws Exception {
        // Initialize the database
        bagOfPointRepository.saveAndFlush(bagOfPoint);
        PointUseDet pointUseDetail;
        if (TestUtil.findAll(em, PointUseDet.class).isEmpty()) {
            pointUseDetail = PointUseDetResourceIT.createEntity(em);
            em.persist(pointUseDetail);
            em.flush();
        } else {
            pointUseDetail = TestUtil.findAll(em, PointUseDet.class).get(0);
        }
        em.persist(pointUseDetail);
        em.flush();
        bagOfPoint.addPointUseDetail(pointUseDetail);
        bagOfPointRepository.saveAndFlush(bagOfPoint);
        Long pointUseDetailId = pointUseDetail.getId();

        // Get all the bagOfPointList where pointUseDetail equals to pointUseDetailId
        defaultBagOfPointShouldBeFound("pointUseDetailId.equals=" + pointUseDetailId);

        // Get all the bagOfPointList where pointUseDetail equals to (pointUseDetailId + 1)
        defaultBagOfPointShouldNotBeFound("pointUseDetailId.equals=" + (pointUseDetailId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBagOfPointShouldBeFound(String filter) throws Exception {
        restBagOfPointMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bagOfPoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].asignationDate").value(hasItem(DEFAULT_ASIGNATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].assignedScore").value(hasItem(DEFAULT_ASSIGNED_SCORE.intValue())))
            .andExpect(jsonPath("$.[*].scoreUsed").value(hasItem(DEFAULT_SCORE_USED.intValue())))
            .andExpect(jsonPath("$.[*].scoreBalance").value(hasItem(DEFAULT_SCORE_BALANCE.intValue())))
            .andExpect(jsonPath("$.[*].operationAmount").value(hasItem(DEFAULT_OPERATION_AMOUNT.doubleValue())));

        // Check, that the count call also returns 1
        restBagOfPointMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBagOfPointShouldNotBeFound(String filter) throws Exception {
        restBagOfPointMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBagOfPointMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
