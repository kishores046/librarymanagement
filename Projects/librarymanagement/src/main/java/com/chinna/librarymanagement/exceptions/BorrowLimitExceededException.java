package com.chinna.librarymanagement.exceptions;

public class BorrowLimitExceededException extends RuntimeException {
    public BorrowLimitExceededException(String message) {
        super(message);
    }
}
