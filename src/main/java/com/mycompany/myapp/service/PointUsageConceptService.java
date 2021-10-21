package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.PointUsageConcept;
import com.mycompany.myapp.repository.PointUsageConceptRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PointUsageConcept}.
 */
@Service
@Transactional
public class PointUsageConceptService {

    private final Logger log = LoggerFactory.getLogger(PointUsageConceptService.class);

    private final PointUsageConceptRepository pointUsageConceptRepository;

    public PointUsageConceptService(PointUsageConceptRepository pointUsageConceptRepository) {
        this.pointUsageConceptRepository = pointUsageConceptRepository;
    }

    /**
     * Save a pointUsageConcept.
     *
     * @param pointUsageConcept the entity to save.
     * @return the persisted entity.
     */
    public PointUsageConcept save(PointUsageConcept pointUsageConcept) {
        log.debug("Request to save PointUsageConcept : {}", pointUsageConcept);
        return pointUsageConceptRepository.save(pointUsageConcept);
    }

    /**
     * Partially update a pointUsageConcept.
     *
     * @param pointUsageConcept the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PointUsageConcept> partialUpdate(PointUsageConcept pointUsageConcept) {
        log.debug("Request to partially update PointUsageConcept : {}", pointUsageConcept);

        return pointUsageConceptRepository
            .findById(pointUsageConcept.getId())
            .map(existingPointUsageConcept -> {
                if (pointUsageConcept.getDescription() != null) {
                    existingPointUsageConcept.setDescription(pointUsageConcept.getDescription());
                }
                if (pointUsageConcept.getRequiredPoints() != null) {
                    existingPointUsageConcept.setRequiredPoints(pointUsageConcept.getRequiredPoints());
                }

                return existingPointUsageConcept;
            })
            .map(pointUsageConceptRepository::save);
    }

    /**
     * Get all the pointUsageConcepts.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PointUsageConcept> findAll() {
        log.debug("Request to get all PointUsageConcepts");
        return pointUsageConceptRepository.findAll();
    }

    /**
     * Get one pointUsageConcept by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PointUsageConcept> findOne(Long id) {
        log.debug("Request to get PointUsageConcept : {}", id);
        return pointUsageConceptRepository.findById(id);
    }

    /**
     * Delete the pointUsageConcept by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PointUsageConcept : {}", id);
        pointUsageConceptRepository.deleteById(id);
    }
}
