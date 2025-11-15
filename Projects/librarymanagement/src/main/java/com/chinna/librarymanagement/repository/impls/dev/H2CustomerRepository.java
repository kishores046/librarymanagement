package com.chinna.librarymanagement.repository.impls.dev;

import com.chinna.librarymanagement.model.Customer;
import com.chinna.librarymanagement.repository.interfaces.CustomerRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import com.chinna.librarymanagement.model.Membership;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Repository
@Profile("dev")
public class H2CustomerRepository implements CustomerRepository {

    private final DataSource ds;

    public H2CustomerRepository(DataSource ds) {
        this.ds = ds;
    }

    private Customer map(ResultSet rs) throws SQLException {
        long id = rs.getLong("customer_id");

        Customer c = new Customer(
                id,
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("phone")
        );

        String type = rs.getString("membership_type");

        if (type != null) {
            Membership m = new Membership(
                    Membership.MembershipType.valueOf(type),
                    rs.getDate("membership_start_date").toLocalDate(),
                    rs.getDate("membership_expiry_date").toLocalDate()
            );

            m.setStatus(Membership.MembershipStatus.valueOf(rs.getString("membership_status")));
            m.setMaxBooksAllowedToBeBorrowed(rs.getInt("max_books_allowed"));
            m.setNoOfDaysAllowed(rs.getInt("no_of_days_allowed"));

            c.setMembership(m);
        }

        return c;
    }

    @Override
    public Customer save(Customer c) {
        final String sql = """
            MERGE INTO CUSTOMERS KEY(customer_id)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            Membership m = c.getMembership();

            ps.setLong(1, c.getCustomerId());
            ps.setString(2, c.getName());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getPhone());

            if (m != null) {
                ps.setString(5, m.getMembershipType().name());
                ps.setDate(6, Date.valueOf(m.getMembershipStartDate()));
                ps.setDate(7, Date.valueOf(m.getMembershipExpiryDate()));
                ps.setString(8, m.getStatus().name());
                ps.setInt(9, m.getMaxBooksAllowedToBeBorrowed());
                ps.setInt(10, m.getNoOfDaysAllowed());
            } else {
                ps.setNull(5, Types.VARCHAR);
                ps.setNull(6, Types.DATE);
                ps.setNull(7, Types.DATE);
                ps.setNull(8, Types.VARCHAR);
                ps.setNull(9, Types.INTEGER);
                ps.setNull(10, Types.INTEGER);
            }

            ps.executeUpdate();
            return c;

        } catch (Exception e) {
            throw new RuntimeException("Error saving customer", e);
        }
    }

    @Override
    public Optional<Customer> findById(long id) {
        final String sql = "SELECT * FROM CUSTOMERS WHERE customer_id=?";

        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return Optional.of(map(rs));

            return Optional.empty();

        } catch (Exception e) {
            throw new RuntimeException("Error fetching customer", e);
        }
    }

    @Override
    public List<Customer> findAll() {
        final String sql = "SELECT * FROM CUSTOMERS";

        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Customer> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));
            return list;

        } catch (Exception e) {
            throw new RuntimeException("Error fetching all customers", e);
        }
    }

    @Override
    public Customer updateCustomer(long id, String newName, String newEmail, String newPhone) {
        final String sql = """
            UPDATE CUSTOMERS
            SET name=?, email=?, phone=?
            WHERE customer_id=?
            """;

        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newName);
            ps.setString(2, newEmail);
            ps.setString(3, newPhone);
            ps.setLong(4, id);

            ps.executeUpdate();

            return findById(id).orElseThrow();

        } catch (Exception e) {
            throw new RuntimeException("Error updating customer", e);
        }
    }
}

