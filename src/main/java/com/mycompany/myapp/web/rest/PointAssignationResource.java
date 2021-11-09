package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.BagOfPoint;
import com.mycompany.myapp.domain.PointUse;
import com.mycompany.myapp.service.PointAssignationService;
import com.mycompany.myapp.web.rest.dto.BackOfPointDto;
import com.mycompany.myapp.web.rest.dto.UseOfPointsDto;
import java.net.URI;
import java.net.URISyntaxException;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<BagOfPoint> createBagofPoint(@Valid @RequestBody BackOfPointDto backOfPointDto) throws URISyntaxException {
        log.debug("REST request to save bagofpoint : {}", backOfPointDto);

        BagOfPoint result = pointAssignationService.assign(backOfPointDto.getClientId(), backOfPointDto.getAmount());
        return ResponseEntity.created(new URI("/api/bag-of-points/" + result.getId())).body(result);
    }

    @PostMapping("/points/use")
    public ResponseEntity<PointUse> usePoints(@Valid UseOfPointsDto useOfPointsDto) throws URISyntaxException {
        PointUse result = pointAssignationService.usePointsFrom(useOfPointsDto);

        return ResponseEntity.created(new URI("/api/point-uses/" + result.getId())).body(result);
    }
}
