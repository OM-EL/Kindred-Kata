package com.kindredgroup.unibetlivetest.batchs;


import com.kindredgroup.unibetlivetest.service.BetsService;
import com.kindredgroup.unibetlivetest.utils.ServiceConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Log4j2
@RequiredArgsConstructor
public class MarketBatch {

    private final BetsService betsService;

    /**
     * Scheduled task to close bets at a specified interval.
     * The interval is configurable through the application properties.
     */
    @Scheduled(fixedRateString = ServiceConstants.BATCH_EXECUTION_INTERVAL_PROP)
    public synchronized void executeCloseBets() {
        long startTime = System.nanoTime();

        List<Long> closedBets = betsService.closeBets();

        long duration = (System.nanoTime() - startTime) / 1_000_000;  // Convert to milliseconds

        log.info(ServiceConstants.LOG_MESSAGE_TEMPLATE, closedBets.size(), duration);
    }
}
