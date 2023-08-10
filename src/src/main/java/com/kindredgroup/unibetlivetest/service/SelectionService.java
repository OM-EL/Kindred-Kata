package com.kindredgroup.unibetlivetest.service;

import com.kindredgroup.unibetlivetest.dto.SelectionDTO;
import com.kindredgroup.unibetlivetest.entity.Selection;
import com.kindredgroup.unibetlivetest.exception.CustomException;
import com.kindredgroup.unibetlivetest.repository.EventRepository;
import com.kindredgroup.unibetlivetest.repository.SelectionRepository;
import com.kindredgroup.unibetlivetest.types.ExceptionType;
import com.kindredgroup.unibetlivetest.types.State;
import com.kindredgroup.unibetlivetest.utils.Helpers;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
@Log4j2
public class SelectionService {

    private final SelectionRepository selectionRepository;
    private final EventRepository eventRepository;

    private static final String EVENT_NOT_FOUND_ERROR_TEMPLATE = "Event with ID %s not found";
    private static final String NO_SELECTIONS_FOUND_ERROR_MESSAGE = "No selections found";

    public Long updateOddsRandomly() {
        List<Selection> openedSelections = selectionRepository.getSelectionByStateEquals(State.OPENED);
        openedSelections.forEach(selection -> {
            selection.setCurrentOdd(Helpers.updateOddRandomly(selection.getCurrentOdd()));
            selectionRepository.save(selection);
        });

        return (long) openedSelections.size();
    }

    public Long closeOddsRandomly() {
        List<Selection> openedSelections = selectionRepository.getSelectionByStateEquals(State.OPENED);
        if (openedSelections.isEmpty()) return 0L;

        Set<Selection> randomSelections = selectRandomOpenedSelections(openedSelections);
        randomSelections.forEach(selection -> {
            selection.setState(State.CLOSED);
            selection.setResult(Helpers.setResultRandomly());
            selectionRepository.save(selection);
        });

        return (long) randomSelections.size();
    }

    private Set<Selection> selectRandomOpenedSelections(List<Selection> selections) {
        Set<Selection> randomSelections = new HashSet<>();
        int limit = Math.min(5, selections.size());

        for (int i = 0; i < limit; i++) {
            randomSelections.add(selections.get(Helpers.getRandomIndex(0, selections.size())));
        }

        return randomSelections;
    }

    public List<SelectionDTO> getSelectionsByEventIdAndState(Long eventId, State state) {
        validateEventExists(eventId);
        List<Selection> selections = fetchSelectionsByEventIdAndState(eventId, state);
        validateSelectionsExist(selections);

        return selections.stream().map(SelectionDTO::new).collect(Collectors.toList());
    }

    private void validateEventExists(Long eventId) {
        eventRepository.findById(eventId).orElseThrow(() -> {
            String errorMsg = String.format(EVENT_NOT_FOUND_ERROR_TEMPLATE, eventId);
            log.error(errorMsg);
            return new CustomException(errorMsg, ExceptionType.EVENT_NOT_FOUND);
        });
    }

    private List<Selection> fetchSelectionsByEventIdAndState(Long eventId, State state) {
        return state == null
                ? selectionRepository.findByMarket_Event_Id(eventId)
                : selectionRepository.findByMarket_Event_IdAndState(eventId, state);
    }

    private void validateSelectionsExist(List<Selection> selections) {
        if (selections.isEmpty()) {
            log.error(NO_SELECTIONS_FOUND_ERROR_MESSAGE);
            throw new CustomException(NO_SELECTIONS_FOUND_ERROR_MESSAGE, ExceptionType.SELECTION_NOT_FOUND);
        }
    }
}
