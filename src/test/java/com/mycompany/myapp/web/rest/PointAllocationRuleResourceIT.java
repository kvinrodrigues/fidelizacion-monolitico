package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.PointAllocationRule;
import com.mycompany.myapp.repository.PointAllocationRuleRepository;
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

    private static final Long DEFAULT_UPPER_LIMIT = 1L;
    private static final Long UPDATED_UPPER_LIMIT = 2L;

    private static final Float DEFAULT_EQUIVALENCE_OF_A_POINT = 1F;
    private static final Float UPDATED_EQUIVALENCE_OF_A_POINT = 2F;

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
