package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.PointAllocationRule;
import com.mycompany.myapp.repository.PointAllocationRuleRepository;
import com.mycompany.myapp.service.PointAllocationRuleService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.PointAllocationRule}.
 */
@RestController
@RequestMapping("/api")
public class PointAllocationRuleResource {

    private final Logger log = LoggerFactory.getLogger(PointAllocationRuleResource.class);

    private static final String ENTITY_NAME = "pointAllocationRule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PointAllocationRuleService pointAllocationRuleService;

    private final PointAllocationRuleRepository pointAllocationRuleRepository;

    public PointAllocationRuleResource(
        PointAllocationRuleService pointAllocationRuleService,
        PointAllocationRuleRepository pointAllocationRuleRepository
    ) {
        this.pointAllocationRuleService = pointAllocationRuleService;
        this.pointAllocationRuleRepository = pointAllocationRuleRepository;
    }

    /**
     * {@code POST  /point-allocation-rules} : Create a new pointAllocationRule.
     *
     * @param pointAllocationRule the pointAllocationRule to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pointAllocationRule, or with status {@code 400 (Bad Request)} if the pointAllocationRule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/point-allocation-rules")
    public ResponseEntity<PointAllocationRule> createPointAllocationRule(@Valid @RequestBody PointAllocationRule pointAllocationRule)
        throws URISyntaxException {
        log.debug("REST request to save PointAllocationRule : {}", pointAllocationRule);
        if (pointAllocationRule.getId() != null) {
            throw new BadRequestAlertException("A new pointAllocationRule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PointAllocationRule result = pointAllocationRuleService.save(pointAllocationRule);
        return ResponseEntity
            .created(new URI("/api/point-allocation-rules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /point-allocation-rules/:id} : Updates an existing pointAllocationRule.
     *
     * @param id the id of the pointAllocationRule to save.
     * @param pointAllocationRule the pointAllocationRule to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pointAllocationRule,
     * or with status {@code 400 (Bad Request)} if the pointAllocationRule is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pointAllocationRule couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/point-allocation-rules/{id}")
    public ResponseEntity<PointAllocationRule> updatePointAllocationRule(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PointAllocationRule pointAllocationRule
    ) throws URISyntaxException {
        log.debug("REST request to update PointAllocationRule : {}, {}", id, pointAllocationRule);
        if (pointAllocationRule.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pointAllocationRule.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pointAllocationRuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PointAllocationRule result = pointAllocationRuleService.save(pointAllocationRule);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pointAllocationRule.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /point-allocation-rules/:id} : Partial updates given fields of an existing pointAllocationRule, field will ignore if it is null
     *
     * @param id the id of the pointAllocationRule to save.
     * @param pointAllocationRule the pointAllocationRule to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pointAllocationRule,
     * or with status {@code 400 (Bad Request)} if the pointAllocationRule is not valid,
     * or with status {@code 404 (Not Found)} if the pointAllocationRule is not found,
     * or with status {@code 500 (Internal Server Error)} if the pointAllocationRule couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/point-allocation-rules/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PointAllocationRule> partialUpdatePointAllocationRule(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PointAllocationRule pointAllocationRule
    ) throws URISyntaxException {
        log.debug("REST request to partial update PointAllocationRule partially : {}, {}", id, pointAllocationRule);
        if (pointAllocationRule.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pointAllocationRule.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pointAllocationRuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PointAllocationRule> result = pointAllocationRuleService.partialUpdate(pointAllocationRule);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pointAllocationRule.getId().toString())
        );
    }

    /**
     * {@code GET  /point-allocation-rules} : get all the pointAllocationRules.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pointAllocationRules in body.
     */
    @GetMapping("/point-allocation-rules")
    public List<PointAllocationRule> getAllPointAllocationRules() {
        log.debug("REST request to get all PointAllocationRules");
        return pointAllocationRuleService.findAll();
    }

    /**
     * {@code GET  /point-allocation-rules/:id} : get the "id" pointAllocationRule.
     *
     * @param id the id of the pointAllocationRule to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pointAllocationRule, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/point-allocation-rules/{id}")
    public ResponseEntity<PointAllocationRule> getPointAllocationRule(@PathVariable Long id) {
        log.debug("REST request to get PointAllocationRule : {}", id);
        Optional<PointAllocationRule> pointAllocationRule = pointAllocationRuleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pointAllocationRule);
    }

    /**
     * {@code DELETE  /point-allocation-rules/:id} : delete the "id" pointAllocationRule.
     *
     * @param id the id of the pointAllocationRule to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/point-allocation-rules/{id}")
    public ResponseEntity<Void> deletePointAllocationRule(@PathVariable Long id) {
        log.debug("REST request to delete PointAllocationRule : {}", id);
        pointAllocationRuleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
