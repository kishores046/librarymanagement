package com.chinna.librarymanagement.repository.impls.dev;

import com.chinna.librarymanagement.exceptions.BookNotFoundException;
import com.chinna.librarymanagement.model.Book;
import com.chinna.librarymanagement.repository.interfaces.BookRepository;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@Profile("dev")
public class H2BookRepository implements BookRepository {

    private final DataSource ds;

    public H2BookRepository(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public void save(Book b) {
        String sql = """
                INSERT INTO BOOKS 
                (book_id, book_name, author_name, isbn, publisher, published_date, 
                 genre, language, edition, total_copies, available_copies, archived)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
                """;

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, b.getBookId());
            ps.setString(2, b.getBookName());
            ps.setString(3, b.getAuthorName());
            ps.setString(4, b.getIsbn());
            ps.setString(5, b.getPublisher());
            ps.setDate(6, b.getPublishedDate() == null ? null : Date.valueOf(b.getPublishedDate()));
            ps.setString(7, b.getGenre());
            ps.setString(8, b.getLanguage());
            ps.setString(9, b.getEdition());
            ps.setInt(10, b.getTotalCopies());
            ps.setInt(11, b.getAvailableCopies());
            ps.setBoolean(12, b.isArchived());

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Book findById(long id) {
        String sql = "SELECT * FROM BOOKS WHERE book_id=?";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return mapRow(rs);

            throw new BookNotFoundException("Book not found: " + id);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Book> findByGenre(String genre) {
        String sql = "SELECT * FROM BOOKS WHERE genre=?";
        List<Book> list = new ArrayList<>();

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, genre);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) list.add(mapRow(rs));

            return list;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Book> findByAuthor(String author) {
        String sql = "SELECT * FROM BOOKS WHERE author_name=?";
        List<Book> list = new ArrayList<>();

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, author);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) list.add(mapRow(rs));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (list.isEmpty()) throw new BookNotFoundException("No books by " + author);
        return list;
    }

    @Override
    public void remove(Book b) {
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM BOOKS WHERE book_id=?")) {

            ps.setLong(1, b.getBookId());
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public List<Book> findAll() {
        return List.of();
    }

    private Book mapRow(ResultSet rs) throws Exception {
        Book b = new Book(
                rs.getLong("book_id"),
                rs.getString("book_name"),
                rs.getString("author_name"),
                rs.getString("isbn"),
                rs.getString("publisher"),
                rs.getDate("published_date") == null ? null : rs.getDate("published_date").toLocalDate(),
                rs.getInt("total_copies")
        );

        b.setAvailableCopies(rs.getInt("available_copies"));
        b.setGenre(rs.getString("genre"));
        b.setLanguage(rs.getString("language"));
        b.setEdition(rs.getString("edition"));
        b.setArchived(rs.getBoolean("archived"));

        return b;
    }
}
