package com.chinna.librarymanagement.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class BorrowRecord {
	private final long customerId;
	private long borrowId;
	private final long bookId;
	private LocalDate borrowDate;
	private LocalDate dueDate;
    private LocalDate actualReturnDate;
	private boolean isReturned;
	private int fineAmount;

    public void setBorrowId(long borrowId) {
        this.borrowId = borrowId;
    }

    private int daysOverdue;
	private LocalDateTime createdAt;
	private LocalDateTime updateAt;
	private BorrowStatus borrowStatus;

    public void setActualReturnDate(LocalDate today) {
        actualReturnDate=today;
    }

    public LocalDate getActualReturnDate() {
        return actualReturnDate;
    }

    public enum BorrowStatus{
		BORROWED,RETURNED,OVERDUE
	}
    public BorrowRecord(long borrowId, long customerId, long bookId, LocalDate borrowDate, LocalDate dueDate) {
        this.borrowId = borrowId;
        this.customerId = customerId;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.createdAt = LocalDateTime.now();
        this.updateAt = this.createdAt;
        this.borrowStatus = BorrowStatus.BORROWED;
        this.isReturned = false;
    }
	public LocalDate getBorrowDate() {
		return borrowDate;
	}
	public void setBorrowDate(LocalDate borrowDate) {
		this.borrowDate = borrowDate;
	}
	public boolean isReturned() {
		return isReturned;
	}
	public void setReturned(boolean isReturned) {
		this.isReturned = isReturned;
	}
	public int getFineAmount() {
		return fineAmount;
	}
	public void setFineAmount(int fineAmount) {
		this.fineAmount = fineAmount;
	}
	public int getDaysOverdue() {
		return daysOverdue;
	}
	public void setDaysOverdue(int daysOverdue) {
		this.daysOverdue = daysOverdue;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public LocalDateTime getUpdateAt() {
		return updateAt;
	}
	public void setUpdateAt(LocalDateTime updateAt) {
		this.updateAt = updateAt;
	}
	public BorrowStatus getBorrowStatus() {
		return borrowStatus;
	}
	public void setBorrowStatus(BorrowStatus borrowStatus) {
		this.borrowStatus = borrowStatus;
	}
	public long getCustomerId() {
		return customerId;
	}
	public long getBorrowId() {
		return borrowId;
	}
	public long getBookId() {
		return bookId;
	}
	public LocalDate getDueDate() {
		return dueDate;
	}
	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}
    public void touch() { this.updateAt = LocalDateTime.now(); }

}
