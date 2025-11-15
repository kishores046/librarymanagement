package com.chinna.librarymanagement.repository.impls.prod;

import com.chinna.librarymanagement.model.Customer;
import com.chinna.librarymanagement.model.Membership;
import com.chinna.librarymanagement.repository.interfaces.CustomerRepository;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;


@Repository
@Profile("prod")
public class JdbcCustomerRepository implements CustomerRepository {
    private final JdbcTemplate jdbc;

    public JdbcCustomerRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<Customer> customerMapper = (rs, rowNum) -> {
        long id = rs.getLong("customer_id");

        Customer c = new Customer(
                id,
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("phone")
        );

        Membership m = new Membership(
                Membership.MembershipType.valueOf(rs.getString("membership_type")),
                rs.getDate("membership_start_date").toLocalDate(),
                rs.getDate("membership_expiry_date").toLocalDate()
        );

        m.setStatus(Membership.MembershipStatus.valueOf(rs.getString("membership_status")));
        m.setMaxBooksAllowedToBeBorrowed(rs.getInt("max_books_allowed"));
        m.setNoOfDaysAllowed(rs.getInt("no_of_days_allowed"));

        c.setMembership(m);
        return c;
    };

    @Override
    public Customer save(Customer c) {

        jdbc.update("""
                        INSERT INTO CUSTOMERS\s
                        (customer_id, name, email, phone, membership_type,
                         membership_start_date, membership_expiry_date,
                         membership_status, max_books_allowed, no_of_days_allowed)
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                        ON DUPLICATE KEY UPDATE\s
                            name = VALUES(name),
                            email = VALUES(email),
                            phone = VALUES(phone),
                            membership_type = VALUES(membership_type),
                            membership_start_date = VALUES(membership_start_date),
                            membership_expiry_date = VALUES(membership_expiry_date),
                            membership_status = VALUES(membership_status),
                            max_books_allowed = VALUES(max_books_allowed),
                            no_of_days_allowed = VALUES(no_of_days_allowed)
               \s""",

                c.getCustomerId(),
                c.getName(),
                c.getEmail(),
                c.getPhone(),
                c.getMembership().getMembershipType().name(),
                Date.valueOf(c.getMembership().getMembershipStartDate()),
                Date.valueOf(c.getMembership().getMembershipExpiryDate()),
                c.getMembership().getStatus().name(),
                c.getMembership().getMaxBooksAllowedToBeBorrowed(),
                c.getMembership().getNoOfDaysAllowed()
        );

        return c;
    }

    @Override
    public Optional<Customer> findById(long id) {
        String sql = "SELECT * FROM CUSTOMERS WHERE customer_id=?";
        List<Customer> list = jdbc.query(sql, customerMapper, id);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.getFirst());
    }

    @Override
    public List<Customer> findAll() {
        return jdbc.query("SELECT * FROM CUSTOMERS", customerMapper);
    }

    @Override
    public Customer updateCustomer(long id, String name, String email, String phone) {
        jdbc.update("UPDATE CUSTOMERS SET name=?, email=?, phone=? WHERE customer_id=?",
                name, email, phone, id);

        Customer c = findById(id).orElseThrow();
        c.setName(name).setEmail(email).setPhone(phone);
        return c;
    }
}
