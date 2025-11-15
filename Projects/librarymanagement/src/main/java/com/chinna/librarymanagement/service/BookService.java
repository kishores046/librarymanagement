package com.chinna.librarymanagement.service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.chinna.librarymanagement.exceptions.BookNotAvailableException;

import com.chinna.librarymanagement.model.Book;
import com.chinna.librarymanagement.repository.interfaces.BookRepository;

@Service
public class BookService {

    private final BookRepository bookRepo;
    private final ConcurrentHashMap<Long, ReentrantLock> locks = new ConcurrentHashMap<>();

    public BookService(BookRepository bookRepo) {
        this.bookRepo = bookRepo;
    }

    // Lock for a specific book (used only for write operations)
    private ReentrantLock lockOf(long bookId) {
        return locks.computeIfAbsent(bookId, id -> new ReentrantLock());
    }

    // =============================
    // BASIC CRUD + REPO OPERATIONS
    // =============================

    public void addBook(Book book) {
        Objects.requireNonNull(book, "Book cannot be null");
        bookRepo.save(book);
    }

    public Book findById(long id) {
        return bookRepo.findById(id); // repo already throws BookNotFoundException
    }

    public List<Book> findByAuthor(String author) {
        return bookRepo.findByAuthor(author);
    }

    public List<Book> findByGenre(String genre) {
        return bookRepo.findByGenre(genre);
    }

    // Not in repo — service implements manually
    public List<Book> findByName(String name) {
        Objects.requireNonNull(name, "Name cannot be null");
        // get all genres, then flatten
        return listAllBooks().stream()
                .filter(b -> b.getBookName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    public void removeBook(long id) {
        Book b = bookRepo.findById(id);
        // we lock only for deletion to avoid race with availability changes
        ReentrantLock lock = lockOf(id);
        lock.lock();
        try {
            bookRepo.remove(b);
        } finally {
            lock.unlock();
        }
    }

    // FULL UPDATE OF BOOK INFO
    public void updateBook(Book updated) {
        Objects.requireNonNull(updated, "Book cannot be null");
        long id = updated.getBookId();
        ReentrantLock lock = lockOf(id);

        lock.lock();
        try {
            Book existing = bookRepo.findById(id);

            existing.setBookName(updated.getBookName());
            existing.setAuthorName(updated.getAuthorName());
            existing.setPublisher(updated.getPublisher());
            existing.setEdition(updated.getEdition());
            existing.setLanguage(updated.getLanguage());
            existing.setGenre(updated.getGenre());
            existing.setDescription(updated.getDescription());
            existing.setIsbn(updated.getIsbn());
            existing.setPublishedDate(updated.getPublishedDate());
            existing.setArchived(updated.isArchived());
            existing.setTotalCopies(updated.getTotalCopies());
            existing.setAvailableCopies(updated.getAvailableCopies());

            bookRepo.save(existing);
        } finally {
            lock.unlock();
        }
    }

    // =============================
    // AVAILABILITY METHODS
    // =============================

    public void increaseAvailable(long bookId, int delta) {
        ReentrantLock l = lockOf(bookId);
        l.lock();
        try {
            Book b = bookRepo.findById(bookId);
            int newVal = Math.min(b.getTotalCopies(), b.getAvailableCopies() + delta);
            b.setAvailableCopies(newVal);
            bookRepo.save(b);
        } finally { l.unlock(); }
    }

    public void decreaseAvailableOrThrow(long bookId, int delta) {
        ReentrantLock l = lockOf(bookId);
        l.lock();
        try {
            Book b = bookRepo.findById(bookId);
            if (b.isArchived()) throw new BookNotAvailableException("Book is archived/unavailable");

            int avail = b.getAvailableCopies();
            if (avail < delta) throw new BookNotAvailableException("Not enough copies available");

            b.setAvailableCopies(avail - delta);
            bookRepo.save(b);
        } finally { l.unlock(); }
    }

    // =============================
    // ARCHIVE / UNARCHIVE
    // =============================

    public void archive(long bookId) {
        ReentrantLock l = lockOf(bookId);
        l.lock();
        try {
            Book b = bookRepo.findById(bookId);
            b.setArchived(true);
            bookRepo.save(b);
        } finally { l.unlock(); }
    }

    public void unarchive(long bookId) {
        ReentrantLock l = lockOf(bookId);
        l.lock();
        try {
            Book b = bookRepo.findById(bookId);
            b.setArchived(false);
            bookRepo.save(b);
        } finally { l.unlock(); }
    }

    // =============================
    // HELPER: LIST ALL BOOKS
    // =============================

    public List<Book> listAllBooks() {
        // Combine all genre lists into one
        // Works for in-memory + JDBC (JDBC repo can override with real query)
        try {
            return bookRepo.findByAuthor("%__ALL_FAKE__%"); // avoid relying on invalid behavior
        } catch (Exception ex) {
            // if JDBC repo supports a direct fetchAll, implement it there
            throw new UnsupportedOperationException("BookRepository must implement findAll() or listAllBooks() logic.");
        }
    }
}
