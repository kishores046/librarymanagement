package com.chinna.librarymanagement.config.profiles;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;


@Configuration
@Profile("dev")
public class H2DevConfig {

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriverClass(org.h2.Driver.class);
        ds.setUrl("jdbc:h2:file:C:/Users/Kishore/Projects/librarymanagement/data/libdb;MODE=MYSQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1;AUTO_SERVER=TRUE");
        ds.setUsername("chinna");
        ds.setPassword("");
        return ds;
    }


    @Bean
    public Boolean initializeSchema(DataSource dataSource) {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("CREATE TABLE IF NOT EXISTS BOOKS ("
                    + "book_id BIGINT PRIMARY KEY, "
                    + "book_name VARCHAR(200), "
                    + "author_name VARCHAR(100), "
                    + "isbn VARCHAR(50), "
                    + "publisher VARCHAR(100), "
                    + "published_date DATE, "
                    + "genre VARCHAR(50), "
                    + "language VARCHAR(50), "
                    + "edition VARCHAR(50), "
                    + "total_copies INT, "
                    + "available_copies INT, "
                    + "archived BOOLEAN"
                    + ");");

            stmt.execute("CREATE TABLE IF NOT EXISTS CUSTOMERS ("
                    + "customer_id BIGINT PRIMARY KEY, "
                    + "name VARCHAR(100) NOT NULL, "
                    + "email VARCHAR(150), "
                    + "phone VARCHAR(20), "
                    + "membership_type VARCHAR(20), "
                    + "membership_start_date DATE, "
                    + "membership_expiry_date DATE, "
                    + "membership_status VARCHAR(20), "
                    + "max_books_allowed INT, "
                    + "no_of_days_allowed INT"
                    + ");");

            stmt.execute("CREATE TABLE IF NOT EXISTS BORROW_RECORDS ("
                    + "borrow_id BIGINT AUTO_INCREMENT PRIMARY KEY, "
                    + "customer_id BIGINT NOT NULL, "
                    + "book_id BIGINT NOT NULL, "
                    + "borrow_date DATE NOT NULL, "
                    + "due_date DATE NOT NULL, "
                    + "actual_return_date DATE, "
                    + "returned BOOLEAN DEFAULT FALSE, "
                    + "fine_amount INT DEFAULT 0, "
                    + "days_overdue INT DEFAULT 0, "
                    + "status VARCHAR(20) NOT NULL, "
                    + "created_at TIMESTAMP NOT NULL, "
                    + "updated_at TIMESTAMP NOT NULL"
                    + ");");

            return Boolean.TRUE;
        } catch (Exception e) {
            // log and rethrow as unchecked so startup fails visibly if schema can't be created
            e.printStackTrace();
            throw new IllegalStateException("Failed to initialize H2 schema", e);
        }
    }
}
