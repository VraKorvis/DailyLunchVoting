package ru.javapractice.dailylunchvoting.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javapractice.dailylunchvoting.model.User;
import ru.javapractice.dailylunchvoting.service.testdata.UserData;

import static org.junit.Assert.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:/spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class UserServiceTest {

    @Autowired
    private UserService service;

    @Test
    public void create() {
        User created = service.create(UserData.getNew());
        int userId = created.id();
        User newUser = UserData.getNew();
        newUser.setId(userId);
        UserData.USER_MATCHER.assertMatch(newUser, created);
    }

    @Test
    public void delete() {
        assertThrows(UnsupportedOperationException.class, () -> service.delete(UserData.USER1_ID));
    }

    @Test
    public void setEnable() {
        service.setEnable(UserData.USER1_ID, false);
        assertFalse(service.get(UserData.USER1_ID).isEnabled());
    }

    @Test
    public void get() {
        UserData.USER_MATCHER.assertMatch(service.get(UserData.USER1_ID), UserData.USER_1);
    }

    @Test
    public void getByEmail() {
        UserData.USER_MATCHER.assertMatch(service.getByEmail(UserData.USER1_EMAIL), UserData.USER_1);
    }

    @Test
    public void getAll() {
        UserData.USER_MATCHER.assertMatch(service.getAll(), UserData.ADMIN, UserData.GUEST, UserData.USER_1, UserData.USER_2);
    }

    @Test
    public void update() {
        User updated = UserData.getUpdated(UserData.USER_1);
        service.update(updated);
        UserData.USER_MATCHER.assertMatch(service.get(UserData.USER1_ID), updated);
    }
}