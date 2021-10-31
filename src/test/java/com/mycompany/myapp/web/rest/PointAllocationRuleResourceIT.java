package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.PointAllocationRule;
import com.mycompany.myapp.repository.PointAllocationRuleRepository;
import com.mycompany.myapp.service.criteria.PointAllocationRuleCriteria;
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
 * Integration tests for the {@link PointAllocationRuleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PointAllocationRuleResourceIT {

    private static final Long DEFAULT_LOWER_LIMIT = 1L;
    private static final Long UPDATED_LOWER_LIMIT = 2L;
    private static final Long SMALLER_LOWER_LIMIT = 1L - 1L;

    private static final Long DEFAULT_UPPER_LIMIT = 1L;
    private static final Long UPDATED_UPPER_LIMIT = 2L;
    private static final Long SMALLER_UPPER_LIMIT = 1L - 1L;

    private static final Float DEFAULT_EQUIVALENCE_OF_A_POINT = 1F;
    private static final Float UPDATED_EQUIVALENCE_OF_A_POINT = 2F;
    private static final Float SMALLER_EQUIVALENCE_OF_A_POINT = 1F - 1F;

    private static final String ENTITY_API_URL = "/api/point-allocation-rules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PointAllocationRuleRepository pointAllocationRuleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPointAllocationRuleMockMvc;

    private PointAllocationRule pointAllocationRule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PointAllocationRule createEntity(EntityManager em) {
        PointAllocationRule pointAllocationRule = new PointAllocationRule()
            .lowerLimit(DEFAULT_LOWER_LIMIT)
            .upperLimit(DEFAULT_UPPER_LIMIT)
            .equivalenceOfAPoint(DEFAULT_EQUIVALENCE_OF_A_POINT);
        return pointAllocationRule;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PointAllocationRule createUpdatedEntity(EntityManager em) {
        PointAllocationRule pointAllocationRule = new PointAllocationRule()
            .lowerLimit(UPDATED_LOWER_LIMIT)
            .upperLimit(UPDATED_UPPER_LIMIT)
            .equivalenceOfAPoint(UPDATED_EQUIVALENCE_OF_A_POINT);
        return pointAllocationRule;
    }

    @BeforeEach
    public void initTest() {
        pointAllocationRule = createEntity(em);
    }

    @Test
    @Transactional
    void createPointAllocationRule() throws Exception {
        int databaseSizeBeforeCreate = pointAllocationRuleRepository.findAll().size();
        // Create the PointAllocationRule
        restPointAllocationRuleMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pointAllocationRule))
            )
            .andExpect(status().isCreated());

        // Validate the PointAllocationRule in the database
        List<PointAllocationRule> pointAllocationRuleList = pointAllocationRuleRepository.findAll();
        assertThat(pointAllocationRuleList).hasSize(databaseSizeBeforeCreate + 1);
        PointAllocationRule testPointAllocationRule = pointAllocationRuleList.get(pointAllocationRuleList.size() - 1);
        assertThat(testPointAllocationRule.getLowerLimit()).isEqualTo(DEFAULT_LOWER_LIMIT);
        assertThat(testPointAllocationRule.getUpperLimit()).isEqualTo(DEFAULT_UPPER_LIMIT);
        assertThat(testPointAllocationRule.getEquivalenceOfAPoint()).isEqualTo(DEFAULT_EQUIVALENCE_OF_A_POINT);
    }

    @Test
    @Transactional
    void createPointAllocationRuleWithExistingId() throws Exception {
        // Create the PointAllocationRule with an existing ID
        pointAllocationRule.setId(1L);

        int databaseSizeBeforeCreate = pointAllocationRuleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPointAllocationRuleMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pointAllocationRule))
            )
            .andExpect(status().isBadRequest());

        // Validate the PointAllocationRule in the database
        List<PointAllocationRule> pointAllocationRuleList = pointAllocationRuleRepository.findAll();
        assertThat(pointAllocationRuleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEquivalenceOfAPointIsRequired() throws Exception {
        int databaseSizeBeforeTest = pointAllocationRuleRepository.findAll().size();
        // set the field null
        pointAllocationRule.setEquivalenceOfAPoint(null);

        // Create the PointAllocationRule, which fails.

        restPointAllocationRuleMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pointAllocationRule))
            )
            .andExpect(status().isBadRequest());

        List<PointAllocationRule> pointAllocationRuleList = pointAllocationRuleRepository.findAll();
        assertThat(pointAllocationRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPointAllocationRules() throws Exception {
        // Initialize the database
        pointAllocationRuleRepository.saveAndFlush(pointAllocationRule);

        // Get all the pointAllocationRuleList
        restPointAllocationRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pointAllocationRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].lowerLimit").value(hasItem(DEFAULT_LOWER_LIMIT.intValue())))
            .andExpect(jsonPath("$.[*].upperLimit").value(hasItem(DEFAULT_UPPER_LIMIT.intValue())))
            .andExpect(jsonPath("$.[*].equivalenceOfAPoint").value(hasItem(DEFAULT_EQUIVALENCE_OF_A_POINT.doubleValue())));
    }

    @Test
    @Transactional
    void getPointAllocationRule() throws Exception {
        // Initialize the database
        pointAllocationRuleRepository.saveAndFlush(pointAllocationRule);

        // Get the pointAllocationRule
        restPointAllocationRuleMockMvc
            .perform(get(ENTITY_API_URL_ID, pointAllocationRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pointAllocationRule.getId().intValue()))
            .andExpect(jsonPath("$.lowerLimit").value(DEFAULT_LOWER_LIMIT.intValue()))
            .andExpect(jsonPath("$.upperLimit").value(DEFAULT_UPPER_LIMIT.intValue()))
            .andExpect(jsonPath("$.equivalenceOfAPoint").value(DEFAULT_EQUIVALENCE_OF_A_POINT.doubleValue()));
    }

    @Test
    @Transactional
    void getPointAllocationRulesByIdFiltering() throws Exception {
        // Initialize the database
        pointAllocationRuleRepository.saveAndFlush(pointAllocationRule);

        Long id = pointAllocationRule.getId();

        defaultPointAllocationRuleShouldBeFound("id.equals=" + id);
        defaultPointAllocationRuleShouldNotBeFound("id.notEquals=" + id);

        defaultPointAllocationRuleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPointAllocationRuleShouldNotBeFound("id.greaterThan=" + id);

        defaultPointAllocationRuleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPointAllocationRuleShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPointAllocationRulesByLowerLimitIsEqualToSomething() throws Exception {
        // Initialize the database
        pointAllocationRuleRepository.saveAndFlush(pointAllocationRule);

        // Get all the pointAllocationRuleList where lowerLimit equals to DEFAULT_LOWER_LIMIT
        defaultPointAllocationRuleShouldBeFound("lowerLimit.equals=" + DEFAULT_LOWER_LIMIT);

        // Get all the pointAllocationRuleList where lowerLimit equals to UPDATED_LOWER_LIMIT
        defaultPointAllocationRuleShouldNotBeFound("lowerLimit.equals=" + UPDATED_LOWER_LIMIT);
    }

    @Test
    @Transactional
    void getAllPointAllocationRulesByLowerLimitIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pointAllocationRuleRepository.saveAndFlush(pointAllocationRule);

        // Get all the pointAllocationRuleList where lowerLimit not equals to DEFAULT_LOWER_LIMIT
        defaultPointAllocationRuleShouldNotBeFound("lowerLimit.notEquals=" + DEFAULT_LOWER_LIMIT);

        // Get all the pointAllocationRuleList where lowerLimit not equals to UPDATED_LOWER_LIMIT
        defaultPointAllocationRuleShouldBeFound("lowerLimit.notEquals=" + UPDATED_LOWER_LIMIT);
    }

    @Test
    @Transactional
    void getAllPointAllocationRulesByLowerLimitIsInShouldWork() throws Exception {
        // Initialize the database
        pointAllocationRuleRepository.saveAndFlush(pointAllocationRule);

        // Get all the pointAllocationRuleList where lowerLimit in DEFAULT_LOWER_LIMIT or UPDATED_LOWER_LIMIT
        defaultPointAllocationRuleShouldBeFound("lowerLimit.in=" + DEFAULT_LOWER_LIMIT + "," + UPDATED_LOWER_LIMIT);

        // Get all the pointAllocationRuleList where lowerLimit equals to UPDATED_LOWER_LIMIT
        defaultPointAllocationRuleShouldNotBeFound("lowerLimit.in=" + UPDATED_LOWER_LIMIT);
    }

    @Test
    @Transactional
    void getAllPointAllocationRulesByLowerLimitIsNullOrNotNull() throws Exception {
        // Initialize the database
        pointAllocationRuleRepository.saveAndFlush(pointAllocationRule);

        // Get all the pointAllocationRuleList where lowerLimit is not null
        defaultPointAllocationRuleShouldBeFound("lowerLimit.specified=true");

        // Get all the pointAllocationRuleList where lowerLimit is null
        defaultPointAllocationRuleShouldNotBeFound("lowerLimit.specified=false");
    }

    @Test
    @Transactional
    void getAllPointAllocationRulesByLowerLimitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pointAllocationRuleRepository.saveAndFlush(pointAllocationRule);

        // Get all the pointAllocationRuleList where lowerLimit is greater than or equal to DEFAULT_LOWER_LIMIT
        defaultPointAllocationRuleShouldBeFound("lowerLimit.greaterThanOrEqual=" + DEFAULT_LOWER_LIMIT);

        // Get all the pointAllocationRuleList where lowerLimit is greater than or equal to UPDATED_LOWER_LIMIT
        defaultPointAllocationRuleShouldNotBeFound("lowerLimit.greaterThanOrEqual=" + UPDATED_LOWER_LIMIT);
    }

    @Test
    @Transactional
    void getAllPointAllocationRulesByLowerLimitIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pointAllocationRuleRepository.saveAndFlush(pointAllocationRule);

        // Get all the pointAllocationRuleList where lowerLimit is less than or equal to DEFAULT_LOWER_LIMIT
        defaultPointAllocationRuleShouldBeFound("lowerLimit.lessThanOrEqual=" + DEFAULT_LOWER_LIMIT);

        // Get all the pointAllocationRuleList where lowerLimit is less than or equal to SMALLER_LOWER_LIMIT
        defaultPointAllocationRuleShouldNotBeFound("lowerLimit.lessThanOrEqual=" + SMALLER_LOWER_LIMIT);
    }

    @Test
    @Transactional
    void getAllPointAllocationRulesByLowerLimitIsLessThanSomething() throws Exception {
        // Initialize the database
        pointAllocationRuleRepository.saveAndFlush(pointAllocationRule);

        // Get all the pointAllocationRuleList where lowerLimit is less than DEFAULT_LOWER_LIMIT
        defaultPointAllocationRuleShouldNotBeFound("lowerLimit.lessThan=" + DEFAULT_LOWER_LIMIT);

        // Get all the pointAllocationRuleList where lowerLimit is less than UPDATED_LOWER_LIMIT
        defaultPointAllocationRuleShouldBeFound("lowerLimit.lessThan=" + UPDATED_LOWER_LIMIT);
    }

    @Test
    @Transactional
    void getAllPointAllocationRulesByLowerLimitIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pointAllocationRuleRepository.saveAndFlush(pointAllocationRule);

        // Get all the pointAllocationRuleList where lowerLimit is greater than DEFAULT_LOWER_LIMIT
        defaultPointAllocationRuleShouldNotBeFound("lowerLimit.greaterThan=" + DEFAULT_LOWER_LIMIT);

        // Get all the pointAllocationRuleList where lowerLimit is greater than SMALLER_LOWER_LIMIT
        defaultPointAllocationRuleShouldBeFound("lowerLimit.greaterThan=" + SMALLER_LOWER_LIMIT);
    }

    @Test
    @Transactional
    void getAllPointAllocationRulesByUpperLimitIsEqualToSomething() throws Exception {
        // Initialize the database
        pointAllocationRuleRepository.saveAndFlush(pointAllocationRule);

        // Get all the pointAllocationRuleList where upperLimit equals to DEFAULT_UPPER_LIMIT
        defaultPointAllocationRuleShouldBeFound("upperLimit.equals=" + DEFAULT_UPPER_LIMIT);

        // Get all the pointAllocationRuleList where upperLimit equals to UPDATED_UPPER_LIMIT
        defaultPointAllocationRuleShouldNotBeFound("upperLimit.equals=" + UPDATED_UPPER_LIMIT);
    }

    @Test
    @Transactional
    void getAllPointAllocationRulesByUpperLimitIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pointAllocationRuleRepository.saveAndFlush(pointAllocationRule);

        // Get all the pointAllocationRuleList where upperLimit not equals to DEFAULT_UPPER_LIMIT
        defaultPointAllocationRuleShouldNotBeFound("upperLimit.notEquals=" + DEFAULT_UPPER_LIMIT);

        // Get all the pointAllocationRuleList where upperLimit not equals to UPDATED_UPPER_LIMIT
        defaultPointAllocationRuleShouldBeFound("upperLimit.notEquals=" + UPDATED_UPPER_LIMIT);
    }

    @Test
    @Transactional
    void getAllPointAllocationRulesByUpperLimitIsInShouldWork() throws Exception {
        // Initialize the database
        pointAllocationRuleRepository.saveAndFlush(pointAllocationRule);

        // Get all the pointAllocationRuleList where upperLimit in DEFAULT_UPPER_LIMIT or UPDATED_UPPER_LIMIT
        defaultPointAllocationRuleShouldBeFound("upperLimit.in=" + DEFAULT_UPPER_LIMIT + "," + UPDATED_UPPER_LIMIT);

        // Get all the pointAllocationRuleList where upperLimit equals to UPDATED_UPPER_LIMIT
        defaultPointAllocationRuleShouldNotBeFound("upperLimit.in=" + UPDATED_UPPER_LIMIT);
    }

    @Test
    @Transactional
    void getAllPointAllocationRulesByUpperLimitIsNullOrNotNull() throws Exception {
        // Initialize the database
        pointAllocationRuleRepository.saveAndFlush(pointAllocationRule);

        // Get all the pointAllocationRuleList where upperLimit is not null
        defaultPointAllocationRuleShouldBeFound("upperLimit.specified=true");

        // Get all the pointAllocationRuleList where upperLimit is null
        defaultPointAllocationRuleShouldNotBeFound("upperLimit.specified=false");
    }

    @Test
    @Transactional
    void getAllPointAllocationRulesByUpperLimitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pointAllocationRuleRepository.saveAndFlush(pointAllocationRule);

        // Get all the pointAllocationRuleList where upperLimit is greater than or equal to DEFAULT_UPPER_LIMIT
        defaultPointAllocationRuleShouldBeFound("upperLimit.greaterThanOrEqual=" + DEFAULT_UPPER_LIMIT);

        // Get all the pointAllocationRuleList where upperLimit is greater than or equal to UPDATED_UPPER_LIMIT
        defaultPointAllocationRuleShouldNotBeFound("upperLimit.greaterThanOrEqual=" + UPDATED_UPPER_LIMIT);
    }

    @Test
    @Transactional
    void getAllPointAllocationRulesByUpperLimitIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pointAllocationRuleRepository.saveAndFlush(pointAllocationRule);

        // Get all the pointAllocationRuleList where upperLimit is less than or equal to DEFAULT_UPPER_LIMIT
        defaultPointAllocationRuleShouldBeFound("upperLimit.lessThanOrEqual=" + DEFAULT_UPPER_LIMIT);

        // Get all the pointAllocationRuleList where upperLimit is less than or equal to SMALLER_UPPER_LIMIT
        defaultPointAllocationRuleShouldNotBeFound("upperLimit.lessThanOrEqual=" + SMALLER_UPPER_LIMIT);
    }

    @Test
    @Transactional
    void getAllPointAllocationRulesByUpperLimitIsLessThanSomething() throws Exception {
        // Initialize the database
        pointAllocationRuleRepository.saveAndFlush(pointAllocationRule);

        // Get all the pointAllocationRuleList where upperLimit is less than DEFAULT_UPPER_LIMIT
        defaultPointAllocationRuleShouldNotBeFound("upperLimit.lessThan=" + DEFAULT_UPPER_LIMIT);

        // Get all the pointAllocationRuleList where upperLimit is less than UPDATED_UPPER_LIMIT
        defaultPointAllocationRuleShouldBeFound("upperLimit.lessThan=" + UPDATED_UPPER_LIMIT);
    }

    @Test
    @Transactional
    void getAllPointAllocationRulesByUpperLimitIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pointAllocationRuleRepository.saveAndFlush(pointAllocationRule);

        // Get all the pointAllocationRuleList where upperLimit is greater than DEFAULT_UPPER_LIMIT
        defaultPointAllocationRuleShouldNotBeFound("upperLimit.greaterThan=" + DEFAULT_UPPER_LIMIT);

        // Get all the pointAllocationRuleList where upperLimit is greater than SMALLER_UPPER_LIMIT
        defaultPointAllocationRuleShouldBeFound("upperLimit.greaterThan=" + SMALLER_UPPER_LIMIT);
    }

    @Test
    @Transactional
    void getAllPointAllocationRulesByEquivalenceOfAPointIsEqualToSomething() throws Exception {
        // Initialize the database
        pointAllocationRuleRepository.saveAndFlush(pointAllocationRule);

        // Get all the pointAllocationRuleList where equivalenceOfAPoint equals to DEFAULT_EQUIVALENCE_OF_A_POINT
        defaultPointAllocationRuleShouldBeFound("equivalenceOfAPoint.equals=" + DEFAULT_EQUIVALENCE_OF_A_POINT);

        // Get all the pointAllocationRuleList where equivalenceOfAPoint equals to UPDATED_EQUIVALENCE_OF_A_POINT
        defaultPointAllocationRuleShouldNotBeFound("equivalenceOfAPoint.equals=" + UPDATED_EQUIVALENCE_OF_A_POINT);
    }

    @Test
    @Transactional
    void getAllPointAllocationRulesByEquivalenceOfAPointIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pointAllocationRuleRepository.saveAndFlush(pointAllocationRule);

        // Get all the pointAllocationRuleList where equivalenceOfAPoint not equals to DEFAULT_EQUIVALENCE_OF_A_POINT
        defaultPointAllocationRuleShouldNotBeFound("equivalenceOfAPoint.notEquals=" + DEFAULT_EQUIVALENCE_OF_A_POINT);

        // Get all the pointAllocationRuleList where equivalenceOfAPoint not equals to UPDATED_EQUIVALENCE_OF_A_POINT
        defaultPointAllocationRuleShouldBeFound("equivalenceOfAPoint.notEquals=" + UPDATED_EQUIVALENCE_OF_A_POINT);
    }

    @Test
    @Transactional
    void getAllPointAllocationRulesByEquivalenceOfAPointIsInShouldWork() throws Exception {
        // Initialize the database
        pointAllocationRuleRepository.saveAndFlush(pointAllocationRule);

        // Get all the pointAllocationRuleList where equivalenceOfAPoint in DEFAULT_EQUIVALENCE_OF_A_POINT or UPDATED_EQUIVALENCE_OF_A_POINT
        defaultPointAllocationRuleShouldBeFound(
            "equivalenceOfAPoint.in=" + DEFAULT_EQUIVALENCE_OF_A_POINT + "," + UPDATED_EQUIVALENCE_OF_A_POINT
        );

        // Get all the pointAllocationRuleList where equivalenceOfAPoint equals to UPDATED_EQUIVALENCE_OF_A_POINT
        defaultPointAllocationRuleShouldNotBeFound("equivalenceOfAPoint.in=" + UPDATED_EQUIVALENCE_OF_A_POINT);
    }

    @Test
    @Transactional
    void getAllPointAllocationRulesByEquivalenceOfAPointIsNullOrNotNull() throws Exception {
        // Initialize the database
        pointAllocationRuleRepository.saveAndFlush(pointAllocationRule);

        // Get all the pointAllocationRuleList where equivalenceOfAPoint is not null
        defaultPointAllocationRuleShouldBeFound("equivalenceOfAPoint.specified=true");

        // Get all the pointAllocationRuleList where equivalenceOfAPoint is null
        defaultPointAllocationRuleShouldNotBeFound("equivalenceOfAPoint.specified=false");
    }

    @Test
    @Transactional
    void getAllPointAllocationRulesByEquivalenceOfAPointIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pointAllocationRuleRepository.saveAndFlush(pointAllocationRule);

        // Get all the pointAllocationRuleList where equivalenceOfAPoint is greater than or equal to DEFAULT_EQUIVALENCE_OF_A_POINT
        defaultPointAllocationRuleShouldBeFound("equivalenceOfAPoint.greaterThanOrEqual=" + DEFAULT_EQUIVALENCE_OF_A_POINT);

        // Get all the pointAllocationRuleList where equivalenceOfAPoint is greater than or equal to UPDATED_EQUIVALENCE_OF_A_POINT
        defaultPointAllocationRuleShouldNotBeFound("equivalenceOfAPoint.greaterThanOrEqual=" + UPDATED_EQUIVALENCE_OF_A_POINT);
    }

    @Test
    @Transactional
    void getAllPointAllocationRulesByEquivalenceOfAPointIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pointAllocationRuleRepository.saveAndFlush(pointAllocationRule);

        // Get all the pointAllocationRuleList where equivalenceOfAPoint is less than or equal to DEFAULT_EQUIVALENCE_OF_A_POINT
        defaultPointAllocationRuleShouldBeFound("equivalenceOfAPoint.lessThanOrEqual=" + DEFAULT_EQUIVALENCE_OF_A_POINT);

        // Get all the pointAllocationRuleList where equivalenceOfAPoint is less than or equal to SMALLER_EQUIVALENCE_OF_A_POINT
        defaultPointAllocationRuleShouldNotBeFound("equivalenceOfAPoint.lessThanOrEqual=" + SMALLER_EQUIVALENCE_OF_A_POINT);
    }

    @Test
    @Transactional
    void getAllPointAllocationRulesByEquivalenceOfAPointIsLessThanSomething() throws Exception {
        // Initialize the database
        pointAllocationRuleRepository.saveAndFlush(pointAllocationRule);

        // Get all the pointAllocationRuleList where equivalenceOfAPoint is less than DEFAULT_EQUIVALENCE_OF_A_POINT
        defaultPointAllocationRuleShouldNotBeFound("equivalenceOfAPoint.lessThan=" + DEFAULT_EQUIVALENCE_OF_A_POINT);

        // Get all the pointAllocationRuleList where equivalenceOfAPoint is less than UPDATED_EQUIVALENCE_OF_A_POINT
        defaultPointAllocationRuleShouldBeFound("equivalenceOfAPoint.lessThan=" + UPDATED_EQUIVALENCE_OF_A_POINT);
    }

    @Test
    @Transactional
    void getAllPointAllocationRulesByEquivalenceOfAPointIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pointAllocationRuleRepository.saveAndFlush(pointAllocationRule);

        // Get all the pointAllocationRuleList where equivalenceOfAPoint is greater than DEFAULT_EQUIVALENCE_OF_A_POINT
        defaultPointAllocationRuleShouldNotBeFound("equivalenceOfAPoint.greaterThan=" + DEFAULT_EQUIVALENCE_OF_A_POINT);

        // Get all the pointAllocationRuleList where equivalenceOfAPoint is greater than SMALLER_EQUIVALENCE_OF_A_POINT
        defaultPointAllocationRuleShouldBeFound("equivalenceOfAPoint.greaterThan=" + SMALLER_EQUIVALENCE_OF_A_POINT);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPointAllocationRuleShouldBeFound(String filter) throws Exception {
        restPointAllocationRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pointAllocationRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].lowerLimit").value(hasItem(DEFAULT_LOWER_LIMIT.intValue())))
            .andExpect(jsonPath("$.[*].upperLimit").value(hasItem(DEFAULT_UPPER_LIMIT.intValue())))
            .andExpect(jsonPath("$.[*].equivalenceOfAPoint").value(hasItem(DEFAULT_EQUIVALENCE_OF_A_POINT.doubleValue())));

        // Check, that the count call also returns 1
        restPointAllocationRuleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPointAllocationRuleShouldNotBeFound(String filter) throws Exception {
        restPointAllocationRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPointAllocationRuleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPointAllocationRule() throws Exception {
        // Get the pointAllocationRule
        restPointAllocationRuleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPointAllocationRule() throws Exception {
        // Initialize the database
        pointAllocationRuleRepository.saveAndFlush(pointAllocationRule);

        int databaseSizeBeforeUpdate = pointAllocationRuleRepository.findAll().size();

        // Update the pointAllocationRule
        PointAllocationRule updatedPointAllocationRule = pointAllocationRuleRepository.findById(pointAllocationRule.getId()).get();
        // Disconnect from session so that the updates on updatedPointAllocationRule are not directly saved in db
        em.detach(updatedPointAllocationRule);
        updatedPointAllocationRule
            .lowerLimit(UPDATED_LOWER_LIMIT)
            .upperLimit(UPDATED_UPPER_LIMIT)
            .equivalenceOfAPoint(UPDATED_EQUIVALENCE_OF_A_POINT);

        restPointAllocationRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPointAllocationRule.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPointAllocationRule))
            )
            .andExpect(status().isOk());

        // Validate the PointAllocationRule in the database
        List<PointAllocationRule> pointAllocationRuleList = pointAllocationRuleRepository.findAll();
        assertThat(pointAllocationRuleList).hasSize(databaseSizeBeforeUpdate);
        PointAllocationRule testPointAllocationRule = pointAllocationRuleList.get(pointAllocationRuleList.size() - 1);
        assertThat(testPointAllocationRule.getLowerLimit()).isEqualTo(UPDATED_LOWER_LIMIT);
        assertThat(testPointAllocationRule.getUpperLimit()).isEqualTo(UPDATED_UPPER_LIMIT);
        assertThat(testPointAllocationRule.getEquivalenceOfAPoint()).isEqualTo(UPDATED_EQUIVALENCE_OF_A_POINT);
    }

    @Test
    @Transactional
    void putNonExistingPointAllocationRule() throws Exception {
        int databaseSizeBeforeUpdate = pointAllocationRuleRepository.findAll().size();
        pointAllocationRule.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPointAllocationRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pointAllocationRule.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pointAllocationRule))
            )
            .andExpect(status().isBadRequest());

        // Validate the PointAllocationRule in the database
        List<PointAllocationRule> pointAllocationRuleList = pointAllocationRuleRepository.findAll();
        assertThat(pointAllocationRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPointAllocationRule() throws Exception {
        int databaseSizeBeforeUpdate = pointAllocationRuleRepository.findAll().size();
        pointAllocationRule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointAllocationRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pointAllocationRule))
            )
            .andExpect(status().isBadRequest());

        // Validate the PointAllocationRule in the database
        List<PointAllocationRule> pointAllocationRuleList = pointAllocationRuleRepository.findAll();
        assertThat(pointAllocationRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPointAllocationRule() throws Exception {
        int databaseSizeBeforeUpdate = pointAllocationRuleRepository.findAll().size();
        pointAllocationRule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointAllocationRuleMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pointAllocationRule))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PointAllocationRule in the database
        List<PointAllocationRule> pointAllocationRuleList = pointAllocationRuleRepository.findAll();
        assertThat(pointAllocationRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePointAllocationRuleWithPatch() throws Exception {
        // Initialize the database
        pointAllocationRuleRepository.saveAndFlush(pointAllocationRule);

        int databaseSizeBeforeUpdate = pointAllocationRuleRepository.findAll().size();

        // Update the pointAllocationRule using partial update
        PointAllocationRule partialUpdatedPointAllocationRule = new PointAllocationRule();
        partialUpdatedPointAllocationRule.setId(pointAllocationRule.getId());

        partialUpdatedPointAllocationRule.lowerLimit(UPDATED_LOWER_LIMIT).equivalenceOfAPoint(UPDATED_EQUIVALENCE_OF_A_POINT);

        restPointAllocationRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPointAllocationRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPointAllocationRule))
            )
            .andExpect(status().isOk());

        // Validate the PointAllocationRule in the database
        List<PointAllocationRule> pointAllocationRuleList = pointAllocationRuleRepository.findAll();
        assertThat(pointAllocationRuleList).hasSize(databaseSizeBeforeUpdate);
        PointAllocationRule testPointAllocationRule = pointAllocationRuleList.get(pointAllocationRuleList.size() - 1);
        assertThat(testPointAllocationRule.getLowerLimit()).isEqualTo(UPDATED_LOWER_LIMIT);
        assertThat(testPointAllocationRule.getUpperLimit()).isEqualTo(DEFAULT_UPPER_LIMIT);
        assertThat(testPointAllocationRule.getEquivalenceOfAPoint()).isEqualTo(UPDATED_EQUIVALENCE_OF_A_POINT);
    }

    @Test
    @Transactional
    void fullUpdatePointAllocationRuleWithPatch() throws Exception {
        // Initialize the database
        pointAllocationRuleRepository.saveAndFlush(pointAllocationRule);

        int databaseSizeBeforeUpdate = pointAllocationRuleRepository.findAll().size();

        // Update the pointAllocationRule using partial update
        PointAllocationRule partialUpdatedPointAllocationRule = new PointAllocationRule();
        partialUpdatedPointAllocationRule.setId(pointAllocationRule.getId());

        partialUpdatedPointAllocationRule
            .lowerLimit(UPDATED_LOWER_LIMIT)
            .upperLimit(UPDATED_UPPER_LIMIT)
            .equivalenceOfAPoint(UPDATED_EQUIVALENCE_OF_A_POINT);

        restPointAllocationRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPointAllocationRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPointAllocationRule))
            )
            .andExpect(status().isOk());

        // Validate the PointAllocationRule in the database
        List<PointAllocationRule> pointAllocationRuleList = pointAllocationRuleRepository.findAll();
        assertThat(pointAllocationRuleList).hasSize(databaseSizeBeforeUpdate);
        PointAllocationRule testPointAllocationRule = pointAllocationRuleList.get(pointAllocationRuleList.size() - 1);
        assertThat(testPointAllocationRule.getLowerLimit()).isEqualTo(UPDATED_LOWER_LIMIT);
        assertThat(testPointAllocationRule.getUpperLimit()).isEqualTo(UPDATED_UPPER_LIMIT);
        assertThat(testPointAllocationRule.getEquivalenceOfAPoint()).isEqualTo(UPDATED_EQUIVALENCE_OF_A_POINT);
    }

    @Test
    @Transactional
    void patchNonExistingPointAllocationRule() throws Exception {
        int databaseSizeBeforeUpdate = pointAllocationRuleRepository.findAll().size();
        pointAllocationRule.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPointAllocationRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pointAllocationRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pointAllocationRule))
            )
            .andExpect(status().isBadRequest());

        // Validate the PointAllocationRule in the database
        List<PointAllocationRule> pointAllocationRuleList = pointAllocationRuleRepository.findAll();
        assertThat(pointAllocationRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPointAllocationRule() throws Exception {
        int databaseSizeBeforeUpdate = pointAllocationRuleRepository.findAll().size();
        pointAllocationRule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointAllocationRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pointAllocationRule))
            )
            .andExpect(status().isBadRequest());

        // Validate the PointAllocationRule in the database
        List<PointAllocationRule> pointAllocationRuleList = pointAllocationRuleRepository.findAll();
        assertThat(pointAllocationRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPointAllocationRule() throws Exception {
        int databaseSizeBeforeUpdate = pointAllocationRuleRepository.findAll().size();
        pointAllocationRule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointAllocationRuleMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pointAllocationRule))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PointAllocationRule in the database
        List<PointAllocationRule> pointAllocationRuleList = pointAllocationRuleRepository.findAll();
        assertThat(pointAllocationRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePointAllocationRule() throws Exception {
        // Initialize the database
        pointAllocationRuleRepository.saveAndFlush(pointAllocationRule);

        int databaseSizeBeforeDelete = pointAllocationRuleRepository.findAll().size();

        // Delete the pointAllocationRule
        restPointAllocationRuleMockMvc
            .perform(delete(ENTITY_API_URL_ID, pointAllocationRule.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PointAllocationRule> pointAllocationRuleList = pointAllocationRuleRepository.findAll();
        assertThat(pointAllocationRuleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
