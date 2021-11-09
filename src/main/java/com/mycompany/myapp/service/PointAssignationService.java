package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.criteria.BagOfPointCriteria;
import com.mycompany.myapp.web.rest.dto.UseOfPointsDto;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

@Service
@Transactional
public class PointAssignationService {

    private final PointAllocationRuleService pointAllocationRuleService;
    private final BagOfPointService bagOfPointService;
    private final BagOfPointQueryService bagOfPointQueryService;
    private final PointUseService pointUseService;
    private final PointUseDetService pointUseDetService;
    private final PointUsageConceptService pointUsageConceptService;

    public PointAssignationService(
        PointAllocationRuleService pointAllocationRuleService,
        BagOfPointService bagOfPointService,
        BagOfPointQueryService bagOfPointQueryService,
        PointUseService pointUseService,
        PointUseDetService pointUseDetService,
        PointUsageConceptService pointUsageConceptService
    ) {
        this.pointAllocationRuleService = pointAllocationRuleService;
        this.bagOfPointService = bagOfPointService;
        this.bagOfPointQueryService = bagOfPointQueryService;
        this.pointUseService = pointUseService;
        this.pointUseDetService = pointUseDetService;
        this.pointUsageConceptService = pointUsageConceptService;
    }

    //  consultar cuantos puntos equivale a un monto X (GET):es un servicio
    // informativo que devuelve la cantidad de puntos equivalente al monto proporcionado
    // como parámetro utilizando la configuración del punto 3
    public Long getAmountPointsFrom(Float amount) {
        Long finalPoint;

        final Optional<PointAllocationRule> pointAllocationRule = pointAllocationRuleService
            .findAll()
            .stream()
            .filter(value -> value.isInTheRange(amount))
            .findFirst();

        finalPoint = pointAllocationRule.map(allocationRule -> (long) (amount / allocationRule.getEquivalenceOfAPoint())).orElse(0L);
        return finalPoint;
    }

    // - carga de puntos (POST):se recibe el identificador de cliente y el monto de la
    // operación, y se asigna los puntos (genera datos con la estructura del punto 5)
    public BagOfPoint assign(Long clientId, Float ammount) {
        long amount = getAmountPointsFrom(ammount);
        final String initialState = "ACTIVE";

        BagOfPoint bagOfPoint = new BagOfPoint()
            .operationAmount(ammount)
            .client(new Client().id(clientId))
            .assignedScore(amount)
            .scoreUsed(0L)
            .scoreBalance(amount)
            .state(initialState)
            .asignationDate(Instant.now())
            .expirationDate(Instant.now().plus(30, ChronoUnit.DAYS));
        return bagOfPointService.save(bagOfPoint);
    }

    // - utilizar puntos (POST):se recibe el identificador del cliente y el identificador del
    // concepto de uso y se descuenta dicho puntaje al cliente registrando el uso de puntos
    // (genera datos con la estructura del punto 6 y actualiza la del punto 5)
    // o además debe enviar un correo electrónico al cliente como comprobante
    public PointUse usePointsFrom(UseOfPointsDto useOfPointsDto) {
        PointUsageConcept pointUsageConcept = pointUsageConceptService
            .findOne(useOfPointsDto.getUsageConceptId())
            .orElseThrow(() -> new BadRequestAlertException("Entity not found", "PointUsage", "idnotfound"));

        Long scoreToUse = pointUsageConcept.getRequiredPoints();
        BagOfPoint bagOfPoint = getAvailableBagOfPointFrom(useOfPointsDto);

        PointUse pointUse = generatePointUse(useOfPointsDto, pointUsageConcept, scoreToUse);
        generatePointUseDetail(bagOfPoint, scoreToUse, pointUse);

        return pointUse;
    }

    private BagOfPoint getAvailableBagOfPointFrom(UseOfPointsDto useOfPointsDto) {
        BagOfPointCriteria bagOfPointCriteria = new BagOfPointCriteria();

        bagOfPointCriteria.setState(new StringFilter().setContains("ACTIVE"));
        LongFilter clientFilter = new LongFilter();
        clientFilter.setEquals(useOfPointsDto.getClientId());
        bagOfPointCriteria.setClientId(clientFilter);
        bagOfPointCriteria.setExpirationDate(new InstantFilter().setGreaterThan(Instant.now()));

        return bagOfPointQueryService
            .findByCriteria(bagOfPointCriteria)
            .stream()
            .max(Comparator.comparing(BagOfPoint::getAsignationDate))
            .orElseThrow(() -> new NullPointerException("Cannot found an available bagOfPoint"));
    }

    private void generatePointUseDetail(BagOfPoint bagOfPoint, Long scoreToUse, PointUse pointUse) {
        PointUseDet pointUseDet = new PointUseDet().pointUse(pointUse).scoreUsed(scoreToUse).bagOfPoint(bagOfPoint);

        pointUseDetService.save(pointUseDet);
    }

    private PointUse generatePointUse(UseOfPointsDto useOfPointsDto, PointUsageConcept pointUsageConcept, Long scoreToUse) {
        PointUse pointUse = new PointUse()
            .pointUsageConcept(pointUsageConcept)
            .scoreUsed(scoreToUse)
            .eventDate(Instant.now())
            .client(new Client().id(useOfPointsDto.getClientId()));
        pointUseService.save(pointUse);
        return pointUse;
    }
}
