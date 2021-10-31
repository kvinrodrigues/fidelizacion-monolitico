package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.BagOfPoint;
import com.mycompany.myapp.domain.PointUse;
import com.mycompany.myapp.domain.PointUseDet;
import com.mycompany.myapp.repository.PointUseDetRepository;
import com.mycompany.myapp.service.criteria.PointUseDetCriteria;
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
 * Integration tests for the {@link PointUseDetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PointUseDetResourceIT {

    private static final Long DEFAULT_SCORE_USED = 1L;
    private static final Long UPDATED_SCORE_USED = 2L;
    private static final Long SMALLER_SCORE_USED = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/point-use-dets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PointUseDetRepository pointUseDetRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPointUseDetMockMvc;

    private PointUseDet pointUseDet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PointUseDet createEntity(EntityManager em) {
        PointUseDet pointUseDet = new PointUseDet().scoreUsed(DEFAULT_SCORE_USED);
        return pointUseDet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PointUseDet createUpdatedEntity(EntityManager em) {
        PointUseDet pointUseDet = new PointUseDet().scoreUsed(UPDATED_SCORE_USED);
        return pointUseDet;
    }

    @BeforeEach
    public void initTest() {
        pointUseDet = createEntity(em);
    }

    @Test
    @Transactional
    void createPointUseDet() throws Exception {
        int databaseSizeBeforeCreate = pointUseDetRepository.findAll().size();
        // Create the PointUseDet
        restPointUseDetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pointUseDet)))
            .andExpect(status().isCreated());

        // Validate the PointUseDet in the database
        List<PointUseDet> pointUseDetList = pointUseDetRepository.findAll();
        assertThat(pointUseDetList).hasSize(databaseSizeBeforeCreate + 1);
        PointUseDet testPointUseDet = pointUseDetList.get(pointUseDetList.size() - 1);
        assertThat(testPointUseDet.getScoreUsed()).isEqualTo(DEFAULT_SCORE_USED);
    }

    @Test
    @Transactional
    void createPointUseDetWithExistingId() throws Exception {
        // Create the PointUseDet with an existing ID
        pointUseDet.setId(1L);

        int databaseSizeBeforeCreate = pointUseDetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPointUseDetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pointUseDet)))
            .andExpect(status().isBadRequest());

        // Validate the PointUseDet in the database
        List<PointUseDet> pointUseDetList = pointUseDetRepository.findAll();
        assertThat(pointUseDetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkScoreUsedIsRequired() throws Exception {
        int databaseSizeBeforeTest = pointUseDetRepository.findAll().size();
        // set the field null
        pointUseDet.setScoreUsed(null);

        // Create the PointUseDet, which fails.

        restPointUseDetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pointUseDet)))
            .andExpect(status().isBadRequest());

        List<PointUseDet> pointUseDetList = pointUseDetRepository.findAll();
        assertThat(pointUseDetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPointUseDets() throws Exception {
        // Initialize the database
        pointUseDetRepository.saveAndFlush(pointUseDet);

        // Get all the pointUseDetList
        restPointUseDetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pointUseDet.getId().intValue())))
            .andExpect(jsonPath("$.[*].scoreUsed").value(hasItem(DEFAULT_SCORE_USED.intValue())));
    }

    @Test
    @Transactional
    void getPointUseDet() throws Exception {
        // Initialize the database
        pointUseDetRepository.saveAndFlush(pointUseDet);

        // Get the pointUseDet
        restPointUseDetMockMvc
            .perform(get(ENTITY_API_URL_ID, pointUseDet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pointUseDet.getId().intValue()))
            .andExpect(jsonPath("$.scoreUsed").value(DEFAULT_SCORE_USED.intValue()));
    }

    @Test
    @Transactional
    void getPointUseDetsByIdFiltering() throws Exception {
        // Initialize the database
        pointUseDetRepository.saveAndFlush(pointUseDet);

        Long id = pointUseDet.getId();

        defaultPointUseDetShouldBeFound("id.equals=" + id);
        defaultPointUseDetShouldNotBeFound("id.notEquals=" + id);

        defaultPointUseDetShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPointUseDetShouldNotBeFound("id.greaterThan=" + id);

        defaultPointUseDetShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPointUseDetShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPointUseDetsByScoreUsedIsEqualToSomething() throws Exception {
        // Initialize the database
        pointUseDetRepository.saveAndFlush(pointUseDet);

        // Get all the pointUseDetList where scoreUsed equals to DEFAULT_SCORE_USED
        defaultPointUseDetShouldBeFound("scoreUsed.equals=" + DEFAULT_SCORE_USED);

        // Get all the pointUseDetList where scoreUsed equals to UPDATED_SCORE_USED
        defaultPointUseDetShouldNotBeFound("scoreUsed.equals=" + UPDATED_SCORE_USED);
    }

    @Test
    @Transactional
    void getAllPointUseDetsByScoreUsedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pointUseDetRepository.saveAndFlush(pointUseDet);

        // Get all the pointUseDetList where scoreUsed not equals to DEFAULT_SCORE_USED
        defaultPointUseDetShouldNotBeFound("scoreUsed.notEquals=" + DEFAULT_SCORE_USED);

        // Get all the pointUseDetList where scoreUsed not equals to UPDATED_SCORE_USED
        defaultPointUseDetShouldBeFound("scoreUsed.notEquals=" + UPDATED_SCORE_USED);
    }

    @Test
    @Transactional
    void getAllPointUseDetsByScoreUsedIsInShouldWork() throws Exception {
        // Initialize the database
        pointUseDetRepository.saveAndFlush(pointUseDet);

        // Get all the pointUseDetList where scoreUsed in DEFAULT_SCORE_USED or UPDATED_SCORE_USED
        defaultPointUseDetShouldBeFound("scoreUsed.in=" + DEFAULT_SCORE_USED + "," + UPDATED_SCORE_USED);

        // Get all the pointUseDetList where scoreUsed equals to UPDATED_SCORE_USED
        defaultPointUseDetShouldNotBeFound("scoreUsed.in=" + UPDATED_SCORE_USED);
    }

    @Test
    @Transactional
    void getAllPointUseDetsByScoreUsedIsNullOrNotNull() throws Exception {
        // Initialize the database
        pointUseDetRepository.saveAndFlush(pointUseDet);

        // Get all the pointUseDetList where scoreUsed is not null
        defaultPointUseDetShouldBeFound("scoreUsed.specified=true");

        // Get all the pointUseDetList where scoreUsed is null
        defaultPointUseDetShouldNotBeFound("scoreUsed.specified=false");
    }

    @Test
    @Transactional
    void getAllPointUseDetsByScoreUsedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pointUseDetRepository.saveAndFlush(pointUseDet);

        // Get all the pointUseDetList where scoreUsed is greater than or equal to DEFAULT_SCORE_USED
        defaultPointUseDetShouldBeFound("scoreUsed.greaterThanOrEqual=" + DEFAULT_SCORE_USED);

        // Get all the pointUseDetList where scoreUsed is greater than or equal to UPDATED_SCORE_USED
        defaultPointUseDetShouldNotBeFound("scoreUsed.greaterThanOrEqual=" + UPDATED_SCORE_USED);
    }

    @Test
    @Transactional
    void getAllPointUseDetsByScoreUsedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pointUseDetRepository.saveAndFlush(pointUseDet);

        // Get all the pointUseDetList where scoreUsed is less than or equal to DEFAULT_SCORE_USED
        defaultPointUseDetShouldBeFound("scoreUsed.lessThanOrEqual=" + DEFAULT_SCORE_USED);

        // Get all the pointUseDetList where scoreUsed is less than or equal to SMALLER_SCORE_USED
        defaultPointUseDetShouldNotBeFound("scoreUsed.lessThanOrEqual=" + SMALLER_SCORE_USED);
    }

    @Test
    @Transactional
    void getAllPointUseDetsByScoreUsedIsLessThanSomething() throws Exception {
        // Initialize the database
        pointUseDetRepository.saveAndFlush(pointUseDet);

        // Get all the pointUseDetList where scoreUsed is less than DEFAULT_SCORE_USED
        defaultPointUseDetShouldNotBeFound("scoreUsed.lessThan=" + DEFAULT_SCORE_USED);

        // Get all the pointUseDetList where scoreUsed is less than UPDATED_SCORE_USED
        defaultPointUseDetShouldBeFound("scoreUsed.lessThan=" + UPDATED_SCORE_USED);
    }

    @Test
    @Transactional
    void getAllPointUseDetsByScoreUsedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pointUseDetRepository.saveAndFlush(pointUseDet);

        // Get all the pointUseDetList where scoreUsed is greater than DEFAULT_SCORE_USED
        defaultPointUseDetShouldNotBeFound("scoreUsed.greaterThan=" + DEFAULT_SCORE_USED);

        // Get all the pointUseDetList where scoreUsed is greater than SMALLER_SCORE_USED
        defaultPointUseDetShouldBeFound("scoreUsed.greaterThan=" + SMALLER_SCORE_USED);
    }

    @Test
    @Transactional
    void getAllPointUseDetsByPointUseIsEqualToSomething() throws Exception {
        // Initialize the database
        pointUseDetRepository.saveAndFlush(pointUseDet);
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
        pointUseDet.setPointUse(pointUse);
        pointUseDetRepository.saveAndFlush(pointUseDet);
        Long pointUseId = pointUse.getId();

        // Get all the pointUseDetList where pointUse equals to pointUseId
        defaultPointUseDetShouldBeFound("pointUseId.equals=" + pointUseId);

        // Get all the pointUseDetList where pointUse equals to (pointUseId + 1)
        defaultPointUseDetShouldNotBeFound("pointUseId.equals=" + (pointUseId + 1));
    }

    @Test
    @Transactional
    void getAllPointUseDetsByBagOfPointIsEqualToSomething() throws Exception {
        // Initialize the database
        pointUseDetRepository.saveAndFlush(pointUseDet);
        BagOfPoint bagOfPoint;
        if (TestUtil.findAll(em, BagOfPoint.class).isEmpty()) {
            bagOfPoint = BagOfPointResourceIT.createEntity(em);
            em.persist(bagOfPoint);
            em.flush();
        } else {
            bagOfPoint = TestUtil.findAll(em, BagOfPoint.class).get(0);
        }
        em.persist(bagOfPoint);
        em.flush();
        pointUseDet.setBagOfPoint(bagOfPoint);
        pointUseDetRepository.saveAndFlush(pointUseDet);
        Long bagOfPointId = bagOfPoint.getId();

        // Get all the pointUseDetList where bagOfPoint equals to bagOfPointId
        defaultPointUseDetShouldBeFound("bagOfPointId.equals=" + bagOfPointId);

        // Get all the pointUseDetList where bagOfPoint equals to (bagOfPointId + 1)
        defaultPointUseDetShouldNotBeFound("bagOfPointId.equals=" + (bagOfPointId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPointUseDetShouldBeFound(String filter) throws Exception {
        restPointUseDetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pointUseDet.getId().intValue())))
            .andExpect(jsonPath("$.[*].scoreUsed").value(hasItem(DEFAULT_SCORE_USED.intValue())));

        // Check, that the count call also returns 1
        restPointUseDetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPointUseDetShouldNotBeFound(String filter) throws Exception {
        restPointUseDetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPointUseDetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPointUseDet() throws Exception {
        // Get the pointUseDet
        restPointUseDetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPointUseDet() throws Exception {
        // Initialize the database
        pointUseDetRepository.saveAndFlush(pointUseDet);

        int databaseSizeBeforeUpdate = pointUseDetRepository.findAll().size();

        // Update the pointUseDet
        PointUseDet updatedPointUseDet = pointUseDetRepository.findById(pointUseDet.getId()).get();
        // Disconnect from session so that the updates on updatedPointUseDet are not directly saved in db
        em.detach(updatedPointUseDet);
        updatedPointUseDet.scoreUsed(UPDATED_SCORE_USED);

        restPointUseDetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPointUseDet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPointUseDet))
            )
            .andExpect(status().isOk());

        // Validate the PointUseDet in the database
        List<PointUseDet> pointUseDetList = pointUseDetRepository.findAll();
        assertThat(pointUseDetList).hasSize(databaseSizeBeforeUpdate);
        PointUseDet testPointUseDet = pointUseDetList.get(pointUseDetList.size() - 1);
        assertThat(testPointUseDet.getScoreUsed()).isEqualTo(UPDATED_SCORE_USED);
    }

    @Test
    @Transactional
    void putNonExistingPointUseDet() throws Exception {
        int databaseSizeBeforeUpdate = pointUseDetRepository.findAll().size();
        pointUseDet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPointUseDetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pointUseDet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pointUseDet))
            )
            .andExpect(status().isBadRequest());

        // Validate the PointUseDet in the database
        List<PointUseDet> pointUseDetList = pointUseDetRepository.findAll();
        assertThat(pointUseDetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPointUseDet() throws Exception {
        int databaseSizeBeforeUpdate = pointUseDetRepository.findAll().size();
        pointUseDet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointUseDetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pointUseDet))
            )
            .andExpect(status().isBadRequest());

        // Validate the PointUseDet in the database
        List<PointUseDet> pointUseDetList = pointUseDetRepository.findAll();
        assertThat(pointUseDetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPointUseDet() throws Exception {
        int databaseSizeBeforeUpdate = pointUseDetRepository.findAll().size();
        pointUseDet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointUseDetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pointUseDet)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PointUseDet in the database
        List<PointUseDet> pointUseDetList = pointUseDetRepository.findAll();
        assertThat(pointUseDetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePointUseDetWithPatch() throws Exception {
        // Initialize the database
        pointUseDetRepository.saveAndFlush(pointUseDet);

        int databaseSizeBeforeUpdate = pointUseDetRepository.findAll().size();

        // Update the pointUseDet using partial update
        PointUseDet partialUpdatedPointUseDet = new PointUseDet();
        partialUpdatedPointUseDet.setId(pointUseDet.getId());

        partialUpdatedPointUseDet.scoreUsed(UPDATED_SCORE_USED);

        restPointUseDetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPointUseDet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPointUseDet))
            )
            .andExpect(status().isOk());

        // Validate the PointUseDet in the database
        List<PointUseDet> pointUseDetList = pointUseDetRepository.findAll();
        assertThat(pointUseDetList).hasSize(databaseSizeBeforeUpdate);
        PointUseDet testPointUseDet = pointUseDetList.get(pointUseDetList.size() - 1);
        assertThat(testPointUseDet.getScoreUsed()).isEqualTo(UPDATED_SCORE_USED);
    }

    @Test
    @Transactional
    void fullUpdatePointUseDetWithPatch() throws Exception {
        // Initialize the database
        pointUseDetRepository.saveAndFlush(pointUseDet);

        int databaseSizeBeforeUpdate = pointUseDetRepository.findAll().size();

        // Update the pointUseDet using partial update
        PointUseDet partialUpdatedPointUseDet = new PointUseDet();
        partialUpdatedPointUseDet.setId(pointUseDet.getId());

        partialUpdatedPointUseDet.scoreUsed(UPDATED_SCORE_USED);

        restPointUseDetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPointUseDet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPointUseDet))
            )
            .andExpect(status().isOk());

        // Validate the PointUseDet in the database
        List<PointUseDet> pointUseDetList = pointUseDetRepository.findAll();
        assertThat(pointUseDetList).hasSize(databaseSizeBeforeUpdate);
        PointUseDet testPointUseDet = pointUseDetList.get(pointUseDetList.size() - 1);
        assertThat(testPointUseDet.getScoreUsed()).isEqualTo(UPDATED_SCORE_USED);
    }

    @Test
    @Transactional
    void patchNonExistingPointUseDet() throws Exception {
        int databaseSizeBeforeUpdate = pointUseDetRepository.findAll().size();
        pointUseDet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPointUseDetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pointUseDet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pointUseDet))
            )
            .andExpect(status().isBadRequest());

        // Validate the PointUseDet in the database
        List<PointUseDet> pointUseDetList = pointUseDetRepository.findAll();
        assertThat(pointUseDetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPointUseDet() throws Exception {
        int databaseSizeBeforeUpdate = pointUseDetRepository.findAll().size();
        pointUseDet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointUseDetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pointUseDet))
            )
            .andExpect(status().isBadRequest());

        // Validate the PointUseDet in the database
        List<PointUseDet> pointUseDetList = pointUseDetRepository.findAll();
        assertThat(pointUseDetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPointUseDet() throws Exception {
        int databaseSizeBeforeUpdate = pointUseDetRepository.findAll().size();
        pointUseDet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointUseDetMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pointUseDet))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PointUseDet in the database
        List<PointUseDet> pointUseDetList = pointUseDetRepository.findAll();
        assertThat(pointUseDetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePointUseDet() throws Exception {
        // Initialize the database
        pointUseDetRepository.saveAndFlush(pointUseDet);

        int databaseSizeBeforeDelete = pointUseDetRepository.findAll().size();

        // Delete the pointUseDet
        restPointUseDetMockMvc
            .perform(delete(ENTITY_API_URL_ID, pointUseDet.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PointUseDet> pointUseDetList = pointUseDetRepository.findAll();
        assertThat(pointUseDetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
