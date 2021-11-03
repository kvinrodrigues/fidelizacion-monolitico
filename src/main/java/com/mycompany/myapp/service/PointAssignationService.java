package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.BagOfPoint;
import com.mycompany.myapp.domain.Client;
import com.mycompany.myapp.domain.PointAllocationRule;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class PointAssignationService {

    private final PointAllocationRuleService pointAllocationRuleService;
    private final BagOfPointService bagOfPointService;

    public PointAssignationService(PointAllocationRuleService pointAllocationRuleService, BagOfPointService bagOfPointService) {
        this.pointAllocationRuleService = pointAllocationRuleService;
        this.bagOfPointService = bagOfPointService;
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
    // - utilizar puntos (POST):se recibe el identificador del cliente y el identificador del
    //concepto de uso y se descuenta dicho puntaje al cliente registrando el uso de puntos
    //(genera datos con la estructura del punto 6 y actualiza la del punto 5)
    //o además debe enviar un correo electrónico al cliente como comprobante
    // TODO, utiliza el metodo getAmountPointsFrom

    // - carga de puntos (POST):se recibe el identificador de cliente y el monto de la
    // operación, y se asigna los puntos (genera datos con la estructura del punto 5)
    // TODO
    public BagOfPoint pointAssignation(Long identificador, Float monto) {
        long amount = getAmountPointsFrom(monto);
        BagOfPoint bagOfPoint = new BagOfPoint()
            .operationAmount(monto)
            .client(new Client().id(identificador))
            .assignedScore(amount)
            .scoreUsed(0L)
            .scoreBalance(amount)
            .state("active")
            .asignationDate(Instant.now())
            .expirationDate(Instant.now().plus(30, ChronoUnit.DAYS));
        return bagOfPointService.save(bagOfPoint);

    }

}
