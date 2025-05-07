package ru.javapractice.dailylunchvoting.restaurant.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

@SpringBootTest
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Test
    public void create() {
//        menuService.create();
    }

    @Test
    public void get() {
    }

    @Test
    public void getAll() {
    }

    @Test
    public void update() {
    }
}