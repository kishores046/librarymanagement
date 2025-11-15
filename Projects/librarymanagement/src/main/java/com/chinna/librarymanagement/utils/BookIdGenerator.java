package com.chinna.librarymanagement.utils;

import java.util.concurrent.atomic.AtomicLong;


import org.springframework.stereotype.Component;

@Component
public class BookIdGenerator {
	private final AtomicLong bookIdc=new AtomicLong(0);

	public long nextId() {
		return bookIdc.incrementAndGet();
	}
	
	public long current() {
		return bookIdc.get();
	}
}
