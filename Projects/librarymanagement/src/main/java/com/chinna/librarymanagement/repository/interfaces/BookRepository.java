package com.chinna.librarymanagement.repository.interfaces;

import java.util.List;
import com.chinna.librarymanagement.model.Book;

public interface BookRepository {
	void save(Book book);
	Book findById(long id);
	List<Book> findByGenre(String genre);
	List<Book> findByAuthor(String author);
	void remove(Book book);
    List<Book> findAll();


}
