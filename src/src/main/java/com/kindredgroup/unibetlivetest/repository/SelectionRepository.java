package com.kindredgroup.unibetlivetest.repository;

import com.kindredgroup.unibetlivetest.entity.Selection;
import com.kindredgroup.unibetlivetest.types.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SelectionRepository extends JpaRepository<Selection, Long> {

    List<Selection> getSelectionByStateEquals(State state);
    List<Selection> findByMarket_Event_IdAndState(Long eventId, State state);
    List<Selection> findByMarket_Event_Id(Long eventId);
    @Query(value = "SELECT * FROM selection ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Selection findRandom();

}
