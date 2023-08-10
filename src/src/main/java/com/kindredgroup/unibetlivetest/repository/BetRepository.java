package com.kindredgroup.unibetlivetest.repository;

import com.kindredgroup.unibetlivetest.entity.Bet;
import com.kindredgroup.unibetlivetest.types.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BetRepository extends JpaRepository<Bet, Long> {
    List<Bet> findByStateAndSelection_State(State betState, State selectionState);
    boolean existsBySelectionIdAndCustomerId(Long selectionId, Long customerId);
}
