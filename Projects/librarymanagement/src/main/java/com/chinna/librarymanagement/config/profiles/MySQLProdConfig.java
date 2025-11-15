package com.chinna.librarymanagement.config.profiles;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

@Configuration
@Profile("prod")
public class MySQLProdConfig {

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriverClass(com.mysql.cj.jdbc.Driver.class);
        ds.setUrl("jdbc:mysql://localhost:3306/library");
        ds.setUsername("root");
        ds.setPassword("12345");  // replace with env variable
        return ds;
    }
}
