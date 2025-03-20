package ru.javapractice.dailylunchvoting.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javapractice.dailylunchvoting.model.Vote;
import ru.javapractice.dailylunchvoting.service.VoteService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.javapractice.dailylunchvoting.util.ValidationUtil.assureIdConsistent;
import static ru.javapractice.dailylunchvoting.util.ValidationUtil.checkIsNew;

@Controller
public class VoteController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private VoteService service;

    public List<Vote> getAll() {
        log.info("getAll");
        return service.getAll();
    }

    public Vote get(int userId, LocalDateTime localDateTime) {
        log.info("get");
        return service.get(userId, localDateTime);
    }

    public Vote create(Vote vote) {
        log.info("create {}", vote);
        checkIsNew(vote);
        return service.create(vote);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id);
    }

    public void update(Vote vote, int id) {
        log.info("update {} with id={}", vote, id);
        assureIdConsistent(vote, id);
        service.update(vote);
    }

}
