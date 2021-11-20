package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.criteria.BagOfPointCriteria;
import com.mycompany.myapp.web.rest.dto.UseOfPointsDto;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
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
    private final ExpirationPointService expirationPointService;
    private final BagOfPointService bagOfPointService;
    private final BagOfPointQueryService bagOfPointQueryService;
    private final PointUseService pointUseService;
    private final PointUseDetService pointUseDetService;
    private final PointUsageConceptService pointUsageConceptService;
    private final ClientService clientService;

    public PointAssignationService(
        PointAllocationRuleService pointAllocationRuleService,
        ExpirationPointService expirationPointService,
        BagOfPointService bagOfPointService,
        BagOfPointQueryService bagOfPointQueryService,
        PointUseService pointUseService,
        PointUseDetService pointUseDetService,
        PointUsageConceptService pointUsageConceptService,
        ClientService clientService
    ) {
        this.pointAllocationRuleService = pointAllocationRuleService;
        this.expirationPointService = expirationPointService;
        this.bagOfPointService = bagOfPointService;
        this.bagOfPointQueryService = bagOfPointQueryService;
        this.pointUseService = pointUseService;
        this.pointUseDetService = pointUseDetService;
        this.pointUsageConceptService = pointUsageConceptService;
        this.clientService = clientService;
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
        Client client = clientService
            .findOne(clientId)
            .orElseThrow(() -> new BadRequestAlertException("Entity not found", "client", "idnotfound"));
        final String initialState = "ACTIVE";

        Instant now = Instant.now();
        Instant expirationDate = expirationPointService
            .findAll()
            .stream()
            .filter(value -> now.isAfter(value.getValidityStartDate()) && now.isBefore(value.getValidityEndDate()))
            .findFirst()
            .map(value -> now.plus(value.getScoreDurationDays(), ChronoUnit.DAYS))
            .orElse(now.plus(30, ChronoUnit.DAYS));

        BagOfPoint bagOfPoint = new BagOfPoint()
            .operationAmount(ammount)
            .client(client)
            .assignedScore(amount)
            .scoreUsed(0L)
            .scoreBalance(amount)
            .state(initialState)
            .asignationDate(Instant.now())
            .expirationDate(expirationDate);
        return bagOfPointService.save(bagOfPoint);
    }

    // - utilizar puntos (POST):se recibe el identificador del cliente y el identificador del
    // concepto de uso y se descuenta dicho puntaje al cliente registrando el uso de puntos
    // (genera datos con la estructura del punto 6 y actualiza la del punto 5)
    // o además debe enviar un correo electrónico al cliente como comprobante
    public PointUse usePointsFrom(UseOfPointsDto useOfPointsDto) {
        PointUsageConcept pointUsageConcept = pointUsageConceptService
            .findOne(useOfPointsDto.getPointUsageConcept().getId())
            .orElseThrow(() -> new BadRequestAlertException("Entity not found", "PointUsage", "idnotfound"));

        clientService
            .findOne(useOfPointsDto.getClient().getId())
            .orElseThrow(() -> new BadRequestAlertException("Entity not found", "client", "idnotfound"));

        Long scoreToUse = pointUsageConcept.getRequiredPoints();
        BagOfPoint bagOfPoint = getAvailableBagOfPointFrom(useOfPointsDto, pointUsageConcept.getRequiredPoints());
        updateBagOfPoint(bagOfPoint, scoreToUse);

        PointUse pointUse = generatePointUse(useOfPointsDto, pointUsageConcept, scoreToUse);
        PointUseDet pointUseDet = generatePointUseDetail(bagOfPoint, scoreToUse, pointUse);

        pointUse.setPointUseDetails(Collections.singleton(pointUseDet));
        return pointUse;
    }

    private BagOfPoint getAvailableBagOfPointFrom(UseOfPointsDto useOfPointsDto, Long requiredScore) {
        BagOfPointCriteria bagOfPointCriteria = new BagOfPointCriteria();

        bagOfPointCriteria.setState(new StringFilter().setContains("ACTIVE"));
        LongFilter clientFilter = new LongFilter();
        clientFilter.setEquals(useOfPointsDto.getClient().getId());
        bagOfPointCriteria.setClientId(clientFilter);
        bagOfPointCriteria.setExpirationDate(new InstantFilter().setGreaterThan(Instant.now()));
        LongFilter scoreFilter = new LongFilter();
        scoreFilter.setGreaterThanOrEqual(requiredScore);

        bagOfPointCriteria.setScoreBalance(scoreFilter);

        return bagOfPointQueryService
            .findByCriteria(bagOfPointCriteria)
            .stream()
            .max(Comparator.comparing(BagOfPoint::getAsignationDate))
            .orElseThrow(() -> new NullPointerException("Cannot found an available bagOfPoint"));
    }

    private void updateBagOfPoint(BagOfPoint bagOfPoint, Long scoreToUse) {
        Long scoreUsed = bagOfPoint.getScoreUsed();
        Long currentBalance = bagOfPoint.getScoreBalance();

        bagOfPoint.setScoreUsed(scoreUsed + scoreToUse);
        bagOfPoint.setScoreBalance(currentBalance - scoreToUse);
        bagOfPointService.save(bagOfPoint);
    }

    private PointUseDet generatePointUseDetail(BagOfPoint bagOfPoint, Long scoreToUse, PointUse pointUse) {
        PointUseDet pointUseDet = new PointUseDet().pointUse(pointUse).scoreUsed(scoreToUse).bagOfPoint(bagOfPoint);

        return pointUseDetService.save(pointUseDet);
    }

    private PointUse generatePointUse(UseOfPointsDto useOfPointsDto, PointUsageConcept pointUsageConcept, Long scoreToUse) {
        PointUse pointUse = new PointUse()
            .pointUsageConcept(pointUsageConcept)
            .scoreUsed(scoreToUse)
            .eventDate(Instant.now())
            .client(new Client().id(useOfPointsDto.getClient().getId()));

        return pointUseService.save(pointUse);
    }
}
