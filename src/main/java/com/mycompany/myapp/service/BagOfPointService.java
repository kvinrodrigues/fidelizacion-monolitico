package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.BagOfPoint;
import com.mycompany.myapp.repository.BagOfPointRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BagOfPoint}.
 */
@Service
@Transactional
public class BagOfPointService {

    private final Logger log = LoggerFactory.getLogger(BagOfPointService.class);

    private final BagOfPointRepository bagOfPointRepository;

    public BagOfPointService(BagOfPointRepository bagOfPointRepository) {
        this.bagOfPointRepository = bagOfPointRepository;
    }

    /**
     * Save a bagOfPoint.
     *
     * @param bagOfPoint the entity to save.
     * @return the persisted entity.
     */
    public BagOfPoint save(BagOfPoint bagOfPoint) {
        log.debug("Request to save BagOfPoint : {}", bagOfPoint);
        return bagOfPointRepository.save(bagOfPoint);
    }

    /**
     * Save a bagOfPoint.
     *
     * @param bagOfPoints the entities to save.
     * @return the persisted entities.
     */
    public List<BagOfPoint> saveAll(List<BagOfPoint> bagOfPoints) {
        log.debug("Request to save BagOfPoint : {}", bagOfPoints);
        return bagOfPointRepository.saveAll(bagOfPoints);
    }

    /**
     * Partially update a bagOfPoint.
     *
     * @param bagOfPoint the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BagOfPoint> partialUpdate(BagOfPoint bagOfPoint) {
        log.debug("Request to partially update BagOfPoint : {}", bagOfPoint);

        return bagOfPointRepository
            .findById(bagOfPoint.getId())
            .map(existingBagOfPoint -> {
                if (bagOfPoint.getAsignationDate() != null) {
                    existingBagOfPoint.setAsignationDate(bagOfPoint.getAsignationDate());
                }
                if (bagOfPoint.getExpirationDate() != null) {
                    existingBagOfPoint.setExpirationDate(bagOfPoint.getExpirationDate());
                }
                if (bagOfPoint.getAssignedScore() != null) {
                    existingBagOfPoint.setAssignedScore(bagOfPoint.getAssignedScore());
                }
                if (bagOfPoint.getScoreUsed() != null) {
                    existingBagOfPoint.setScoreUsed(bagOfPoint.getScoreUsed());
                }
                if (bagOfPoint.getScoreBalance() != null) {
                    existingBagOfPoint.setScoreBalance(bagOfPoint.getScoreBalance());
                }
                if (bagOfPoint.getOperationAmount() != null) {
                    existingBagOfPoint.setOperationAmount(bagOfPoint.getOperationAmount());
                }

                return existingBagOfPoint;
            })
            .map(bagOfPointRepository::save);
    }

    /**
     * Get all the bagOfPoints.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BagOfPoint> findAll() {
        log.debug("Request to get all BagOfPoints");
        return bagOfPointRepository.findAll();
    }

    /**
     * Get one bagOfPoint by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BagOfPoint> findOne(Long id) {
        log.debug("Request to get BagOfPoint : {}", id);
        return bagOfPointRepository.findById(id);
    }

    /**
     * Delete the bagOfPoint by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BagOfPoint : {}", id);
        bagOfPointRepository.deleteById(id);
    }
}
