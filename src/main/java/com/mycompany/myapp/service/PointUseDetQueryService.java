package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.PointUseDet;
import com.mycompany.myapp.repository.PointUseDetRepository;
import com.mycompany.myapp.service.criteria.PointUseDetCriteria;
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
 * Service for executing complex queries for {@link PointUseDet} entities in the database.
 * The main input is a {@link PointUseDetCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PointUseDet} or a {@link Page} of {@link PointUseDet} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PointUseDetQueryService extends QueryService<PointUseDet> {

    private final Logger log = LoggerFactory.getLogger(PointUseDetQueryService.class);

    private final PointUseDetRepository pointUseDetRepository;

    public PointUseDetQueryService(PointUseDetRepository pointUseDetRepository) {
        this.pointUseDetRepository = pointUseDetRepository;
    }

    /**
     * Return a {@link List} of {@link PointUseDet} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PointUseDet> findByCriteria(PointUseDetCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PointUseDet> specification = createSpecification(criteria);
        return pointUseDetRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PointUseDet} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PointUseDet> findByCriteria(PointUseDetCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PointUseDet> specification = createSpecification(criteria);
        return pointUseDetRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PointUseDetCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PointUseDet> specification = createSpecification(criteria);
        return pointUseDetRepository.count(specification);
    }

    /**
     * Function to convert {@link PointUseDetCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PointUseDet> createSpecification(PointUseDetCriteria criteria) {
        Specification<PointUseDet> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PointUseDet_.id));
            }
            if (criteria.getScoreUsed() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getScoreUsed(), PointUseDet_.scoreUsed));
            }
            if (criteria.getPointUseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPointUseId(),
                            root -> root.join(PointUseDet_.pointUse, JoinType.LEFT).get(PointUse_.id)
                        )
                    );
            }
            if (criteria.getBagOfPointId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBagOfPointId(),
                            root -> root.join(PointUseDet_.bagOfPoint, JoinType.LEFT).get(BagOfPoint_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
