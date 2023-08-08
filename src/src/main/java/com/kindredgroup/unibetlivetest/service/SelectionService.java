package com.kindredgroup.unibetlivetest.service;

import com.kindredgroup.unibetlivetest.entity.Selection;
import com.kindredgroup.unibetlivetest.repository.SelectionRepository;
import com.kindredgroup.unibetlivetest.types.SelectionState;
import com.kindredgroup.unibetlivetest.utils.Helpers;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Component
@Log4j2
public class SelectionService {

    private final SelectionRepository selectionRepository;

    /**
     * 1. Récupère toute les selections ouvertes
     * 2. Mis à jour la cote aléatoirement
     */

    public Long updateOddsRandomly() {
        final List<Selection> selectionsOpened = selectionRepository.getSelectionByStateEquals(SelectionState.OPENED);
        return selectionsOpened.isEmpty() ? 0 : selectionsOpened
                .stream()
                .map(selection -> selection.setCurrentOdd(Helpers.updateOddRandomly(selection.getCurrentOdd())))
                .map(selectionRepository::save)
                .count();
    }

    /**
     * 1. Récupère toute les selections ouvertes
     * 2. Ferme 5 sélections aléatoirement.
     */

    public Long closeOddsRandomly() {
        final List<Selection> selectionsOpened = selectionRepository.getSelectionByStateEquals(SelectionState.OPENED);
        return selectionsOpened.isEmpty() ? 0 : IntStream
                .range(0, 5)
                .mapToObj(i -> selectionRepository.save(
                        selectionsOpened.get(Helpers.getRandomIndex(0, selectionsOpened.size()))
                                .setState(SelectionState.CLOSED)
                                .setResult(Helpers.setResultRandomly())))
                .count();
    }


}
