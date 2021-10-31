package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.PointUseDet;
import com.mycompany.myapp.repository.PointUseDetRepository;
import com.mycompany.myapp.service.PointUseDetQueryService;
import com.mycompany.myapp.service.PointUseDetService;
import com.mycompany.myapp.service.criteria.PointUseDetCriteria;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.PointUseDet}.
 */
@RestController
@RequestMapping("/api")
public class PointUseDetResource {

    private final Logger log = LoggerFactory.getLogger(PointUseDetResource.class);

    private static final String ENTITY_NAME = "pointUseDet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PointUseDetService pointUseDetService;

    private final PointUseDetRepository pointUseDetRepository;

    private final PointUseDetQueryService pointUseDetQueryService;

    public PointUseDetResource(
        PointUseDetService pointUseDetService,
        PointUseDetRepository pointUseDetRepository,
        PointUseDetQueryService pointUseDetQueryService
    ) {
        this.pointUseDetService = pointUseDetService;
        this.pointUseDetRepository = pointUseDetRepository;
        this.pointUseDetQueryService = pointUseDetQueryService;
    }

    /**
     * {@code POST  /point-use-dets} : Create a new pointUseDet.
     *
     * @param pointUseDet the pointUseDet to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pointUseDet, or with status {@code 400 (Bad Request)} if the pointUseDet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/point-use-dets")
    public ResponseEntity<PointUseDet> createPointUseDet(@Valid @RequestBody PointUseDet pointUseDet) throws URISyntaxException {
        log.debug("REST request to save PointUseDet : {}", pointUseDet);
        if (pointUseDet.getId() != null) {
            throw new BadRequestAlertException("A new pointUseDet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PointUseDet result = pointUseDetService.save(pointUseDet);
        return ResponseEntity
            .created(new URI("/api/point-use-dets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /point-use-dets/:id} : Updates an existing pointUseDet.
     *
     * @param id the id of the pointUseDet to save.
     * @param pointUseDet the pointUseDet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pointUseDet,
     * or with status {@code 400 (Bad Request)} if the pointUseDet is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pointUseDet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/point-use-dets/{id}")
    public ResponseEntity<PointUseDet> updatePointUseDet(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PointUseDet pointUseDet
    ) throws URISyntaxException {
        log.debug("REST request to update PointUseDet : {}, {}", id, pointUseDet);
        if (pointUseDet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pointUseDet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pointUseDetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PointUseDet result = pointUseDetService.save(pointUseDet);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pointUseDet.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /point-use-dets/:id} : Partial updates given fields of an existing pointUseDet, field will ignore if it is null
     *
     * @param id the id of the pointUseDet to save.
     * @param pointUseDet the pointUseDet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pointUseDet,
     * or with status {@code 400 (Bad Request)} if the pointUseDet is not valid,
     * or with status {@code 404 (Not Found)} if the pointUseDet is not found,
     * or with status {@code 500 (Internal Server Error)} if the pointUseDet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/point-use-dets/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PointUseDet> partialUpdatePointUseDet(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PointUseDet pointUseDet
    ) throws URISyntaxException {
        log.debug("REST request to partial update PointUseDet partially : {}, {}", id, pointUseDet);
        if (pointUseDet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pointUseDet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pointUseDetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PointUseDet> result = pointUseDetService.partialUpdate(pointUseDet);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pointUseDet.getId().toString())
        );
    }

    /**
     * {@code GET  /point-use-dets} : get all the pointUseDets.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pointUseDets in body.
     */
    @GetMapping("/point-use-dets")
    public ResponseEntity<List<PointUseDet>> getAllPointUseDets(PointUseDetCriteria criteria) {
        log.debug("REST request to get PointUseDets by criteria: {}", criteria);
        List<PointUseDet> entityList = pointUseDetQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /point-use-dets/count} : count all the pointUseDets.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/point-use-dets/count")
    public ResponseEntity<Long> countPointUseDets(PointUseDetCriteria criteria) {
        log.debug("REST request to count PointUseDets by criteria: {}", criteria);
        return ResponseEntity.ok().body(pointUseDetQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /point-use-dets/:id} : get the "id" pointUseDet.
     *
     * @param id the id of the pointUseDet to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pointUseDet, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/point-use-dets/{id}")
    public ResponseEntity<PointUseDet> getPointUseDet(@PathVariable Long id) {
        log.debug("REST request to get PointUseDet : {}", id);
        Optional<PointUseDet> pointUseDet = pointUseDetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pointUseDet);
    }

    /**
     * {@code DELETE  /point-use-dets/:id} : delete the "id" pointUseDet.
     *
     * @param id the id of the pointUseDet to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/point-use-dets/{id}")
    public ResponseEntity<Void> deletePointUseDet(@PathVariable Long id) {
        log.debug("REST request to delete PointUseDet : {}", id);
        pointUseDetService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
