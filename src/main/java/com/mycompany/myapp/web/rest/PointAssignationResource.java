package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.PointAssignationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
