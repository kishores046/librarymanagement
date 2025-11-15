package com.chinna.librarymanagement.exceptions;

public class BorrowRecordNotFoundException extends RuntimeException {
    public BorrowRecordNotFoundException(String message) {
        super(message);
    }
}
