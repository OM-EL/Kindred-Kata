package com.kindredgroup.unibetlivetest.service;

import com.kindredgroup.unibetlivetest.dto.EventDTO;
import com.kindredgroup.unibetlivetest.entity.Event;
import com.kindredgroup.unibetlivetest.entity.Selection;
import com.kindredgroup.unibetlivetest.exception.CustomException;
import com.kindredgroup.unibetlivetest.repository.EventRepository;
import com.kindredgroup.unibetlivetest.repository.SelectionRepository;
import com.kindredgroup.unibetlivetest.types.ExceptionType;
import com.kindredgroup.unibetlivetest.types.State;
import com.kindredgroup.unibetlivetest.utils.ServiceConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final SelectionRepository selectionRepository;

    /**
     * Retrieves events based on their live state. If isLive is null, it fetches all events.
     *
     * @param isLive Boolean indicating the live state of the events. If null, fetches all events.
     * @return A set of EventDTOs that match the given live state or all if isLive is null.
     * @throws CustomException if no matching events are found.
     */
    public Set<EventDTO> getEventsByState(Boolean isLive) {
        Set<EventDTO> events = (isLive == null) ? getAllEvents() : getEventsByLiveState(isLive);

        if (events.isEmpty()) {
            log.warn(String.format(ServiceConstants.NO_EVENTS_FOUND_LOG_TEMPLATE, isLive));
            throw new CustomException(ServiceConstants.NO_EVENTS_FOUND_EXCEPTION_MESSAGE, ExceptionType.EVENTS_NOT_FOUND);
        }

        return events;
    }

    /**
     * Fetches all events from the repository, maps them to DTOs, and returns as a set.
     *
     * @return A set of all EventDTOs.
     */
    private Set<EventDTO> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(EventDTO::new)
                .collect(Collectors.toSet());
    }

    /**
     * Retrieves events from the repository based on their associated selections' state.
     *
     * @param isLive Boolean indicating if the selections should be in OPENED state (true) or CLOSED state (false).
     * @return A set of EventDTOs corresponding to the given live state.
     */
    private Set<EventDTO> getEventsByLiveState(Boolean isLive) {
        if (isLive) {
            List<Event> liveEvents = eventRepository.findEventsBySelectionState(State.OPENED);
            return liveEvents.stream().map(EventDTO::new).collect(Collectors.toSet());
        } else {
            List<Event> notLiveEvents = eventRepository.findEventsWithoutSelectionState(State.OPENED);
            return notLiveEvents.stream().map(EventDTO::new).collect(Collectors.toSet());
        }
    }

    /**
     * Validates if a given selection is associated with a market and an event.
     *
     * @param selection The selection entity to validate.
     * @return Boolean indicating if the selection has both a market and an associated event.
     */
    private boolean isValidSelection(Selection selection) {
        return selection.getMarket() != null && selection.getMarket().getEvent() != null;
    }
}
