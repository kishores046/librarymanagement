package com.chinna.librarymanagement.repository.impls.prod;
import com.chinna.librarymanagement.exceptions.BookNotFoundException;
import com.chinna.librarymanagement.model.Book;
import com.chinna.librarymanagement.repository.interfaces.BookRepository;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Profile("prod")
public class JdbcBookRepository implements BookRepository {
    private final JdbcTemplate jdbc;

    private final RowMapper<Book> bookMapper = (rs, rowNum) -> mapBook(rs);

    public JdbcBookRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private Book mapBook(ResultSet rs) throws SQLException {
        Book b = new Book(
                rs.getLong("book_id"),
                rs.getString("book_name"),
                rs.getString("author_name"),
                rs.getString("isbn"),
                rs.getString("publisher"),
                rs.getDate("published_date") == null ? null :
                        rs.getDate("published_date").toLocalDate(),
                rs.getInt("total_copies")
        );

        b.setAvailableCopies(rs.getInt("available_copies"));
        b.setGenre(rs.getString("genre"));
        b.setLanguage(rs.getString("language"));
        b.setEdition(rs.getString("edition"));
        b.setArchived(rs.getBoolean("archived"));

        return b;
    }

    @Override
    public void save(Book b) {
        String sql = """
                INSERT INTO BOOKS 
                (book_id, book_name, author_name, isbn, publisher, published_date,
                 genre, language, edition, total_copies, available_copies, archived)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                  book_name = VALUES(book_name),
                  author_name = VALUES(author_name),
                  isbn = VALUES(isbn),
                  publisher = VALUES(publisher),
                  published_date = VALUES(published_date),
                  genre = VALUES(genre),
                  language = VALUES(language),
                  edition = VALUES(edition),
                  total_copies = VALUES(total_copies),
                  available_copies = VALUES(available_copies),
                  archived = VALUES(archived);
                """;

        jdbc.update(sql,
                b.getBookId(), b.getBookName(), b.getAuthorName(), b.getIsbn(),
                b.getPublisher(),
                b.getPublishedDate() == null ? null : java.sql.Date.valueOf(b.getPublishedDate()),
                b.getGenre(), b.getLanguage(), b.getEdition(),
                b.getTotalCopies(), b.getAvailableCopies(), b.isArchived()
        );
    }

    @Override
    public Book findById(long id) {
        String sql = "SELECT * FROM BOOKS WHERE book_id=?";
        List<Book> list = jdbc.query(sql, bookMapper, id);
        if (list.isEmpty()) throw new BookNotFoundException("No book found: " + id);
        return list.get(0);
    }

    @Override
    public List<Book> findByGenre(String genre) {
        String sql = "SELECT * FROM BOOKS WHERE genre=?";
        return jdbc.query(sql, bookMapper, genre);
    }

    @Override
    public List<Book> findByAuthor(String author) {
        String sql = "SELECT * FROM BOOKS WHERE author_name=?";
        return jdbc.query(sql, bookMapper, author);
    }

    @Override
    public void remove(Book b) {
        jdbc.update("DELETE FROM BOOKS WHERE book_id=?", b.getBookId());
    }

    @Override
    public List<Book> findAll() {
        return List.of();
    }
}
