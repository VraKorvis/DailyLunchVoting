package ru.javapractice.dailylunchvoting.web.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javapractice.dailylunchvoting.model.User;
import ru.javapractice.dailylunchvoting.service.UserService;

import java.util.List;

public class ProfileRestController {

    private final Logger log = LoggerFactory.getLogger(ProfileRestController.class);

    private UserService service;

    public List<User> getAll() {
        log.info("getAll");

        return null;
    }

    public User get(int id) {
        log.info("get {}", id);
        return null;
    }

    public User create(User user) {
        log.info("create {}", user);
        return null;
    }

    public void delete(int id) {
        log.info("delete {}", id);
    }

    public void update(User user, int id) {
        log.info("update {}", id);
    }

    public User getByMail(String email) {
        log.info("getByMail {}", email);
        return null;
    }
}
