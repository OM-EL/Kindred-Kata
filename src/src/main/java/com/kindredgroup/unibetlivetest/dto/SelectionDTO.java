package com.kindredgroup.unibetlivetest.dto;

import com.kindredgroup.unibetlivetest.entity.Selection;
import com.kindredgroup.unibetlivetest.types.SelectionResult;
import com.kindredgroup.unibetlivetest.types.State;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) representing a selection.
 * This class captures the essential details of a selection for transfer between layers.
 */
@Data
public class SelectionDTO {

    private Long id;                  // ID of the selection
    private String name;              // Name of the selection
    private BigDecimal currentOdd;   // Current odd value of the selection
    private State state;              // Current state of the selection
    private SelectionResult result;  // Result of the selection (e.g., WON, LOST)
    private Long marketId;            // Associated market ID for the selection

    /**
     * Constructs a SelectionDTO from a given Selection entity.
     *
     * @param selection The selection entity from which to populate this DTO.
     */
    public SelectionDTO(Selection selection) {
        this.id = selection.getId();
        this.name = selection.getName();
        this.currentOdd = selection.getCurrentOdd();
        this.state = selection.getState();
        this.result = selection.getResult();
        this.marketId = selection.getMarket().getId();
    }
}
