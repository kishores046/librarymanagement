package com.chinna.librarymanagement.model;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import com.chinna.librarymanagement.utils.BookIdGenerator;
import com.chinna.librarymanagement.validatorsutil.valid.ValidISBN;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Book {
	
	private long bookId;
	
	@NotNull(message="Book Name cannot be empty")
	private String bookName;
	
	@NotNull(message="Author name cannot be blank/empty")
	@Size(min=2,max=50)
	private String authorName;
	
	@ValidISBN
	private String isbn;
	
	@NotNull(message="Invalid publisher details")
	private String publisher;
	
	private LocalDate publishedDate;
	
	@NotNull(message="Invalid genre")
	private String genre;
	
	@NotNull(message="Invalid language")
	private String language;
	
	@NotNull(message="Invalid edition")
	private String edition;
	
	private int totalCopies;
	
	private int availableCopies;


	private String description;

	private boolean archived; // for old/unavailable books
	
	public Book(long bookId,@NotNull(message = "Book Name cannot be empty") String bookName,
			@NotNull(message = "Author name cannot be blank/empty") @Size(min = 2, max = 50) String authorName,
			String isbn, @NotNull(message = "Invalid publisher details") String publisher, LocalDate publishedDate,
			int totalCopies) {
		this.bookId=bookId;
		this.bookName = bookName;
		this.authorName = authorName;
		this.isbn = isbn;
		this.publisher = publisher;
		this.publishedDate = publishedDate;
		this.totalCopies = totalCopies;
	}


	public String getBookName() {
		return bookName;
	}

	public String getAuthorName() {
		return authorName;
	}

	public String getIsbn() {
		return isbn;
	}

	public String getPublisher() {
		return publisher;
	}

	public LocalDate getPublishedDate() {
		return publishedDate;
	}

	public String getGenre() {
		return genre;
	}

	public String getLanguage() {
		return language;
	}

	public String getEdition() {
		return edition;
	}

	public int getTotalCopies() {
		return totalCopies;
	}

	public int getAvailableCopies() {
		return availableCopies;
	}

	public String getDescription() {
		return description;
	}

	public boolean isArchived() {
		return archived;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public void setPublishedDate(LocalDate publishedDate) {
		this.publishedDate = publishedDate;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}

	public void setTotalCopies(int totalCopies) {
		this.totalCopies = totalCopies;
	}

	public void setAvailableCopies(int availableCopies) {
		this.availableCopies = availableCopies;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}
	  
	@Override
	public boolean equals(Object obj) {
	    if (this == obj) return true;                // same object
	    if (obj == null || getClass() != obj.getClass()) return false;

	    Book book = (Book) obj;
	    return isbn != null && isbn.equalsIgnoreCase(book.isbn);
	}
	@Override
	public int hashCode() {
	    return Objects.hash(isbn == null ? 0 : isbn.toLowerCase());
	}


	public long getBookId() {
		return bookId;
	}
}
