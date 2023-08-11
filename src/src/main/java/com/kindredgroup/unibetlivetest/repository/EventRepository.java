package com.kindredgroup.unibetlivetest.repository;

import com.kindredgroup.unibetlivetest.entity.Customer;
import com.kindredgroup.unibetlivetest.entity.Event;
import com.kindredgroup.unibetlivetest.types.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT DISTINCT s.market.event FROM Selection s WHERE s.state = :state")
    List<Event> findEventsBySelectionState(@Param("state") State state);

    @Query("SELECT DISTINCT e FROM Event e WHERE NOT EXISTS (SELECT s from Selection s WHERE s.market.event = e AND s.state = :state)")
    List<Event> findEventsWithoutSelectionState(@Param("state") State state);

    @Query(value = "SELECT * FROM event ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Event findRandom();

}
