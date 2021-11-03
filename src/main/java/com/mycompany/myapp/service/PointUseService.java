package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.PointUse;
import com.mycompany.myapp.repository.PointUseRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PointUse}.
 */
@Service
@Transactional
public class PointUseService {

    private final Logger log = LoggerFactory.getLogger(PointUseService.class);

    private final PointUseRepository pointUseRepository;

    public PointUseService(PointUseRepository pointUseRepository) {
        this.pointUseRepository = pointUseRepository;
    }

    /**
     * Save a pointUse.
     *
     * @param pointUse the entity to save.
     * @return the persisted entity.
     */
    public PointUse save(PointUse pointUse) {
        log.debug("Request to save PointUse : {}", pointUse);
        return pointUseRepository.save(pointUse);
    }

    /**
     * Partially update a pointUse.
     *
     * @param pointUse the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PointUse> partialUpdate(PointUse pointUse) {
        log.debug("Request to partially update PointUse : {}", pointUse);

        return pointUseRepository
            .findById(pointUse.getId())
            .map(existingPointUse -> {
                if (pointUse.getScoreUsed() != null) {
                    existingPointUse.setScoreUsed(pointUse.getScoreUsed());
                }
                if (pointUse.getEventDate() != null) {
                    existingPointUse.setEventDate(pointUse.getEventDate());
                }

                return existingPointUse;
            })
            .map(pointUseRepository::save);
    }

    /**
     * Get all the pointUses.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PointUse> findAll() {
        log.debug("Request to get all PointUses");
        return pointUseRepository.findAll();
    }

    /**
     * Get one pointUse by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PointUse> findOne(Long id) {
        log.debug("Request to get PointUse : {}", id);
        return pointUseRepository.findById(id);
    }

    /**
     * Delete the pointUse by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PointUse : {}", id);
        pointUseRepository.deleteById(id);
    }


}
