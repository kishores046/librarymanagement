package com.chinna.librarymanagement;

import java.util.concurrent.ConcurrentHashMap;

public class DemoRunner {
    public static void main(String[] args) {
        // In-memory maps for books repo construction
        var booksByGenre = new java.util.concurrent.ConcurrentHashMap<String, java.util.List<com.chinna.librarymanagement.model.Book>>();
        var bookById = new java.util.concurrent.ConcurrentHashMap<Long, com.chinna.librarymanagement.model.Book>();
        var booksByAuthor = new java.util.concurrent.ConcurrentHashMap<String, java.util.List<com.chinna.librarymanagement.model.Book>>();

        var bookRepo = new com.chinna.librarymanagement.repository.impls.test.InMemoryBookRepository(booksByGenre, bookById, booksByAuthor);
        var customerRepo = new com.chinna.librarymanagement.repository.impls.test.InMemoryCustomerRepository(new ConcurrentHashMap<>());
        var borrowRepo = new com.chinna.librarymanagement.repository.impls.test.InMemoryBorrowRecordRepository(new ConcurrentHashMap<>());
        var bookSvc = new com.chinna.librarymanagement.service.BookService(bookRepo);
        var borrowSvc = new com.chinna.librarymanagement.service.BorrowService(bookSvc, borrowRepo, customerRepo);

        // Seed book
        var b = new com.chinna.librarymanagement.model.Book(101L, "Clean Code", "Robert C. Martin", "9780132350884",
                "Prentice Hall", java.time.LocalDate.of(2008, 8, 1), 3);
        b.setGenre("Software"); b.setLanguage("EN"); b.setEdition("1");
        b.setAvailableCopies(3);
        bookRepo.save(b);

        // Seed customer + membership
        var m = new com.chinna.librarymanagement.model.Membership(
                com.chinna.librarymanagement.model.Membership.MembershipType.REGULAR,
                java.time.LocalDate.now(), java.time.LocalDate.now().plusMonths(2));
        m.setMaxBooksAllowedToBeBorrowed(2);
        m.setNoOfDaysAllowed(10);

        var c = new com.chinna.librarymanagement.model.Customer(1L, "Chinna", "c@x.com", "9999999999");
        c.setMembership(m);
        customerRepo.save(c);

        // Borrow
        var record = borrowSvc.borrow(1L, 101L);
        System.out.println("Borrowed: id=" + record.getBorrowId() + " due=" + record.getDueDate());

        // Return (simulate later date by actually returning today; fine will be 0)
        var returned = borrowSvc.returnBook(record.getBorrowId());
        System.out.println("Returned: fine=" + returned.getFineAmount() + " overdueDays=" + returned.getDaysOverdue());
    }
}
