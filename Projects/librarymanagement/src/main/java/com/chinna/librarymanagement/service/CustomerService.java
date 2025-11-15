package com.chinna.librarymanagement.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.chinna.librarymanagement.forms.SignUpForm;
import com.chinna.librarymanagement.model.factory.MembershipFactory;
import com.chinna.librarymanagement.repository.interfaces.CustomerRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import com.chinna.librarymanagement.exceptions.CustomerNotFoundException;
import com.chinna.librarymanagement.model.Customer;
import com.chinna.librarymanagement.utils.CustomerIdGenerator;

@Service
public class CustomerService {
	private final MembershipService membershipService;
	private final CustomerRepository customerRepository;
	private final CustomerIdGenerator customerIdGenerator;
    private final MembershipFactory membershipFactory;
	public CustomerService(MembershipService membershipService,CustomerRepository customerRepository,CustomerIdGenerator customerIdGenerator,MembershipFactory membershipFactory) {
		this.membershipService = membershipService;
		this.customerRepository=customerRepository;
		this.customerIdGenerator=customerIdGenerator;
        this.membershipFactory=membershipFactory;
	}
	
	public Customer addCustomer(String name,String email,String phone,String membershipType) {
		Customer newCustomer=new Customer(customerIdGenerator.nextId(),name,email,phone);
		newCustomer.setMembership(membershipService.generateMembership(membershipType,LocalDate.now()));
		return customerRepository.save(newCustomer);
	}
	
	public Optional<Customer> getCustomerById(long id) {
		return Optional.ofNullable(customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("No customer foud by given id")));
	}
	
	public List<Customer> getAllCustomers(){
		return customerRepository.findAll();
	}


    public Customer createFromForm(@Valid SignUpForm form) {
        long newId = System.currentTimeMillis();  // or a sequence generator in DB profile

        // 2) Build the customer entity
        Customer customer = new Customer(
                newId,
                form.getName(),
                form.getEmail(),
                form.getPhone()
        );
        LocalDate today = LocalDate.now();
        var membership = membershipFactory.generateMembership(form.getMembershipType(), today);
        customer.setMembership(membership);
        customerRepository.save(customer);
        return customer;
    }
}
