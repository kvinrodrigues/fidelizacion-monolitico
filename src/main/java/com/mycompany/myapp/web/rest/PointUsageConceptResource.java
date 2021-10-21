package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.PointUsageConcept;
import com.mycompany.myapp.repository.PointUsageConceptRepository;
import com.mycompany.myapp.service.PointUsageConceptService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.PointUsageConcept}.
 */
@RestController
@RequestMapping("/api")
public class PointUsageConceptResource {

    private final Logger log = LoggerFactory.getLogger(PointUsageConceptResource.class);

    private static final String ENTITY_NAME = "pointUsageConcept";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PointUsageConceptService pointUsageConceptService;

    private final PointUsageConceptRepository pointUsageConceptRepository;

    public PointUsageConceptResource(
        PointUsageConceptService pointUsageConceptService,
        PointUsageConceptRepository pointUsageConceptRepository
    ) {
        this.pointUsageConceptService = pointUsageConceptService;
        this.pointUsageConceptRepository = pointUsageConceptRepository;
    }

    /**
     * {@code POST  /point-usage-concepts} : Create a new pointUsageConcept.
     *
     * @param pointUsageConcept the pointUsageConcept to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pointUsageConcept, or with status {@code 400 (Bad Request)} if the pointUsageConcept has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/point-usage-concepts")
    public ResponseEntity<PointUsageConcept> createPointUsageConcept(@RequestBody PointUsageConcept pointUsageConcept)
        throws URISyntaxException {
        log.debug("REST request to save PointUsageConcept : {}", pointUsageConcept);
        if (pointUsageConcept.getId() != null) {
            throw new BadRequestAlertException("A new pointUsageConcept cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PointUsageConcept result = pointUsageConceptService.save(pointUsageConcept);
        return ResponseEntity
            .created(new URI("/api/point-usage-concepts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /point-usage-concepts/:id} : Updates an existing pointUsageConcept.
     *
     * @param id the id of the pointUsageConcept to save.
     * @param pointUsageConcept the pointUsageConcept to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pointUsageConcept,
     * or with status {@code 400 (Bad Request)} if the pointUsageConcept is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pointUsageConcept couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/point-usage-concepts/{id}")
    public ResponseEntity<PointUsageConcept> updatePointUsageConcept(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PointUsageConcept pointUsageConcept
    ) throws URISyntaxException {
        log.debug("REST request to update PointUsageConcept : {}, {}", id, pointUsageConcept);
        if (pointUsageConcept.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pointUsageConcept.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pointUsageConceptRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PointUsageConcept result = pointUsageConceptService.save(pointUsageConcept);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pointUsageConcept.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /point-usage-concepts/:id} : Partial updates given fields of an existing pointUsageConcept, field will ignore if it is null
     *
     * @param id the id of the pointUsageConcept to save.
     * @param pointUsageConcept the pointUsageConcept to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pointUsageConcept,
     * or with status {@code 400 (Bad Request)} if the pointUsageConcept is not valid,
     * or with status {@code 404 (Not Found)} if the pointUsageConcept is not found,
     * or with status {@code 500 (Internal Server Error)} if the pointUsageConcept couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/point-usage-concepts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PointUsageConcept> partialUpdatePointUsageConcept(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PointUsageConcept pointUsageConcept
    ) throws URISyntaxException {
        log.debug("REST request to partial update PointUsageConcept partially : {}, {}", id, pointUsageConcept);
        if (pointUsageConcept.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pointUsageConcept.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pointUsageConceptRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PointUsageConcept> result = pointUsageConceptService.partialUpdate(pointUsageConcept);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pointUsageConcept.getId().toString())
        );
    }

    /**
     * {@code GET  /point-usage-concepts} : get all the pointUsageConcepts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pointUsageConcepts in body.
     */
    @GetMapping("/point-usage-concepts")
    public List<PointUsageConcept> getAllPointUsageConcepts() {
        log.debug("REST request to get all PointUsageConcepts");
        return pointUsageConceptService.findAll();
    }

    /**
     * {@code GET  /point-usage-concepts/:id} : get the "id" pointUsageConcept.
     *
     * @param id the id of the pointUsageConcept to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pointUsageConcept, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/point-usage-concepts/{id}")
    public ResponseEntity<PointUsageConcept> getPointUsageConcept(@PathVariable Long id) {
        log.debug("REST request to get PointUsageConcept : {}", id);
        Optional<PointUsageConcept> pointUsageConcept = pointUsageConceptService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pointUsageConcept);
    }

    /**
     * {@code DELETE  /point-usage-concepts/:id} : delete the "id" pointUsageConcept.
     *
     * @param id the id of the pointUsageConcept to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/point-usage-concepts/{id}")
    public ResponseEntity<Void> deletePointUsageConcept(@PathVariable Long id) {
        log.debug("REST request to delete PointUsageConcept : {}", id);
        pointUsageConceptService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
