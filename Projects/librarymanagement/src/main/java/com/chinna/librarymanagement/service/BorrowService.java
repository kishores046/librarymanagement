package com.chinna.librarymanagement.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.chinna.librarymanagement.domain.BorrowPolicy;

import com.chinna.librarymanagement.exceptions.BorrowLimitExceededException;
import com.chinna.librarymanagement.exceptions.BorrowRecordNotFoundException;
import com.chinna.librarymanagement.exceptions.CustomerNotFoundException;
import com.chinna.librarymanagement.exceptions.MembershipInvalidException;
import com.chinna.librarymanagement.model.BorrowRecord;
import com.chinna.librarymanagement.model.Customer;
import com.chinna.librarymanagement.model.Membership;
import com.chinna.librarymanagement.repository.interfaces.BorrowRecordRepository;
import com.chinna.librarymanagement.repository.interfaces.CustomerRepository;

@Service
public class BorrowService {

    private final BookService bookService;
    private final BorrowRecordRepository borrowRepo;
    private final CustomerRepository customerRepo;

    public BorrowService(BookService bookService,
                         BorrowRecordRepository borrowRepo,
                         CustomerRepository customerRepo) {
        this.bookService = bookService;
        this.borrowRepo = borrowRepo;
        this.customerRepo = customerRepo;
    }

    // ======================================================
    //                     BORROW
    // ======================================================
    public BorrowRecord borrow(long customerId, long bookId) {

        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found: " + customerId));

        Membership m = customer.getMembership();
        if (m == null)
            throw new MembershipInvalidException("Customer has no membership");
        if (!m.isValid())
            throw new MembershipInvalidException("Membership expired or invalid");

        // Active borrows of customer
        List<BorrowRecord> active = borrowRepo.findActiveByCustomer(customerId);

        // Check borrow limit
        if (active.size() >= m.getMaxBooksAllowedToBeBorrowed())
            throw new BorrowLimitExceededException(
                    "Borrow limit reached for membership type: " + m.getMembershipType()
            );

        // Customer already borrowed this book?
        for (BorrowRecord r : active) {
            if (r.getBookId() == bookId) {
                throw new IllegalStateException("Customer already borrowed this book and has not returned it.");
            }
        }

        // Decrease availability of the book
        bookService.decreaseAvailableOrThrow(bookId, 1);

        LocalDate today = LocalDate.now();
        LocalDate dueDate = BorrowPolicy.computeDueDate(today, m);

        BorrowRecord br = new BorrowRecord(
                0L,
                customerId,
                bookId,
                today,
                dueDate
        );

        borrowRepo.save(br);
        return br;
    }

    public BorrowRecord returnBook(long borrowId) {

        BorrowRecord br = borrowRepo.findById(borrowId)
                .orElseThrow(() -> new BorrowRecordNotFoundException("Borrow record not found: " + borrowId));

        if (br.isReturned())
            return br; // idempotent return

        LocalDate today = LocalDate.now();

        int fine = BorrowPolicy.computeFine(br.getDueDate(), today);
        int overdueDays = Math.max(0,
                (int) (today.toEpochDay() - br.getDueDate().toEpochDay())
        );

        br.setActualReturnDate(today);
        br.setReturned(true);
        br.setFineAmount(fine);
        br.setDaysOverdue(overdueDays);
        br.setBorrowStatus(overdueDays > 0
                ? BorrowRecord.BorrowStatus.OVERDUE
                : BorrowRecord.BorrowStatus.RETURNED);

        br.touch();
        borrowRepo.update(br);

        // restore stock
        bookService.increaseAvailable(br.getBookId(), 1);

        return br;
    }

    public void markOverduesForCustomer(long customerId) {

        List<BorrowRecord> active = borrowRepo.findActiveByCustomer(customerId);
        LocalDate today = LocalDate.now();

        for (BorrowRecord br : active) {
            if (today.isAfter(br.getDueDate())) {
                br.setBorrowStatus(BorrowRecord.BorrowStatus.OVERDUE);
                br.touch();
                borrowRepo.update(br);
            }
        }
    }

    public List<BorrowRecord> getActiveBorrowsOfCustomer(long customerId) {
        return borrowRepo.findActiveByCustomer(customerId);
    }

    public List<BorrowRecord> getActiveBorrowsOfBook(long bookId) {
        return borrowRepo.findActiveByBook(bookId);
    }
}
