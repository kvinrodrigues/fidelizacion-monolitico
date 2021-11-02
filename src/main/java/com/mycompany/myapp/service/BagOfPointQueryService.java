package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.BagOfPoint;
import com.mycompany.myapp.repository.BagOfPointRepository;
import com.mycompany.myapp.service.criteria.BagOfPointCriteria;
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
 * Service for executing complex queries for {@link BagOfPoint} entities in the database.
 * The main input is a {@link BagOfPointCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BagOfPoint} or a {@link Page} of {@link BagOfPoint} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BagOfPointQueryService extends QueryService<BagOfPoint> {

    private final Logger log = LoggerFactory.getLogger(BagOfPointQueryService.class);

    private final BagOfPointRepository bagOfPointRepository;

    public BagOfPointQueryService(BagOfPointRepository bagOfPointRepository) {
        this.bagOfPointRepository = bagOfPointRepository;
    }

    /**
     * Return a {@link List} of {@link BagOfPoint} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BagOfPoint> findByCriteria(BagOfPointCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BagOfPoint> specification = createSpecification(criteria);
        return bagOfPointRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link BagOfPoint} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BagOfPoint> findByCriteria(BagOfPointCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BagOfPoint> specification = createSpecification(criteria);
        return bagOfPointRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BagOfPointCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BagOfPoint> specification = createSpecification(criteria);
        return bagOfPointRepository.count(specification);
    }

    /**
     * Function to convert {@link BagOfPointCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BagOfPoint> createSpecification(BagOfPointCriteria criteria) {
        Specification<BagOfPoint> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BagOfPoint_.id));
            }
            if (criteria.getAsignationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAsignationDate(), BagOfPoint_.asignationDate));
            }
            if (criteria.getExpirationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExpirationDate(), BagOfPoint_.expirationDate));
            }
            if (criteria.getAssignedScore() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAssignedScore(), BagOfPoint_.assignedScore));
            }
            if (criteria.getScoreUsed() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getScoreUsed(), BagOfPoint_.scoreUsed));
            }
            if (criteria.getScoreBalance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getScoreBalance(), BagOfPoint_.scoreBalance));
            }
            if (criteria.getOperationAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOperationAmount(), BagOfPoint_.operationAmount));
            }
            if (criteria.getState() != null) {
                specification = specification.and(buildStringSpecification(criteria.getState(), BagOfPoint_.state));
            }
            if (criteria.getPointUseDetailId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPointUseDetailId(),
                            root -> root.join(BagOfPoint_.pointUseDetails, JoinType.LEFT).get(PointUseDet_.id)
                        )
                    );
            }
            if (criteria.getClientId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getClientId(), root -> root.join(BagOfPoint_.client, JoinType.LEFT).get(Client_.id))
                    );
            }
        }
        return specification;
    }
}
