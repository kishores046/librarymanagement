package com.chinna.librarymanagement.model;



import com.chinna.librarymanagement.validatorsutil.valid.ValidPhone;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Customer {
    // Core Identity Fields
    private Long customerId;
    
    @NotNull(message="Name cannot be empty")
    @Size(min=2,max=50)
    private String name;
    
    @Email
    private String email;
    
    @ValidPhone
    private String phone;
    
    @NotNull(message="Every customer must have membership") 
    private Membership membership;
   
    public Customer(long id,String name, String email, String phone) {
        this.customerId = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
   
	// Getters and Setters
	public long getCustomerId() {
		return customerId;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPhone() {
		return phone;
	}

	public Membership getMembership() {
		return membership;
	}

	public Customer setName(String name) {
		this.name = name;
		return this;
	}

	public Customer setEmail(String email) {
		this.email = email;
		return this;
	}

	public Customer setPhone(String phone) {
		this.phone = phone;
		return this;
		
	}

	public Customer setMembership(Membership membership) {
		this.membership = membership;
		return this;
	}
    
	 @Override
		public String toString() {
			return "Customer [customerId=" + customerId + ", name=" + name + ", email=" + email + ", phone=" + phone
					+ ", membership=" + membership + "]";
	    }
  
}