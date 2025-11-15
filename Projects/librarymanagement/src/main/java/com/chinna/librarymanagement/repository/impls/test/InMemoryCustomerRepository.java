package com.chinna.librarymanagement.repository.impls.test;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.chinna.librarymanagement.repository.interfaces.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import com.chinna.librarymanagement.model.Customer;
@Repository
@Profile("test")
public class InMemoryCustomerRepository implements CustomerRepository {

    private final Map<Long,Customer> customerData;

    @Autowired
    public InMemoryCustomerRepository(Map<Long,Customer> customerData) {
        this.customerData=customerData;
    }


	
	public Customer save(Customer newCustomer) {
		customerData.put(newCustomer.getCustomerId(),newCustomer);
		return newCustomer;
	}
	
	public Optional<Customer> findById(long id) {
		return Optional.ofNullable(customerData.get(id));
	}
	
	public List<Customer> findAll(){
	     return new ArrayList<Customer>(customerData.values());
	}
	
	public Customer updateCustomer(long id,String newName,String newEmail,String newPhone) {
		Customer customer=customerData.get(id);
		customer.setEmail(newEmail).setName(newName).setPhone(newPhone);
		customerData.replace(id,customer);
		return customer;
	}
	
	
	
}
