package com.kindredgroup.unibetlivetest.repository;

import com.kindredgroup.unibetlivetest.entity.Selection;
import com.kindredgroup.unibetlivetest.types.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SelectionRepository extends JpaRepository<Selection, Long> {

    List<Selection> getSelectionByStateEquals(State state);
    List<Selection> findByMarket_Event_IdAndState(Long eventId, State state);

    List<Selection> findByMarket_Event_Id(Long eventId);
}
