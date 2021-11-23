package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.criteria.BagOfPointCriteria;
import com.mycompany.myapp.web.rest.dto.UseOfPointsDto;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
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
        // Validaciones
        PointUsageConcept pointUsageConcept = pointUsageConceptService
            .findOne(useOfPointsDto.getPointUsageConcept().getId())
            .orElseThrow(() -> new BadRequestAlertException("Entity not found", "PointUsage", "idnotfound"));

        clientService
            .findOne(useOfPointsDto.getClient().getId())
            .orElseThrow(() -> new BadRequestAlertException("Entity not found", "client", "idnotfound"));

        // Declaracion de cantidad de puntos a utilizar
        Long scoreToUse = pointUsageConcept.getRequiredPoints();
        // Se obtienen las bolsas de puntos disponibles que por lo menos tengan 1 punto disponible
        List<BagOfPoint> bagOfPoints = getAvailableBagOfPointFrom(useOfPointsDto, pointUsageConcept.getRequiredPoints());

        // En caso de que no es encuentre una bolsa de puntos es que se lanza una excepcion
        if (bagOfPoints.isEmpty()) {
            throw new NullPointerException("Cannot found an available bagOfPoints");
        }

        // Se registra la cantidad de puntos necesarios segun el concepto
        Long requiredPoints = pointUsageConcept.getRequiredPoints();

        // Se registra una nueva entrada de un uso de puntaje
        PointUse pointUse = generatePointUse(useOfPointsDto, pointUsageConcept, scoreToUse);

        // Se recorren las bolsas de puntos disponibles para realizar la actualizacion de cantidad de puntos
        for (BagOfPoint bagOfpoint : bagOfPoints) {
            final Long scoreBalance = bagOfpoint.getScoreBalance();

            if (requiredPoints > scoreBalance) {
                bagOfpoint.incrementScoreUsed(scoreBalance);
                bagOfpoint.setScoreBalance(0L);
                requiredPoints -= scoreBalance;
            } else {
                Long resultScore = bagOfpoint.getScoreBalance() - requiredPoints;

                bagOfpoint.incrementScoreUsed(requiredPoints);
                bagOfpoint.setScoreBalance(resultScore);
                requiredPoints -= scoreBalance;
            }

            PointUseDet pointUseDet = generatePointUseDetail(bagOfpoint, pointUse);
            pointUse.setPointUseDetails(Collections.singleton(pointUseDet));

            if (requiredPoints.equals(0L)) {
                break;
            }

            updateBagOfPoint(bagOfpoint, scoreToUse);
        }

        return pointUse;
    }

    private List<BagOfPoint> getAvailableBagOfPointFrom(UseOfPointsDto useOfPointsDto, Long requiredScore) {
        BagOfPointCriteria bagOfPointCriteria = new BagOfPointCriteria();

        bagOfPointCriteria.setState(new StringFilter().setContains("ACTIVE"));
        LongFilter clientFilter = new LongFilter();
        clientFilter.setEquals(useOfPointsDto.getClient().getId());
        bagOfPointCriteria.setClientId(clientFilter);
        bagOfPointCriteria.setExpirationDate(new InstantFilter().setGreaterThan(Instant.now()));
        LongFilter scoreFilter = new LongFilter();
        scoreFilter.setGreaterThan(0L);

        bagOfPointCriteria.setScoreBalance(scoreFilter);

        return bagOfPointQueryService
            .findByCriteria(bagOfPointCriteria)
            .stream()
            .sorted(Comparator.comparing(BagOfPoint::getAsignationDate))
            .collect(Collectors.toList());
    }

    private void updateBagOfPoint(BagOfPoint bagOfPoint, Long scoreToUse) {
        Long scoreUsed = bagOfPoint.getScoreUsed();
        Long currentBalance = bagOfPoint.getScoreBalance();

        bagOfPoint.setScoreUsed(scoreUsed + scoreToUse);
        bagOfPoint.setScoreBalance(currentBalance - scoreToUse);
        bagOfPointService.save(bagOfPoint);
    }

    private PointUseDet generatePointUseDetail(BagOfPoint bagOfPoint, PointUse pointUse) {
        Long scoreToUse = bagOfPoint.getScoreUsed();
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
