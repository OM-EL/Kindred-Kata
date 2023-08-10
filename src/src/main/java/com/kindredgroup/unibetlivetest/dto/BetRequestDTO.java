package com.kindredgroup.unibetlivetest.dto;

import com.kindredgroup.unibetlivetest.entity.Bet;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) representing the request for placing a bet.
 * This class captures all necessary details for a betting operation.
 */
@Data
public class BetRequestDTO {

    private Long selectionId;          // ID of the selection being bet on
    private BigDecimal currentOdd;     // Current odd for the selection
    private Long customerId;           // ID of the customer placing the bet
    private BigDecimal mise;           // Amount staked by the customer

    /**
     * Constructs a BetRequestDTO from a given Bet entity.
     *
     * @param bet The bet entity from which to populate this DTO.
     */
    public BetRequestDTO(Bet bet) {
        this.selectionId = bet.getSelection().getId();
        this.currentOdd = bet.getSelection().getCurrentOdd();
        this.customerId = bet.getCustomer().getId();
        this.mise = bet.getStake();
    }
}
