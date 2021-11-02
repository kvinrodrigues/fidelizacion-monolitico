package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.PointUse;
import com.mycompany.myapp.repository.PointUseRepository;
import com.mycompany.myapp.service.criteria.PointUseCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link PointUse} entities in the database.
 * The main input is a {@link PointUseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PointUse} or a {@link Page} of {@link PointUse} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PointUseQueryService extends QueryService<PointUse> {

    private final Logger log = LoggerFactory.getLogger(PointUseQueryService.class);

    private final PointUseRepository pointUseRepository;

    public PointUseQueryService(PointUseRepository pointUseRepository) {
        this.pointUseRepository = pointUseRepository;
    }

    /**
     * Return a {@link List} of {@link PointUse} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PointUse> findByCriteria(PointUseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PointUse> specification = createSpecification(criteria);
        return pointUseRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PointUse} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PointUse> findByCriteria(PointUseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PointUse> specification = createSpecification(criteria);
        return pointUseRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PointUseCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PointUse> specification = createSpecification(criteria);
        return pointUseRepository.count(specification);
    }

    /**
     * Function to convert {@link PointUseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PointUse> createSpecification(PointUseCriteria criteria) {
        Specification<PointUse> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PointUse_.id));
            }
            if (criteria.getScoreUsed() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getScoreUsed(), PointUse_.scoreUsed));
            }
            if (criteria.getEventDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEventDate(), PointUse_.eventDate));
            }
            if (criteria.getPointUseDetailId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPointUseDetailId(),
                            root -> root.join(PointUse_.pointUseDetails, JoinType.LEFT).get(PointUseDet_.id)
                        )
                    );
            }
            if (criteria.getClientId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getClientId(), root -> root.join(PointUse_.client, JoinType.LEFT).get(Client_.id))
                    );
            }
            if (criteria.getPointUsageConceptId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPointUsageConceptId(),
                            root -> root.join(PointUse_.pointUsageConcept, JoinType.LEFT).get(PointUsageConcept_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
