package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ExpirationPoint;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ExpirationPoint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExpirationPointRepository extends JpaRepository<ExpirationPoint, Long> {}
