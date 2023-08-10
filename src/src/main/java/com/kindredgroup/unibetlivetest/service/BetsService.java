package com.kindredgroup.unibetlivetest.service;

import com.kindredgroup.unibetlivetest.dto.BetRequestDTO;
import com.kindredgroup.unibetlivetest.entity.Bet;
import com.kindredgroup.unibetlivetest.entity.Customer;
import com.kindredgroup.unibetlivetest.entity.Selection;
import com.kindredgroup.unibetlivetest.exception.CustomException;
import com.kindredgroup.unibetlivetest.repository.BetRepository;
import com.kindredgroup.unibetlivetest.repository.CustomerRepository;
import com.kindredgroup.unibetlivetest.repository.SelectionRepository;
import com.kindredgroup.unibetlivetest.types.BetState;
import com.kindredgroup.unibetlivetest.types.ExceptionType;
import com.kindredgroup.unibetlivetest.types.State;
import com.kindredgroup.unibetlivetest.utils.ServiceConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Log4j2
@Service
@RequiredArgsConstructor
public class BetsService {
    private final BetRepository betRepository;
    private final SelectionRepository selectionRepository;
    private final CustomerRepository customerRepository;

    public BetRequestDTO addBet(BetRequestDTO betRequestDTO) {
        Selection selection = getSelection(betRequestDTO.getSelectionId());
        Customer customer = getCustomer(betRequestDTO.getCustomerId());

        validateCurrentOdd(selection, betRequestDTO.getCurrentOdd());
        validateSelectionState(selection);
        validateCustomerBalance(customer, betRequestDTO.getMise());
        validateDuplicateBet(betRequestDTO);

        deductAndSaveCustomerBalance(customer, betRequestDTO.getMise());

        Bet bet = prepareBet(betRequestDTO);
        return new BetRequestDTO(betRepository.save(bet));
    }

    public List<Long> closeBets() {
        List<Bet> bets = betRepository.findByStateAndSelection_State(State.OPENED, State.CLOSED);
        return bets.stream()
                .map(this::safeCloseBetAndPayTheClient)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Transactional
    protected void closeBetAndPayTheClient(Bet bet) {
        bet.setState(State.CLOSED);
        betRepository.save(bet);
        if (bet.getBetState().equals(BetState.WON)) {
            addAndSaveCustomerBalance(bet.getCustomer(), bet.getStake().multiply(bet.getSelection().getCurrentOdd()));
        }
    }

    private Selection getSelection(Long selectionId) {
        return selectionRepository.findById(selectionId)
                .orElseThrow(() -> new CustomException(format(ServiceConstants.SELECTION_NOT_FOUND_TEMPLATE, selectionId), ExceptionType.SELECTION_NOT_FOUND));
    }

    private Customer getCustomer(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomException(format(ServiceConstants.CUSTOMER_NOT_FOUND_TEMPLATE, customerId), ExceptionType.CUSTOMER_NOT_FOUND));
    }

    private void validateCurrentOdd(Selection selection, BigDecimal currentOdd) {
        if (!selection.getCurrentOdd().equals(currentOdd)) {
            throw new CustomException(ServiceConstants.ODD_CHANGED_MESSAGE, ExceptionType.ODD_CHANGED);
        }
    }

    private void validateSelectionState(Selection selection) {
        if (selection.getState() == State.CLOSED) {
            throw new CustomException(ServiceConstants.SELECTION_CLOSED_MESSAGE, ExceptionType.SELECTION_CLOSED);
        }
    }

    private void validateCustomerBalance(Customer customer, BigDecimal stake) {
        if (stake.compareTo(customer.getBalance()) > 0) {
            throw new CustomException(ServiceConstants.INSUFFICIENT_BALANCE_MESSAGE, ExceptionType.INSUFFICIENT_BALANCE);
        }
    }

    private void validateDuplicateBet(BetRequestDTO betRequestDTO) {
        if (betRepository.existsBySelectionIdAndCustomerId(betRequestDTO.getSelectionId(), betRequestDTO.getCustomerId())) {
            throw new CustomException(ServiceConstants.DUPLICATE_BET_MESSAGE, ExceptionType.DUPLICATE_BET_EXCEPTION);
        }
    }

    private Bet prepareBet(BetRequestDTO betRequestDTO) {
        Bet bet = new Bet();
        bet.setDate(new Date());
        bet.setStake(betRequestDTO.getMise());
        bet.setCustomer(new Customer().setId(betRequestDTO.getCustomerId()));
        bet.setSelection(new Selection().setId(betRequestDTO.getSelectionId()));
        return bet;
    }

    private void deductAndSaveCustomerBalance(Customer customer, BigDecimal stake) {
        customer.setBalance(customer.getBalance().subtract(stake));
        customerRepository.save(customer);
    }

    private void addAndSaveCustomerBalance(Customer customer, BigDecimal amount) {
        customer.setBalance(customer.getBalance().add(amount));
        customerRepository.save(customer);
    }

    private Optional<Long> safeCloseBetAndPayTheClient(Bet bet) {
        try {
            closeBetAndPayTheClient(bet);
            return Optional.of(bet.getId());
        } catch (Exception e) {
            log.error(String.format(ServiceConstants.ERROR_PROCESSING_BET_TEMPLATE, bet.getId()), e);
            return Optional.empty();
        }
    }
}
