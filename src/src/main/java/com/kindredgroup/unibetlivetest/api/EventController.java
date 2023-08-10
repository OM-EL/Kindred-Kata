package com.kindredgroup.unibetlivetest.api;

import com.kindredgroup.unibetlivetest.dto.ApiResponse;
import com.kindredgroup.unibetlivetest.dto.EventDTO;
import com.kindredgroup.unibetlivetest.dto.SelectionDTO;
import com.kindredgroup.unibetlivetest.service.EventService;
import com.kindredgroup.unibetlivetest.service.SelectionService;
import com.kindredgroup.unibetlivetest.types.State;
import com.kindredgroup.unibetlivetest.utils.ServiceConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
@RestController
@Log4j2
@RequestMapping(Urls.BASE_PATH)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")  // Consider limiting origins in production for security.
public class EventController {

    private final EventService eventService;
    private final SelectionService selectionService;

    /**
     * Retrieves events based on their live status.
     *
     * @param isLive Filter events that are currently live. If null, fetch all events.
     * @return ApiResponse containing the list of events.
     */
    @GetMapping(Urls.EVENTS)
    public ResponseEntity<ApiResponse> getLiveEvents(@RequestParam(required = false) Boolean isLive) {
        Set<EventDTO> events = eventService.getEventsByState(isLive);

        ApiResponse response = new ApiResponse();
        response.setData(events);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage(ServiceConstants.FETCHED_EVENTS_SUCCESS_MESSAGE);

        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves selections for a given event, optionally filtered by state.
     *
     * @param id The ID of the event.
     * @param state Optional filter to get selections of a particular state.
     * @return ApiResponse containing the list of selections.
     */
    @GetMapping(Urls.SELECTIONS)
    public ResponseEntity<ApiResponse> getSelectionsForEvent(@PathVariable Long id,
                                                             @RequestParam(required = false) State state) {
        List<SelectionDTO> selections = selectionService.getSelectionsByEventIdAndState(id, state);

        ApiResponse response = new ApiResponse();
        response.setData(selections);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage(ServiceConstants.FETCHED_SELECTIONS_SUCCESS_MESSAGE);

        return ResponseEntity.ok(response);
    }
}
