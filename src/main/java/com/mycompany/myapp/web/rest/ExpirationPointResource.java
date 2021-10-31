package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ExpirationPoint;
import com.mycompany.myapp.repository.ExpirationPointRepository;
import com.mycompany.myapp.service.ExpirationPointQueryService;
import com.mycompany.myapp.service.ExpirationPointService;
import com.mycompany.myapp.service.criteria.ExpirationPointCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ExpirationPoint}.
 */
@RestController
@RequestMapping("/api")
public class ExpirationPointResource {

    private final Logger log = LoggerFactory.getLogger(ExpirationPointResource.class);

    private static final String ENTITY_NAME = "expirationPoint";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExpirationPointService expirationPointService;

    private final ExpirationPointRepository expirationPointRepository;

    private final ExpirationPointQueryService expirationPointQueryService;

    public ExpirationPointResource(
        ExpirationPointService expirationPointService,
        ExpirationPointRepository expirationPointRepository,
        ExpirationPointQueryService expirationPointQueryService
    ) {
        this.expirationPointService = expirationPointService;
        this.expirationPointRepository = expirationPointRepository;
        this.expirationPointQueryService = expirationPointQueryService;
    }

    /**
     * {@code POST  /expiration-points} : Create a new expirationPoint.
     *
     * @param expirationPoint the expirationPoint to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new expirationPoint, or with status {@code 400 (Bad Request)} if the expirationPoint has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/expiration-points")
    public ResponseEntity<ExpirationPoint> createExpirationPoint(@Valid @RequestBody ExpirationPoint expirationPoint)
        throws URISyntaxException {
        log.debug("REST request to save ExpirationPoint : {}", expirationPoint);
        if (expirationPoint.getId() != null) {
            throw new BadRequestAlertException("A new expirationPoint cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExpirationPoint result = expirationPointService.save(expirationPoint);
        return ResponseEntity
            .created(new URI("/api/expiration-points/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /expiration-points/:id} : Updates an existing expirationPoint.
     *
     * @param id the id of the expirationPoint to save.
     * @param expirationPoint the expirationPoint to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated expirationPoint,
     * or with status {@code 400 (Bad Request)} if the expirationPoint is not valid,
     * or with status {@code 500 (Internal Server Error)} if the expirationPoint couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/expiration-points/{id}")
    public ResponseEntity<ExpirationPoint> updateExpirationPoint(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExpirationPoint expirationPoint
    ) throws URISyntaxException {
        log.debug("REST request to update ExpirationPoint : {}, {}", id, expirationPoint);
        if (expirationPoint.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, expirationPoint.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!expirationPointRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ExpirationPoint result = expirationPointService.save(expirationPoint);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, expirationPoint.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /expiration-points/:id} : Partial updates given fields of an existing expirationPoint, field will ignore if it is null
     *
     * @param id the id of the expirationPoint to save.
     * @param expirationPoint the expirationPoint to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated expirationPoint,
     * or with status {@code 400 (Bad Request)} if the expirationPoint is not valid,
     * or with status {@code 404 (Not Found)} if the expirationPoint is not found,
     * or with status {@code 500 (Internal Server Error)} if the expirationPoint couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/expiration-points/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExpirationPoint> partialUpdateExpirationPoint(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExpirationPoint expirationPoint
    ) throws URISyntaxException {
        log.debug("REST request to partial update ExpirationPoint partially : {}, {}", id, expirationPoint);
        if (expirationPoint.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, expirationPoint.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!expirationPointRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExpirationPoint> result = expirationPointService.partialUpdate(expirationPoint);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, expirationPoint.getId().toString())
        );
    }

    /**
     * {@code GET  /expiration-points} : get all the expirationPoints.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of expirationPoints in body.
     */
    @GetMapping("/expiration-points")
    public ResponseEntity<List<ExpirationPoint>> getAllExpirationPoints(ExpirationPointCriteria criteria) {
        log.debug("REST request to get ExpirationPoints by criteria: {}", criteria);
        List<ExpirationPoint> entityList = expirationPointQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /expiration-points/count} : count all the expirationPoints.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/expiration-points/count")
    public ResponseEntity<Long> countExpirationPoints(ExpirationPointCriteria criteria) {
        log.debug("REST request to count ExpirationPoints by criteria: {}", criteria);
        return ResponseEntity.ok().body(expirationPointQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /expiration-points/:id} : get the "id" expirationPoint.
     *
     * @param id the id of the expirationPoint to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the expirationPoint, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/expiration-points/{id}")
    public ResponseEntity<ExpirationPoint> getExpirationPoint(@PathVariable Long id) {
        log.debug("REST request to get ExpirationPoint : {}", id);
        Optional<ExpirationPoint> expirationPoint = expirationPointService.findOne(id);
        return ResponseUtil.wrapOrNotFound(expirationPoint);
    }

    /**
     * {@code DELETE  /expiration-points/:id} : delete the "id" expirationPoint.
     *
     * @param id the id of the expirationPoint to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/expiration-points/{id}")
    public ResponseEntity<Void> deleteExpirationPoint(@PathVariable Long id) {
        log.debug("REST request to delete ExpirationPoint : {}", id);
        expirationPointService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
