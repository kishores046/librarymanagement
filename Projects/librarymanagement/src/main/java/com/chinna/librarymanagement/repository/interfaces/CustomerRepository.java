package com.chinna.librarymanagement.repository.interfaces;

import com.chinna.librarymanagement.model.Customer;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository {
    public Customer save(Customer customer);
    public Optional<Customer> findById(long id);
    public List<Customer> findAll();
    public Customer updateCustomer(long id,String newName,String newEmail,String newPhone);
}
