package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.ExpirationPoint;
import com.mycompany.myapp.repository.ExpirationPointRepository;
import com.mycompany.myapp.service.criteria.ExpirationPointCriteria;
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
 * Service for executing complex queries for {@link ExpirationPoint} entities in the database.
 * The main input is a {@link ExpirationPointCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ExpirationPoint} or a {@link Page} of {@link ExpirationPoint} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExpirationPointQueryService extends QueryService<ExpirationPoint> {

    private final Logger log = LoggerFactory.getLogger(ExpirationPointQueryService.class);

    private final ExpirationPointRepository expirationPointRepository;

    public ExpirationPointQueryService(ExpirationPointRepository expirationPointRepository) {
        this.expirationPointRepository = expirationPointRepository;
    }

    /**
     * Return a {@link List} of {@link ExpirationPoint} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ExpirationPoint> findByCriteria(ExpirationPointCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ExpirationPoint> specification = createSpecification(criteria);
        return expirationPointRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ExpirationPoint} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExpirationPoint> findByCriteria(ExpirationPointCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ExpirationPoint> specification = createSpecification(criteria);
        return expirationPointRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExpirationPointCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ExpirationPoint> specification = createSpecification(criteria);
        return expirationPointRepository.count(specification);
    }

    /**
     * Function to convert {@link ExpirationPointCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ExpirationPoint> createSpecification(ExpirationPointCriteria criteria) {
        Specification<ExpirationPoint> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ExpirationPoint_.id));
            }
            if (criteria.getValidityStartDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getValidityStartDate(), ExpirationPoint_.validityStartDate));
            }
            if (criteria.getValidityEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValidityEndDate(), ExpirationPoint_.validityEndDate));
            }
            if (criteria.getScoreDurationDays() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getScoreDurationDays(), ExpirationPoint_.scoreDurationDays));
            }
        }
        return specification;
    }
}
