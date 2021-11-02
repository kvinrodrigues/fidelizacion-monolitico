package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Client;
import com.mycompany.myapp.repository.ClientRepository;
import com.mycompany.myapp.service.criteria.ClientCriteria;
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
 * Service for executing complex queries for {@link Client} entities in the database.
 * The main input is a {@link ClientCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Client} or a {@link Page} of {@link Client} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClientQueryService extends QueryService<Client> {

    private final Logger log = LoggerFactory.getLogger(ClientQueryService.class);

    private final ClientRepository clientRepository;

    public ClientQueryService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /**
     * Return a {@link List} of {@link Client} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Client> findByCriteria(ClientCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Client> specification = createSpecification(criteria);
        return clientRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Client} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Client> findByCriteria(ClientCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Client> specification = createSpecification(criteria);
        return clientRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ClientCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Client> specification = createSpecification(criteria);
        return clientRepository.count(specification);
    }

    /**
     * Function to convert {@link ClientCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Client> createSpecification(ClientCriteria criteria) {
        Specification<Client> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Client_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Client_.name));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Client_.lastName));
            }
            if (criteria.getDocumentNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDocumentNumber(), Client_.documentNumber));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Client_.email));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), Client_.phoneNumber));
            }
            if (criteria.getBirthDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBirthDate(), Client_.birthDate));
            }
            if (criteria.getBagOfPointId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBagOfPointId(),
                            root -> root.join(Client_.bagOfPoints, JoinType.LEFT).get(BagOfPoint_.id)
                        )
                    );
            }
            if (criteria.getPointUseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPointUseId(), root -> root.join(Client_.pointUses, JoinType.LEFT).get(PointUse_.id))
                    );
            }
            if (criteria.getDocumentTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDocumentTypeId(),
                            root -> root.join(Client_.documentType, JoinType.LEFT).get(DocumentType_.id)
                        )
                    );
            }
            if (criteria.getNacionalityId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getNacionalityId(),
                            root -> root.join(Client_.nacionality, JoinType.LEFT).get(Nacionality_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
