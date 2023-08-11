package com.kindredgroup.unibetlivetest.repository;

import com.kindredgroup.unibetlivetest.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> getCustomerByPseudo(String pseudo);

    @Query(value = "SELECT * FROM customer ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Customer findRandom();


}
