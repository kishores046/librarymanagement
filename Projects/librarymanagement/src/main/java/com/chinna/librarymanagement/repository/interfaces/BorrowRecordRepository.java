package com.chinna.librarymanagement.repository.interfaces;

import com.chinna.librarymanagement.model.BorrowRecord;

import java.util.List;
import java.util.Optional;

public interface BorrowRecordRepository {
    BorrowRecord save(BorrowRecord br);
    Optional<BorrowRecord> findById(long borrowId);
    List<BorrowRecord> findActiveByCustomer(long customerId);
    List<BorrowRecord> findActiveByBook(long bookId);
    void update(BorrowRecord br);
}
