package com.chinna.librarymanagement.utils;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerIdGenerator {
	private final AtomicLong customerIdc=new AtomicLong(0L);

	public long nextId() {
		return customerIdc.incrementAndGet();
	}
	
	public long current() {
		return customerIdc.get();
	}
}
