package com.chinna.librarymanagement.config.profiles;

import com.chinna.librarymanagement.model.Book;
import com.chinna.librarymanagement.model.BorrowRecord;
import com.chinna.librarymanagement.model.Customer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@Profile("test")
public class CacheTestConfig {

    @Bean
    public Map<String, List<Book>> booksByGenre() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Map<Long, Book> bookById() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Map<String, List<Book>> booksByAuthor() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Map<Long, BorrowRecord> borrowRecordStore() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Map<Long, Customer> customerStore() {
        return new ConcurrentHashMap<>();
    }
}
