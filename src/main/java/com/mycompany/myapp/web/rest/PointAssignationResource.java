package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.BagOfPoint;
import com.mycompany.myapp.domain.PointUseDet;
import com.mycompany.myapp.service.PointAssignationService;
import com.mycompany.myapp.web.rest.dto.BackOfPointDto;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
public class PointAssignationResource {

    private final Logger log = LoggerFactory.getLogger(BagOfPointResource.class);

    private final PointAssignationService pointAssignationService;

    public PointAssignationResource(PointAssignationService pointAssignationService) {
        this.pointAssignationService = pointAssignationService;
    }

    @GetMapping("/points/equivalence")
    public ResponseEntity<Long> getEquivalentPointsByAmount(Float amount) {
        log.debug("REST request to get BagOfPoints by amount: {}", amount);
        Long availablePoints = pointAssignationService.getAmountPointsFrom(amount);
        return ResponseEntity.ok().body(availablePoints);
    }
    @PostMapping("/points/assignation")
    public ResponseEntity<BagOfPoint> createBagofPoint(@Valid @RequestBody BackOfPointDto bagOfPoint) throws URISyntaxException {
        log.debug("REST request to save bagofpoint : {}", bagOfPoint);

        BagOfPoint result = pointAssignationService.pointAssignation(bagOfPoint.getClientId(), bagOfPoint.getAmount());
        return ResponseEntity
            .created(new URI("/api/points/assignation" + result.getId()))
            .body(result);

    }
}
