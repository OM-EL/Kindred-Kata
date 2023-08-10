package com.kindredgroup.unibetlivetest.service;

import com.kindredgroup.unibetlivetest.entity.Customer;
import com.kindredgroup.unibetlivetest.exception.CustomException;
import com.kindredgroup.unibetlivetest.repository.CustomerRepository;
import com.kindredgroup.unibetlivetest.types.ExceptionType;
import com.kindredgroup.unibetlivetest.utils.ServiceConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    /**
     * Finds a customer based on their pseudo.
     *
     * @param pseudo The pseudo of the customer to be searched.
     * @return The found customer.
     * @throws CustomException If the customer is not found.
     */
    public Customer findCustomerByPseudo(String pseudo) {
        return customerRepository.getCustomerByPseudo(pseudo)
                .orElseThrow(() -> new CustomException(format(ServiceConstants.CUSTOMER_NOT_FOUND_TEMPLATE, pseudo), ExceptionType.CUSTOMER_NOT_FOUND));
    }
}

