package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.PointUsageConcept;
import com.mycompany.myapp.repository.PointUsageConceptRepository;
import com.mycompany.myapp.service.criteria.PointUsageConceptCriteria;
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
 * Service for executing complex queries for {@link PointUsageConcept} entities in the database.
 * The main input is a {@link PointUsageConceptCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PointUsageConcept} or a {@link Page} of {@link PointUsageConcept} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PointUsageConceptQueryService extends QueryService<PointUsageConcept> {

    private final Logger log = LoggerFactory.getLogger(PointUsageConceptQueryService.class);

    private final PointUsageConceptRepository pointUsageConceptRepository;

    public PointUsageConceptQueryService(PointUsageConceptRepository pointUsageConceptRepository) {
        this.pointUsageConceptRepository = pointUsageConceptRepository;
    }

    /**
     * Return a {@link List} of {@link PointUsageConcept} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PointUsageConcept> findByCriteria(PointUsageConceptCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PointUsageConcept> specification = createSpecification(criteria);
        return pointUsageConceptRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PointUsageConcept} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PointUsageConcept> findByCriteria(PointUsageConceptCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PointUsageConcept> specification = createSpecification(criteria);
        return pointUsageConceptRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PointUsageConceptCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PointUsageConcept> specification = createSpecification(criteria);
        return pointUsageConceptRepository.count(specification);
    }

    /**
     * Function to convert {@link PointUsageConceptCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PointUsageConcept> createSpecification(PointUsageConceptCriteria criteria) {
        Specification<PointUsageConcept> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PointUsageConcept_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), PointUsageConcept_.description));
            }
            if (criteria.getRequiredPoints() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRequiredPoints(), PointUsageConcept_.requiredPoints));
            }
            if (criteria.getPointUseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPointUseId(),
                            root -> root.join(PointUsageConcept_.pointUses, JoinType.LEFT).get(PointUse_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
