package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PointUse;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PointUse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PointUseRepository extends JpaRepository<PointUse, Long>, JpaSpecificationExecutor<PointUse> {}
