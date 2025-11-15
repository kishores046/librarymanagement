package com.chinna.librarymanagement.repository.impls.dev;

import com.chinna.librarymanagement.model.BorrowRecord;
import com.chinna.librarymanagement.repository.interfaces.BorrowRecordRepository;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.*;
import java.sql.Date;
import java.util.*;
@Repository
@Profile("dev")
public class H2BorrowRecordRepository implements BorrowRecordRepository {

    private final DataSource ds;

    public H2BorrowRecordRepository(DataSource ds) {
        this.ds = ds;
    }

    private BorrowRecord map(ResultSet rs) throws SQLException {

        BorrowRecord br = new BorrowRecord(
                rs.getLong("borrow_id"),
                rs.getLong("customer_id"),
                rs.getLong("book_id"),
                rs.getDate("borrow_date").toLocalDate(),
                rs.getDate("due_date").toLocalDate()
        );

        Date actual = rs.getDate("actual_return_date");
        if (actual != null)
            br.setActualReturnDate(actual.toLocalDate());

        br.setReturned(rs.getBoolean("returned"));
        br.setFineAmount(rs.getInt("fine_amount"));
        br.setDaysOverdue(rs.getInt("days_overdue"));
        br.setBorrowStatus(BorrowRecord.BorrowStatus.valueOf(rs.getString("status")));

        return br;
    }

    @Override
    public BorrowRecord save(BorrowRecord br) {

        final String sql = """
            INSERT INTO BORROW_RECORDS
            (customer_id, book_id, borrow_date, due_date, actual_return_date,
             returned, fine_amount, days_overdue, status, created_at, updated_at)
            VALUES (?, ?, ?, ?, NULL, ?, ?, ?, ?, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP())
            """;

        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, br.getCustomerId());
            ps.setLong(2, br.getBookId());
            ps.setDate(3, Date.valueOf(br.getBorrowDate()));
            ps.setDate(4, Date.valueOf(br.getDueDate()));
            ps.setBoolean(5, br.isReturned());
            ps.setInt(6, br.getFineAmount());
            ps.setInt(7, br.getDaysOverdue());
            ps.setString(8, br.getBorrowStatus().name());

            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                br.setBorrowId(keys.getLong(1));
            }

            return br;

        } catch (Exception e) {
            throw new RuntimeException("Error saving borrow record", e);
        }
    }

    @Override
    public Optional<BorrowRecord> findById(long id) {
        final String sql = "SELECT * FROM BORROW_RECORDS WHERE borrow_id=?";

        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return Optional.of(map(rs));

            return Optional.empty();

        } catch (Exception e) {
            throw new RuntimeException("Error fetching borrow record", e);
        }
    }

    @Override
    public List<BorrowRecord> findActiveByCustomer(long customerId) {

        final String sql = """
            SELECT * FROM BORROW_RECORDS
            WHERE customer_id=? AND returned=FALSE
            """;

        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, customerId);
            ResultSet rs = ps.executeQuery();

            List<BorrowRecord> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));

            return list;

        } catch (Exception e) {
            throw new RuntimeException("Error fetching active borrows", e);
        }
    }

    @Override
    public List<BorrowRecord> findActiveByBook(long bookId) {

        final String sql = """
            SELECT * FROM BORROW_RECORDS
            WHERE book_id=? AND returned=FALSE
            """;

        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, bookId);
            ResultSet rs = ps.executeQuery();

            List<BorrowRecord> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));

            return list;

        } catch (Exception e) {
            throw new RuntimeException("Error fetching active borrows of book", e);
        }
    }

    @Override
    public void update(BorrowRecord br) {

        final String sql = """
            UPDATE BORROW_RECORDS SET
                actual_return_date = ?,
                returned = ?,
                fine_amount = ?,
                days_overdue = ?,
                status = ?,
                updated_at = CURRENT_TIMESTAMP()
            WHERE borrow_id = ?
            """;

        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (br.getActualReturnDate() == null)
                ps.setNull(1, Types.DATE);
            else
                ps.setDate(1, Date.valueOf(br.getActualReturnDate()));

            ps.setBoolean(2, br.isReturned());
            ps.setInt(3, br.getFineAmount());
            ps.setInt(4, br.getDaysOverdue());
            ps.setString(5, br.getBorrowStatus().name());
            ps.setLong(6, br.getBorrowId());

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Error updating borrow record", e);
        }
    }
}
