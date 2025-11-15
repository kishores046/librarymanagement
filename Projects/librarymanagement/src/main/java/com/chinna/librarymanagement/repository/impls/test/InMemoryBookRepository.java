package com.chinna.librarymanagement.repository.impls.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import com.chinna.librarymanagement.exceptions.BookNotFoundException;
import com.chinna.librarymanagement.model.Book;
import com.chinna.librarymanagement.repository.interfaces.BookRepository;

@Repository
@Profile("test")
public class InMemoryBookRepository implements BookRepository {

    private final Map<String, List<Book>> booksByGenre;
    private final Map<Long, Book> bookById;
    private final Map<String, List<Book>> booksByAuthor;

    @Autowired
    public InMemoryBookRepository(
            @Qualifier("booksByGenre") Map<String, List<Book>> booksByGenre,
            @Qualifier("bookById") Map<Long, Book> bookById,
            @Qualifier("booksByAuthor") Map<String, List<Book>> booksByAuthor) {
        this.booksByGenre = booksByGenre;
        this.bookById = bookById;
        this.booksByAuthor = booksByAuthor;
    }

    @Override
    public void save(Book book) {
        booksByGenre.computeIfAbsent(book.getGenre(), k -> new CopyOnWriteArrayList<>()).add(book);
        bookById.put(book.getBookId(), book);
        booksByAuthor.computeIfAbsent(book.getAuthorName(), k -> new CopyOnWriteArrayList<>()).add(book);
    }

    @Override
    public Book findById(long id) {
        if(bookById.containsKey(id)) return bookById.get(id);
        else throw new BookNotFoundException("No book found by this id");
    }

    @Override
    public List<Book> findByGenre(String genre) {
        if(booksByGenre.containsKey(genre)) return booksByGenre.get(genre);
        else throw new BookNotFoundException("No such genre exists");
    }

    @Override
    public List<Book> findByAuthor(String author) {
        if(booksByAuthor.containsKey(author)) return booksByAuthor.get(author);
        else throw new BookNotFoundException("No book with such author exists");
    }

    @Override
    public void remove(Book book) {
        if(bookById.containsKey(book.getBookId())) {
            bookById.remove(book.getBookId());
            List<Book> g = booksByGenre.get(book.getGenre());
            if(g != null) g.remove(book);
            List<Book> a = booksByAuthor.get(book.getAuthorName());
            if(a != null) a.remove(book);
        } else {
            throw new BookNotFoundException("No such book to remove");
        }
    }

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(bookById.values());
    }


}