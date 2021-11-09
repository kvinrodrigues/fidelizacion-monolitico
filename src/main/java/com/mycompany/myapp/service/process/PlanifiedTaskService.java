package com.mycompany.myapp.service.process;

import com.mycompany.myapp.domain.BagOfPoint;
import com.mycompany.myapp.service.BagOfPointService;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class PlanifiedTaskService {

    private final Logger log = LoggerFactory.getLogger(PlanifiedTaskService.class);

    private final BagOfPointService bagOfPointService;

    public PlanifiedTaskService(BagOfPointService bagOfPointService) {
        this.bagOfPointService = bagOfPointService;
    }

    // 9. Proceso planificado cada x horas

    // Proceso que pueda planificarse que corra cada X horas y actualice el estado de
    // las bolsas con puntos vencidos.
    @Scheduled(fixedRateString = "${spring.application.task.delay.time}", initialDelay = 10000)
    public void scheduleFixedRateTask() {
        log.debug("Running planified process...");

        final List<BagOfPoint> modifiedExpiredBagOfPoints = bagOfPointService
            .findAll()
            .stream()
            .filter(bagOfPoints -> Instant.now().isAfter(bagOfPoints.getExpirationDate()))
            .peek(value -> {
                value.assignedScore(0L);
                value.setScoreBalance(0L);
            })
            .collect(Collectors.toList());

        final List<BagOfPoint> updatedExpiredBagOfPoints = bagOfPointService.saveAll(modifiedExpiredBagOfPoints);
        log.info("Number of expired bag of points: {}", updatedExpiredBagOfPoints.size());
    }
}
