package com.comulynx.wallet.rest.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.comulynx.wallet.rest.api.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	Optional<Customer> findByCustomerId(String customerId);

	Boolean existsByCustomerId(String customerId);

	Boolean existsByEmail(String email);

	@Query("DELETE Customer c WHERE c.customerId=:customer_id")
	int deleteCustomerByCustomerId(String customer_id);

	@Query("UPDATE Customer c SET c.firstName=:firstName WHERE c.customerId=:customer_id")
	int updateCustomerByCustomerId(String firstName, String customer_id);

	@Query("SELECT c from Customer c WHERE c.email like '%@gmail.com'")
	List<Customer> findAllCustomersWhoseEmailContainsGmail();
}
