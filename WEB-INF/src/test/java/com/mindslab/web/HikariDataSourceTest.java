package com.mindslab.web;

import java.sql.Connection;

import javax.sql.DataSource;

import com.mindslab.WebApplication;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest(classes = WebApplication.class, properties = "spring.profiles.active:local")
@ActiveProfiles("local")
@Slf4j
public class HikariDataSourceTest {

    @Autowired
    @Qualifier("mariaDataSource")
    private DataSource dataSource;

    @Test
    public void hikariDataSourceTest() {
        log.info("hikariDataSourceTest");
        try {
            log.info("hikariDataSourceTest Start");
            Connection con = dataSource.getConnection();
            log.info("'Connection객체 : " + con + "'");
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }
    }

}
