package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.BagOfPoint;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BagOfPoint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BagOfPointRepository extends JpaRepository<BagOfPoint, Long>, JpaSpecificationExecutor<BagOfPoint> {}
