package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.PointUseDet;
import com.mycompany.myapp.repository.PointUseDetRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PointUseDet}.
 */
@Service
@Transactional
public class PointUseDetService {

    private final Logger log = LoggerFactory.getLogger(PointUseDetService.class);

    private final PointUseDetRepository pointUseDetRepository;

    public PointUseDetService(PointUseDetRepository pointUseDetRepository) {
        this.pointUseDetRepository = pointUseDetRepository;
    }

    /**
     * Save a pointUseDet.
     *
     * @param pointUseDet the entity to save.
     * @return the persisted entity.
     */
    public PointUseDet save(PointUseDet pointUseDet) {
        log.debug("Request to save PointUseDet : {}", pointUseDet);
        return pointUseDetRepository.save(pointUseDet);
    }

    /**
     * Partially update a pointUseDet.
     *
     * @param pointUseDet the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PointUseDet> partialUpdate(PointUseDet pointUseDet) {
        log.debug("Request to partially update PointUseDet : {}", pointUseDet);

        return pointUseDetRepository
            .findById(pointUseDet.getId())
            .map(existingPointUseDet -> {
                if (pointUseDet.getScoreUsed() != null) {
                    existingPointUseDet.setScoreUsed(pointUseDet.getScoreUsed());
                }

                return existingPointUseDet;
            })
            .map(pointUseDetRepository::save);
    }

    /**
     * Get all the pointUseDets.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PointUseDet> findAll() {
        log.debug("Request to get all PointUseDets");
        return pointUseDetRepository.findAll();
    }

    /**
     * Get one pointUseDet by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PointUseDet> findOne(Long id) {
        log.debug("Request to get PointUseDet : {}", id);
        return pointUseDetRepository.findById(id);
    }

    /**
     * Delete the pointUseDet by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PointUseDet : {}", id);
        pointUseDetRepository.deleteById(id);
    }
}
