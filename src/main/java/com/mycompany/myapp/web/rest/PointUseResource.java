package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.PointUse;
import com.mycompany.myapp.repository.PointUseRepository;
import com.mycompany.myapp.service.PointUseQueryService;
import com.mycompany.myapp.service.PointUseService;
import com.mycompany.myapp.service.criteria.PointUseCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.PointUse}.
 */
@RestController
@RequestMapping("/api")
public class PointUseResource {

    private final Logger log = LoggerFactory.getLogger(PointUseResource.class);

    private static final String ENTITY_NAME = "pointUse";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PointUseService pointUseService;

    private final PointUseRepository pointUseRepository;

    private final PointUseQueryService pointUseQueryService;

    public PointUseResource(
        PointUseService pointUseService,
        PointUseRepository pointUseRepository,
        PointUseQueryService pointUseQueryService
    ) {
        this.pointUseService = pointUseService;
        this.pointUseRepository = pointUseRepository;
        this.pointUseQueryService = pointUseQueryService;
    }

    /**
     * {@code POST  /point-uses} : Create a new pointUse.
     *
     * @param pointUse the pointUse to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pointUse, or with status {@code 400 (Bad Request)} if the pointUse has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/point-uses")
    public ResponseEntity<PointUse> createPointUse(@Valid @RequestBody PointUse pointUse) throws URISyntaxException {
        log.debug("REST request to save PointUse : {}", pointUse);
        if (pointUse.getId() != null) {
            throw new BadRequestAlertException("A new pointUse cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PointUse result = pointUseService.save(pointUse);
        return ResponseEntity
            .created(new URI("/api/point-uses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /point-uses/:id} : Updates an existing pointUse.
     *
     * @param id the id of the pointUse to save.
     * @param pointUse the pointUse to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pointUse,
     * or with status {@code 400 (Bad Request)} if the pointUse is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pointUse couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/point-uses/{id}")
    public ResponseEntity<PointUse> updatePointUse(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PointUse pointUse
    ) throws URISyntaxException {
        log.debug("REST request to update PointUse : {}, {}", id, pointUse);
        if (pointUse.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pointUse.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pointUseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PointUse result = pointUseService.save(pointUse);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pointUse.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /point-uses/:id} : Partial updates given fields of an existing pointUse, field will ignore if it is null
     *
     * @param id the id of the pointUse to save.
     * @param pointUse the pointUse to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pointUse,
     * or with status {@code 400 (Bad Request)} if the pointUse is not valid,
     * or with status {@code 404 (Not Found)} if the pointUse is not found,
     * or with status {@code 500 (Internal Server Error)} if the pointUse couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/point-uses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PointUse> partialUpdatePointUse(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PointUse pointUse
    ) throws URISyntaxException {
        log.debug("REST request to partial update PointUse partially : {}, {}", id, pointUse);
        if (pointUse.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pointUse.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pointUseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PointUse> result = pointUseService.partialUpdate(pointUse);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pointUse.getId().toString())
        );
    }

    /**
     * {@code GET  /point-uses} : get all the pointUses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pointUses in body.
     */
    @GetMapping("/point-uses")
    public ResponseEntity<List<PointUse>> getAllPointUses(PointUseCriteria criteria) {
        log.debug("REST request to get PointUses by criteria: {}", criteria);
        List<PointUse> entityList = pointUseQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /point-uses/count} : count all the pointUses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/point-uses/count")
    public ResponseEntity<Long> countPointUses(PointUseCriteria criteria) {
        log.debug("REST request to count PointUses by criteria: {}", criteria);
        return ResponseEntity.ok().body(pointUseQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /point-uses/:id} : get the "id" pointUse.
     *
     * @param id the id of the pointUse to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pointUse, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/point-uses/{id}")
    public ResponseEntity<PointUse> getPointUse(@PathVariable Long id) {
        log.debug("REST request to get PointUse : {}", id);
        Optional<PointUse> pointUse = pointUseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pointUse);
    }

    /**
     * {@code DELETE  /point-uses/:id} : delete the "id" pointUse.
     *
     * @param id the id of the pointUse to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/point-uses/{id}")
    public ResponseEntity<Void> deletePointUse(@PathVariable Long id) {
        log.debug("REST request to delete PointUse : {}", id);
        pointUseService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
