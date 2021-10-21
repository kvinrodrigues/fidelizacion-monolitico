package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.PointUseDet;
import com.mycompany.myapp.repository.PointUseDetRepository;
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
