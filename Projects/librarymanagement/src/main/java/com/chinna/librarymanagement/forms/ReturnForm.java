package com.chinna.librarymanagement.forms;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ReturnForm {
    @NotNull @Min(1)
    private Long borrowId;

    public Long getBorrowId() { return borrowId; }
    public void setBorrowId(Long borrowId) { this.borrowId = borrowId; }
}
