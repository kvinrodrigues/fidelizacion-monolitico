package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Nacionality;
import com.mycompany.myapp.repository.NacionalityRepository;
import com.mycompany.myapp.service.criteria.NacionalityCriteria;
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
 * Service for executing complex queries for {@link Nacionality} entities in the database.
 * The main input is a {@link NacionalityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Nacionality} or a {@link Page} of {@link Nacionality} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NacionalityQueryService extends QueryService<Nacionality> {

    private final Logger log = LoggerFactory.getLogger(NacionalityQueryService.class);

    private final NacionalityRepository nacionalityRepository;

    public NacionalityQueryService(NacionalityRepository nacionalityRepository) {
        this.nacionalityRepository = nacionalityRepository;
    }

    /**
     * Return a {@link List} of {@link Nacionality} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Nacionality> findByCriteria(NacionalityCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Nacionality> specification = createSpecification(criteria);
        return nacionalityRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Nacionality} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Nacionality> findByCriteria(NacionalityCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Nacionality> specification = createSpecification(criteria);
        return nacionalityRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NacionalityCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Nacionality> specification = createSpecification(criteria);
        return nacionalityRepository.count(specification);
    }

    /**
     * Function to convert {@link NacionalityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Nacionality> createSpecification(NacionalityCriteria criteria) {
        Specification<Nacionality> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Nacionality_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Nacionality_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Nacionality_.description));
            }
            if (criteria.getClientId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getClientId(), root -> root.join(Nacionality_.clients, JoinType.LEFT).get(Client_.id))
                    );
            }
        }
        return specification;
    }
}
