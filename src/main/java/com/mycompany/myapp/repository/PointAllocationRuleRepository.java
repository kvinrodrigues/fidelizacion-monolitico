package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PointAllocationRule;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PointAllocationRule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PointAllocationRuleRepository
    extends JpaRepository<PointAllocationRule, Long>, JpaSpecificationExecutor<PointAllocationRule> {}
