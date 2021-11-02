package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.BagOfPoint;
import com.mycompany.myapp.repository.BagOfPointRepository;
import com.mycompany.myapp.service.BagOfPointQueryService;
import com.mycompany.myapp.service.BagOfPointService;
import com.mycompany.myapp.service.criteria.BagOfPointCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.BagOfPoint}.
 */
@RestController
@RequestMapping("/api")
public class BagOfPointResource {

    private final Logger log = LoggerFactory.getLogger(BagOfPointResource.class);

    private static final String ENTITY_NAME = "bagOfPoint";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BagOfPointService bagOfPointService;

    private final BagOfPointRepository bagOfPointRepository;

    private final BagOfPointQueryService bagOfPointQueryService;

    public BagOfPointResource(
        BagOfPointService bagOfPointService,
        BagOfPointRepository bagOfPointRepository,
        BagOfPointQueryService bagOfPointQueryService
    ) {
        this.bagOfPointService = bagOfPointService;
        this.bagOfPointRepository = bagOfPointRepository;
        this.bagOfPointQueryService = bagOfPointQueryService;
    }

    /**
     * {@code POST  /bag-of-points} : Create a new bagOfPoint.
     *
     * @param bagOfPoint the bagOfPoint to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bagOfPoint, or with status {@code 400 (Bad Request)} if the bagOfPoint has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bag-of-points")
    public ResponseEntity<BagOfPoint> createBagOfPoint(@Valid @RequestBody BagOfPoint bagOfPoint) throws URISyntaxException {
        log.debug("REST request to save BagOfPoint : {}", bagOfPoint);
        if (bagOfPoint.getId() != null) {
            throw new BadRequestAlertException("A new bagOfPoint cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BagOfPoint result = bagOfPointService.save(bagOfPoint);
        return ResponseEntity
            .created(new URI("/api/bag-of-points/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bag-of-points/:id} : Updates an existing bagOfPoint.
     *
     * @param id the id of the bagOfPoint to save.
     * @param bagOfPoint the bagOfPoint to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bagOfPoint,
     * or with status {@code 400 (Bad Request)} if the bagOfPoint is not valid,
     * or with status {@code 500 (Internal Server ErrorbagOfPointQueryService)} if the bagOfPoint couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bag-of-points/{id}")
    public ResponseEntity<BagOfPoint> updateBagOfPoint(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BagOfPoint bagOfPoint
    ) throws URISyntaxException {
        log.debug("REST request to update BagOfPoint : {}, {}", id, bagOfPoint);
        if (bagOfPoint.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bagOfPoint.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bagOfPointRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BagOfPoint result = bagOfPointService.save(bagOfPoint);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bagOfPoint.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bag-of-points/:id} : Partial updates given fields of an existing bagOfPoint, field will ignore if it is null
     *
     * @param id the id of the bagOfPoint to save.
     * @param bagOfPoint the bagOfPoint to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bagOfPoint,
     * or with status {@code 400 (Bad Request)} if the bagOfPoint is not valid,
     * or with status {@code 404 (Not Found)} if the bagOfPoint is not found,
     * or with status {@code 500 (Internal Server Error)} if the bagOfPoint couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bag-of-points/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BagOfPoint> partialUpdateBagOfPoint(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BagOfPoint bagOfPoint
    ) throws URISyntaxException {
        log.debug("REST request to partial update BagOfPoint partially : {}, {}", id, bagOfPoint);
        if (bagOfPoint.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bagOfPoint.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bagOfPointRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BagOfPoint> result = bagOfPointService.partialUpdate(bagOfPoint);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bagOfPoint.getId().toString())
        );
    }

    /**
     * {@code GET  /bag-of-points} : get all the bagOfPoints.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bagOfPoints in body.
     */
    @GetMapping("/bag-of-points")
    public ResponseEntity<List<BagOfPoint>> getAllBagOfPoints(BagOfPointCriteria criteria) {
        log.debug("REST request to get BagOfPoints by criteria: {}", criteria);
        List<BagOfPoint> entityList = bagOfPointQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /bag-of-points/count} : count all the bagOfPoints.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/bag-of-points/count")
    public ResponseEntity<Long> countBagOfPoints(BagOfPointCriteria criteria) {
        log.debug("REST request to count BagOfPoints by criteria: {}", criteria);
        return ResponseEntity.ok().body(bagOfPointQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /bag-of-points/:id} : get the "id" bagOfPoint.
     *
     * @param id the id of the bagOfPoint to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bagOfPoint, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bag-of-points/{id}")
    public ResponseEntity<BagOfPoint> getBagOfPoint(@PathVariable Long id) {
        log.debug("REST request to get BagOfPoint : {}", id);
        Optional<BagOfPoint> bagOfPoint = bagOfPointService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bagOfPoint);
    }

    /**
     * {@code DELETE  /bag-of-points/:id} : delete the "id" bagOfPoint.
     *
     * @param id the id of the bagOfPoint to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bag-of-points/{id}")
    public ResponseEntity<Void> deleteBagOfPoint(@PathVariable Long id) {
        log.debug("REST request to delete BagOfPoint : {}", id);
        bagOfPointService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
