package com.kindredgroup.unibetlivetest.batchs;

import com.kindredgroup.unibetlivetest.service.SelectionService;
import com.kindredgroup.unibetlivetest.utils.ServiceConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class SelectionBatch {

    private static final long UPDATE_ODDS_INTERVAL = 20000L; // 5 seconds in milliseconds
    private static final long CLOSE_ODDS_INTERVAL = 1000L * 60; // 1 minute in milliseconds

    private final SelectionService selectionService;

    /**
     * Scheduled task to update odds for selections at a fixed rate.
     * This method updates the odds randomly every 5 seconds.
     */
    @Scheduled(fixedRate = UPDATE_ODDS_INTERVAL)
    public void updateOddsRandomly() {
        log.info(ServiceConstants.UPDATE_LOG_MESSAGE_TEMPLATE, selectionService.updateOddsRandomly());
    }

    /**
     * Scheduled task to close odds for selections at a fixed rate.
     * This method closes the odds randomly every minute.
     */
    @Scheduled(fixedRate = CLOSE_ODDS_INTERVAL)
    public void closeOddsRandomly() {
        log.info(ServiceConstants.CLOSE_LOG_MESSAGE_TEMPLATE, selectionService.closeOddsRandomly());
    }
}
