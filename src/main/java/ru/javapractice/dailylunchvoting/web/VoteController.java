package ru.javapractice.dailylunchvoting.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import ru.javapractice.dailylunchvoting.model.Vote;
import ru.javapractice.dailylunchvoting.service.VoteService;
import ru.javapractice.dailylunchvoting.util.SecurityUtil;

import java.util.List;

import static ru.javapractice.dailylunchvoting.util.ValidationUtil.assureIdConsistent;

@RestController
public class VoteController {
    protected final Logger log = LoggerFactory.getLogger(VoteController.class);

    @Autowired
    private VoteService service;

    public List<Vote> getAll() {
        log.info("getAll");
        return service.getAll();
    }

    public Vote get(int id) {
        log.info("get "+ id);
        return service.get(id, SecurityUtil.authUserId());
    }

    public Vote createOrUpdate(Vote vote) {
        assureIdConsistent(vote, SecurityUtil.authUserId());
        return service.createOrUpdate(vote, SecurityUtil.authUserId());
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id, SecurityUtil.authUserId());
    }

}
