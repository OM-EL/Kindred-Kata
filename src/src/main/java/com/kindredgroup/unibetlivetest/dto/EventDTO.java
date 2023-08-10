package com.kindredgroup.unibetlivetest.dto;

import com.kindredgroup.unibetlivetest.entity.Event;
import lombok.Data;

import java.util.Date;

/**
 * Data Transfer Object (DTO) representing an event.
 * This class captures the essential details of an event for transfer between layers.
 */
@Data
public class EventDTO {

    private Long id;           // ID of the event
    private String name;       // Name of the event
    private Date startDate;    // Start date and time of the event

    /**
     * Constructs an EventDTO from a given Event entity.
     *
     * @param event The event entity from which to populate this DTO.
     */
    public EventDTO(Event event) {
        this.id = event.getId();
        this.name = event.getName();
        this.startDate = event.getStartDate();
    }
}
