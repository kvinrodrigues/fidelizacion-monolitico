package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PointUsageConcept;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PointUsageConcept entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PointUsageConceptRepository extends JpaRepository<PointUsageConcept, Long>, JpaSpecificationExecutor<PointUsageConcept> {}
