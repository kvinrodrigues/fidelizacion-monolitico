package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.PointAllocationRule;
import com.mycompany.myapp.repository.PointAllocationRuleRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PointAllocationRule}.
 */
@Service
@Transactional
public class PointAllocationRuleService {

    private final Logger log = LoggerFactory.getLogger(PointAllocationRuleService.class);

    private final PointAllocationRuleRepository pointAllocationRuleRepository;

    public PointAllocationRuleService(PointAllocationRuleRepository pointAllocationRuleRepository) {
        this.pointAllocationRuleRepository = pointAllocationRuleRepository;
    }

    /**
     * Save a pointAllocationRule.
     *
     * @param pointAllocationRule the entity to save.
     * @return the persisted entity.
     */
    public PointAllocationRule save(PointAllocationRule pointAllocationRule) {
        log.debug("Request to save PointAllocationRule : {}", pointAllocationRule);
        return pointAllocationRuleRepository.save(pointAllocationRule);
    }

    /**
     * Partially update a pointAllocationRule.
     *
     * @param pointAllocationRule the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PointAllocationRule> partialUpdate(PointAllocationRule pointAllocationRule) {
        log.debug("Request to partially update PointAllocationRule : {}", pointAllocationRule);

        return pointAllocationRuleRepository
            .findById(pointAllocationRule.getId())
            .map(existingPointAllocationRule -> {
                if (pointAllocationRule.getLowerLimit() != null) {
                    existingPointAllocationRule.setLowerLimit(pointAllocationRule.getLowerLimit());
                }
                if (pointAllocationRule.getUpperLimit() != null) {
                    existingPointAllocationRule.setUpperLimit(pointAllocationRule.getUpperLimit());
                }
                if (pointAllocationRule.getEquivalenceOfAPoint() != null) {
                    existingPointAllocationRule.setEquivalenceOfAPoint(pointAllocationRule.getEquivalenceOfAPoint());
                }

                return existingPointAllocationRule;
            })
            .map(pointAllocationRuleRepository::save);
    }

    /**
     * Get all the pointAllocationRules.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PointAllocationRule> findAll() {
        log.debug("Request to get all PointAllocationRules");
        return pointAllocationRuleRepository.findAll();
    }

    /**
     * Get one pointAllocationRule by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PointAllocationRule> findOne(Long id) {
        log.debug("Request to get PointAllocationRule : {}", id);
        return pointAllocationRuleRepository.findById(id);
    }

    /**
     * Delete the pointAllocationRule by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PointAllocationRule : {}", id);
        pointAllocationRuleRepository.deleteById(id);
    }
}
