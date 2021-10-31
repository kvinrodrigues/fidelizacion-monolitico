package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Nacionality;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Nacionality entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NacionalityRepository extends JpaRepository<Nacionality, Long>, JpaSpecificationExecutor<Nacionality> {}
