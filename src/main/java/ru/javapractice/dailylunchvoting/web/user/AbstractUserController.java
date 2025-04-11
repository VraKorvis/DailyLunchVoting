package ru.javapractice.dailylunchvoting.web.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import ru.javapractice.dailylunchvoting.model.User;
import ru.javapractice.dailylunchvoting.service.UserService;

import java.util.List;

import static ru.javapractice.dailylunchvoting.util.ValidationUtil.assureIdConsistent;
import static ru.javapractice.dailylunchvoting.util.ValidationUtil.checkIsNew;

public abstract class AbstractUserController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService service;

    public List<User> getAll() {
        log.info("getAll");
        return service.getAll();
    }

    public User get(int id) {
        log.info("get {}", id);
        return service.get(id);
    }

    public User create(User user) {
        log.info("create {}", user);
        checkIsNew(user);
        return service.create(user);
    }

    public void setEnable(@PathVariable int id, boolean enable) {
        log.info("set enable id {} {}", id, enable);
        service.setEnable(id, enable);
    }

    public void update(User user, int id) {
        log.info("update {} with id={}", user, id);
        assureIdConsistent(user, id);
        service.update(user);
    }

    public User getByMail(String email) {
        log.info("getByEmail {}", email);
        return service.getByEmail(email);
    }

}