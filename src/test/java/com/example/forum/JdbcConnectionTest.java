package com.example.forum;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class JdbcConnectionTest {

    @Autowired
    private DataSource dataSource;

    @Test
    void testJdbcConnection() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            assertNotNull(conn);
            System.out.println("JDBC连接成功: " + conn.getMetaData().getURL());
        }
    }
}