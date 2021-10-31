package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Nacionality;
import com.mycompany.myapp.repository.NacionalityRepository;
import com.mycompany.myapp.service.NacionalityQueryService;
import com.mycompany.myapp.service.NacionalityService;
import com.mycompany.myapp.service.criteria.NacionalityCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Nacionality}.
 */
@RestController
@RequestMapping("/api")
public class NacionalityResource {

    private final Logger log = LoggerFactory.getLogger(NacionalityResource.class);

    private static final String ENTITY_NAME = "nacionality";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NacionalityService nacionalityService;

    private final NacionalityRepository nacionalityRepository;

    private final NacionalityQueryService nacionalityQueryService;

    public NacionalityResource(
        NacionalityService nacionalityService,
        NacionalityRepository nacionalityRepository,
        NacionalityQueryService nacionalityQueryService
    ) {
        this.nacionalityService = nacionalityService;
        this.nacionalityRepository = nacionalityRepository;
        this.nacionalityQueryService = nacionalityQueryService;
    }

    /**
     * {@code POST  /nacionalities} : Create a new nacionality.
     *
     * @param nacionality the nacionality to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nacionality, or with status {@code 400 (Bad Request)} if the nacionality has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/nacionalities")
    public ResponseEntity<Nacionality> createNacionality(@Valid @RequestBody Nacionality nacionality) throws URISyntaxException {
        log.debug("REST request to save Nacionality : {}", nacionality);
        if (nacionality.getId() != null) {
            throw new BadRequestAlertException("A new nacionality cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Nacionality result = nacionalityService.save(nacionality);
        return ResponseEntity
            .created(new URI("/api/nacionalities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /nacionalities/:id} : Updates an existing nacionality.
     *
     * @param id the id of the nacionality to save.
     * @param nacionality the nacionality to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nacionality,
     * or with status {@code 400 (Bad Request)} if the nacionality is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nacionality couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/nacionalities/{id}")
    public ResponseEntity<Nacionality> updateNacionality(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Nacionality nacionality
    ) throws URISyntaxException {
        log.debug("REST request to update Nacionality : {}, {}", id, nacionality);
        if (nacionality.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nacionality.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nacionalityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Nacionality result = nacionalityService.save(nacionality);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, nacionality.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /nacionalities/:id} : Partial updates given fields of an existing nacionality, field will ignore if it is null
     *
     * @param id the id of the nacionality to save.
     * @param nacionality the nacionality to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nacionality,
     * or with status {@code 400 (Bad Request)} if the nacionality is not valid,
     * or with status {@code 404 (Not Found)} if the nacionality is not found,
     * or with status {@code 500 (Internal Server Error)} if the nacionality couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/nacionalities/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Nacionality> partialUpdateNacionality(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Nacionality nacionality
    ) throws URISyntaxException {
        log.debug("REST request to partial update Nacionality partially : {}, {}", id, nacionality);
        if (nacionality.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nacionality.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nacionalityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Nacionality> result = nacionalityService.partialUpdate(nacionality);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, nacionality.getId().toString())
        );
    }

    /**
     * {@code GET  /nacionalities} : get all the nacionalities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nacionalities in body.
     */
    @GetMapping("/nacionalities")
    public ResponseEntity<List<Nacionality>> getAllNacionalities(NacionalityCriteria criteria) {
        log.debug("REST request to get Nacionalities by criteria: {}", criteria);
        List<Nacionality> entityList = nacionalityQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /nacionalities/count} : count all the nacionalities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/nacionalities/count")
    public ResponseEntity<Long> countNacionalities(NacionalityCriteria criteria) {
        log.debug("REST request to count Nacionalities by criteria: {}", criteria);
        return ResponseEntity.ok().body(nacionalityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /nacionalities/:id} : get the "id" nacionality.
     *
     * @param id the id of the nacionality to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nacionality, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/nacionalities/{id}")
    public ResponseEntity<Nacionality> getNacionality(@PathVariable Long id) {
        log.debug("REST request to get Nacionality : {}", id);
        Optional<Nacionality> nacionality = nacionalityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nacionality);
    }

    /**
     * {@code DELETE  /nacionalities/:id} : delete the "id" nacionality.
     *
     * @param id the id of the nacionality to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/nacionalities/{id}")
    public ResponseEntity<Void> deleteNacionality(@PathVariable Long id) {
        log.debug("REST request to delete Nacionality : {}", id);
        nacionalityService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
