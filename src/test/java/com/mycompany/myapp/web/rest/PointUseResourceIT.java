package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.PointUse;
import com.mycompany.myapp.repository.PointUseRepository;
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
 * Integration tests for the {@link PointUseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PointUseResourceIT {

    private static final Long DEFAULT_SCORE_USED = 1L;
    private static final Long UPDATED_SCORE_USED = 2L;

    private static final Instant DEFAULT_EVENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EVENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/point-uses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PointUseRepository pointUseRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPointUseMockMvc;

    private PointUse pointUse;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PointUse createEntity(EntityManager em) {
        PointUse pointUse = new PointUse().scoreUsed(DEFAULT_SCORE_USED).eventDate(DEFAULT_EVENT_DATE);
        return pointUse;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PointUse createUpdatedEntity(EntityManager em) {
        PointUse pointUse = new PointUse().scoreUsed(UPDATED_SCORE_USED).eventDate(UPDATED_EVENT_DATE);
        return pointUse;
    }

    @BeforeEach
    public void initTest() {
        pointUse = createEntity(em);
    }

    @Test
    @Transactional
    void createPointUse() throws Exception {
        int databaseSizeBeforeCreate = pointUseRepository.findAll().size();
        // Create the PointUse
        restPointUseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pointUse)))
            .andExpect(status().isCreated());

        // Validate the PointUse in the database
        List<PointUse> pointUseList = pointUseRepository.findAll();
        assertThat(pointUseList).hasSize(databaseSizeBeforeCreate + 1);
        PointUse testPointUse = pointUseList.get(pointUseList.size() - 1);
        assertThat(testPointUse.getScoreUsed()).isEqualTo(DEFAULT_SCORE_USED);
        assertThat(testPointUse.getEventDate()).isEqualTo(DEFAULT_EVENT_DATE);
    }

    @Test
    @Transactional
    void createPointUseWithExistingId() throws Exception {
        // Create the PointUse with an existing ID
        pointUse.setId(1L);

        int databaseSizeBeforeCreate = pointUseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPointUseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pointUse)))
            .andExpect(status().isBadRequest());

        // Validate the PointUse in the database
        List<PointUse> pointUseList = pointUseRepository.findAll();
        assertThat(pointUseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkScoreUsedIsRequired() throws Exception {
        int databaseSizeBeforeTest = pointUseRepository.findAll().size();
        // set the field null
        pointUse.setScoreUsed(null);

        // Create the PointUse, which fails.

        restPointUseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pointUse)))
            .andExpect(status().isBadRequest());

        List<PointUse> pointUseList = pointUseRepository.findAll();
        assertThat(pointUseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEventDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = pointUseRepository.findAll().size();
        // set the field null
        pointUse.setEventDate(null);

        // Create the PointUse, which fails.

        restPointUseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pointUse)))
            .andExpect(status().isBadRequest());

        List<PointUse> pointUseList = pointUseRepository.findAll();
        assertThat(pointUseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPointUses() throws Exception {
        // Initialize the database
        pointUseRepository.saveAndFlush(pointUse);

        // Get all the pointUseList
        restPointUseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pointUse.getId().intValue())))
            .andExpect(jsonPath("$.[*].scoreUsed").value(hasItem(DEFAULT_SCORE_USED.intValue())))
            .andExpect(jsonPath("$.[*].eventDate").value(hasItem(DEFAULT_EVENT_DATE.toString())));
    }

    @Test
    @Transactional
    void getPointUse() throws Exception {
        // Initialize the database
        pointUseRepository.saveAndFlush(pointUse);

        // Get the pointUse
        restPointUseMockMvc
            .perform(get(ENTITY_API_URL_ID, pointUse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pointUse.getId().intValue()))
            .andExpect(jsonPath("$.scoreUsed").value(DEFAULT_SCORE_USED.intValue()))
            .andExpect(jsonPath("$.eventDate").value(DEFAULT_EVENT_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPointUse() throws Exception {
        // Get the pointUse
        restPointUseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPointUse() throws Exception {
        // Initialize the database
        pointUseRepository.saveAndFlush(pointUse);

        int databaseSizeBeforeUpdate = pointUseRepository.findAll().size();

        // Update the pointUse
        PointUse updatedPointUse = pointUseRepository.findById(pointUse.getId()).get();
        // Disconnect from session so that the updates on updatedPointUse are not directly saved in db
        em.detach(updatedPointUse);
        updatedPointUse.scoreUsed(UPDATED_SCORE_USED).eventDate(UPDATED_EVENT_DATE);

        restPointUseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPointUse.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPointUse))
            )
            .andExpect(status().isOk());

        // Validate the PointUse in the database
        List<PointUse> pointUseList = pointUseRepository.findAll();
        assertThat(pointUseList).hasSize(databaseSizeBeforeUpdate);
        PointUse testPointUse = pointUseList.get(pointUseList.size() - 1);
        assertThat(testPointUse.getScoreUsed()).isEqualTo(UPDATED_SCORE_USED);
        assertThat(testPointUse.getEventDate()).isEqualTo(UPDATED_EVENT_DATE);
    }

    @Test
    @Transactional
    void putNonExistingPointUse() throws Exception {
        int databaseSizeBeforeUpdate = pointUseRepository.findAll().size();
        pointUse.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPointUseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pointUse.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pointUse))
            )
            .andExpect(status().isBadRequest());

        // Validate the PointUse in the database
        List<PointUse> pointUseList = pointUseRepository.findAll();
        assertThat(pointUseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPointUse() throws Exception {
        int databaseSizeBeforeUpdate = pointUseRepository.findAll().size();
        pointUse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointUseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pointUse))
            )
            .andExpect(status().isBadRequest());

        // Validate the PointUse in the database
        List<PointUse> pointUseList = pointUseRepository.findAll();
        assertThat(pointUseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPointUse() throws Exception {
        int databaseSizeBeforeUpdate = pointUseRepository.findAll().size();
        pointUse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointUseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pointUse)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PointUse in the database
        List<PointUse> pointUseList = pointUseRepository.findAll();
        assertThat(pointUseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePointUseWithPatch() throws Exception {
        // Initialize the database
        pointUseRepository.saveAndFlush(pointUse);

        int databaseSizeBeforeUpdate = pointUseRepository.findAll().size();

        // Update the pointUse using partial update
        PointUse partialUpdatedPointUse = new PointUse();
        partialUpdatedPointUse.setId(pointUse.getId());

        partialUpdatedPointUse.scoreUsed(UPDATED_SCORE_USED).eventDate(UPDATED_EVENT_DATE);

        restPointUseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPointUse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPointUse))
            )
            .andExpect(status().isOk());

        // Validate the PointUse in the database
        List<PointUse> pointUseList = pointUseRepository.findAll();
        assertThat(pointUseList).hasSize(databaseSizeBeforeUpdate);
        PointUse testPointUse = pointUseList.get(pointUseList.size() - 1);
        assertThat(testPointUse.getScoreUsed()).isEqualTo(UPDATED_SCORE_USED);
        assertThat(testPointUse.getEventDate()).isEqualTo(UPDATED_EVENT_DATE);
    }

    @Test
    @Transactional
    void fullUpdatePointUseWithPatch() throws Exception {
        // Initialize the database
        pointUseRepository.saveAndFlush(pointUse);

        int databaseSizeBeforeUpdate = pointUseRepository.findAll().size();

        // Update the pointUse using partial update
        PointUse partialUpdatedPointUse = new PointUse();
        partialUpdatedPointUse.setId(pointUse.getId());

        partialUpdatedPointUse.scoreUsed(UPDATED_SCORE_USED).eventDate(UPDATED_EVENT_DATE);

        restPointUseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPointUse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPointUse))
            )
            .andExpect(status().isOk());

        // Validate the PointUse in the database
        List<PointUse> pointUseList = pointUseRepository.findAll();
        assertThat(pointUseList).hasSize(databaseSizeBeforeUpdate);
        PointUse testPointUse = pointUseList.get(pointUseList.size() - 1);
        assertThat(testPointUse.getScoreUsed()).isEqualTo(UPDATED_SCORE_USED);
        assertThat(testPointUse.getEventDate()).isEqualTo(UPDATED_EVENT_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingPointUse() throws Exception {
        int databaseSizeBeforeUpdate = pointUseRepository.findAll().size();
        pointUse.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPointUseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pointUse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pointUse))
            )
            .andExpect(status().isBadRequest());

        // Validate the PointUse in the database
        List<PointUse> pointUseList = pointUseRepository.findAll();
        assertThat(pointUseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPointUse() throws Exception {
        int databaseSizeBeforeUpdate = pointUseRepository.findAll().size();
        pointUse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointUseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pointUse))
            )
            .andExpect(status().isBadRequest());

        // Validate the PointUse in the database
        List<PointUse> pointUseList = pointUseRepository.findAll();
        assertThat(pointUseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPointUse() throws Exception {
        int databaseSizeBeforeUpdate = pointUseRepository.findAll().size();
        pointUse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointUseMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pointUse)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PointUse in the database
        List<PointUse> pointUseList = pointUseRepository.findAll();
        assertThat(pointUseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePointUse() throws Exception {
        // Initialize the database
        pointUseRepository.saveAndFlush(pointUse);

        int databaseSizeBeforeDelete = pointUseRepository.findAll().size();

        // Delete the pointUse
        restPointUseMockMvc
            .perform(delete(ENTITY_API_URL_ID, pointUse.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PointUse> pointUseList = pointUseRepository.findAll();
        assertThat(pointUseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
