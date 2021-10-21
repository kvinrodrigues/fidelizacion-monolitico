package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.ExpirationPoint;
import com.mycompany.myapp.repository.ExpirationPointRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ExpirationPoint}.
 */
@Service
@Transactional
public class ExpirationPointService {

    private final Logger log = LoggerFactory.getLogger(ExpirationPointService.class);

    private final ExpirationPointRepository expirationPointRepository;

    public ExpirationPointService(ExpirationPointRepository expirationPointRepository) {
        this.expirationPointRepository = expirationPointRepository;
    }

    /**
     * Save a expirationPoint.
     *
     * @param expirationPoint the entity to save.
     * @return the persisted entity.
     */
    public ExpirationPoint save(ExpirationPoint expirationPoint) {
        log.debug("Request to save ExpirationPoint : {}", expirationPoint);
        return expirationPointRepository.save(expirationPoint);
    }

    /**
     * Partially update a expirationPoint.
     *
     * @param expirationPoint the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ExpirationPoint> partialUpdate(ExpirationPoint expirationPoint) {
        log.debug("Request to partially update ExpirationPoint : {}", expirationPoint);

        return expirationPointRepository
            .findById(expirationPoint.getId())
            .map(existingExpirationPoint -> {
                if (expirationPoint.getValidityStartDate() != null) {
                    existingExpirationPoint.setValidityStartDate(expirationPoint.getValidityStartDate());
                }
                if (expirationPoint.getValidityEndDate() != null) {
                    existingExpirationPoint.setValidityEndDate(expirationPoint.getValidityEndDate());
                }
                if (expirationPoint.getScoreDurationDays() != null) {
                    existingExpirationPoint.setScoreDurationDays(expirationPoint.getScoreDurationDays());
                }

                return existingExpirationPoint;
            })
            .map(expirationPointRepository::save);
    }

    /**
     * Get all the expirationPoints.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ExpirationPoint> findAll() {
        log.debug("Request to get all ExpirationPoints");
        return expirationPointRepository.findAll();
    }

    /**
     * Get one expirationPoint by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ExpirationPoint> findOne(Long id) {
        log.debug("Request to get ExpirationPoint : {}", id);
        return expirationPointRepository.findById(id);
    }

    /**
     * Delete the expirationPoint by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ExpirationPoint : {}", id);
        expirationPointRepository.deleteById(id);
    }
}
