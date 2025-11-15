package com.chinna.librarymanagement.exceptions;

public class CustomerNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CustomerNotFoundException() {
		
	}

	public CustomerNotFoundException(String message) {
		super(message);
		
	}

	public CustomerNotFoundException(Throwable cause) {
		super(cause);
		
	}

	public CustomerNotFoundException(String message, Throwable cause) {
		super(message, cause);
	
	}



}
