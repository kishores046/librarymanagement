package com.chinna.librarymanagement.repository.impls.prod;

import com.chinna.librarymanagement.model.BorrowRecord;
import com.chinna.librarymanagement.repository.interfaces.BorrowRecordRepository;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("prod")
public class JdbcBorrowRecordRepository implements BorrowRecordRepository {
    private final JdbcTemplate jdbc;

    public JdbcBorrowRecordRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<BorrowRecord> mapper = (rs, rowNum) -> map(rs);

    private BorrowRecord map(ResultSet rs) throws SQLException {
        BorrowRecord br = new BorrowRecord(
                rs.getLong("borrow_id"),
                rs.getLong("customer_id"),
                rs.getLong("book_id"),
                rs.getDate("borrow_date").toLocalDate(),
                rs.getDate("due_date").toLocalDate()
        );

        Date actual = rs.getDate("actual_return_date");
        if (actual != null) br.setActualReturnDate(actual.toLocalDate());

        br.setReturned(rs.getBoolean("returned"));
        br.setFineAmount(rs.getInt("fine_amount"));
        br.setDaysOverdue(rs.getInt("days_overdue"));
        br.setBorrowStatus(BorrowRecord.BorrowStatus.valueOf(rs.getString("status")));

        return br;
    }

    @Override
    public BorrowRecord save(BorrowRecord br) {

        jdbc.update("""
                        INSERT INTO BORROW_RECORDS
                        (customer_id, book_id, borrow_date, due_date, actual_return_date,
                         returned, fine_amount, days_overdue, status, created_at, updated_at)
                        VALUES (?, ?, ?, ?, NULL, ?, ?, ?, ?, NOW(), NOW())
                """,
                br.getCustomerId(),
                br.getBookId(),
                Date.valueOf(br.getBorrowDate()),
                Date.valueOf(br.getDueDate()),
                br.isReturned(),
                br.getFineAmount(),
                br.getDaysOverdue(),
                br.getBorrowStatus().name()
        );

        Long id = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        br.setBorrowId(id);

        return br;
    }

    @Override
    public Optional<BorrowRecord> findById(long id) {
        String sql = "SELECT * FROM BORROW_RECORDS WHERE borrow_id=?";
        List<BorrowRecord> list = jdbc.query(sql, mapper, id);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public List<BorrowRecord> findActiveByCustomer(long customerId) {
        return jdbc.query(
                "SELECT * FROM BORROW_RECORDS WHERE customer_id=? AND returned=false",
                mapper,
                customerId
        );
    }

    @Override
    public List<BorrowRecord> findActiveByBook(long bookId) {
        String sql = """
            SELECT * FROM BORROW_RECORDS
            WHERE book_id = ? AND returned = FALSE
            """;

        return jdbc.query(sql, mapper, bookId);
    }

    @Override
    public void update(BorrowRecord br) {
        jdbc.update("""
                        UPDATE BORROW_RECORDS SET
                            actual_return_date=?,
                            returned=?,
                            fine_amount=?,
                            days_overdue=?,
                            status=?,
                            updated_at=NOW()
                        WHERE borrow_id=?
                """,
                br.getActualReturnDate() == null ? null :
                        Date.valueOf(br.getActualReturnDate()),
                br.isReturned(),
                br.getFineAmount(),
                br.getDaysOverdue(),
                br.getBorrowStatus().name(),
                br.getBorrowId()
        );
    }
}
