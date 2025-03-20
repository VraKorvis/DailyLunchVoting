package ru.javapractice.dailylunchvoting.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javapractice.dailylunchvoting.service.MenuService;
import ru.javapractice.dailylunchvoting.web.user.ProfileRestController;

import java.awt.*;
import java.util.List;

@Controller
public class MenuRestController {
    private final Logger log = LoggerFactory.getLogger(ProfileRestController.class);

    @Autowired
    private MenuService menuService;

    public List<Menu> getAll() {
        log.info("getAll()");
        return null;
    }

    public Menu get(int id) {
        log.info("get({})", id);
        return null;
    }

    public Menu create(Menu menu) {
        log.info("create({})", menu);
        return null;
    }

    public void delete(int id) {
        log.info("delete({})", id);
    }

    public void update(Menu menu) {
        log.info("update({})", menu);
    }


}
