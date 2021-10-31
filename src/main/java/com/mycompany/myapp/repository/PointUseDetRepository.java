package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PointUseDet;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PointUseDet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PointUseDetRepository extends JpaRepository<PointUseDet, Long>, JpaSpecificationExecutor<PointUseDet> {}
