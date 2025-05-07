package ru.javapractice.dailylunchvoting.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javapractice.dailylunchvoting.restaurant.model.Vote;
import ru.javapractice.dailylunchvoting.restaurant.service.VoteService;
import ru.javapractice.dailylunchvoting.util.SecurityUtil;

import java.util.List;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {
    protected final Logger log = LoggerFactory.getLogger(VoteController.class);
    static final String REST_URL = "/api/votes";

    private final VoteService service;

    public VoteController(VoteService service) {
        this.service = service;
    }

    @GetMapping
    public List<Vote> getAll() {
        log.info("getAll");
        return service.getAll();
    }

    @GetMapping("/today")
    public Vote getTodayVote() {
        log.info("getTodayVote");
        return service.findByUserIdForToday(SecurityUtil.authUserId());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void vote(@RequestParam int restaurantId) {
        log.info("vote {}", restaurantId);
        service.vote(restaurantId, SecurityUtil.authUserId());
    }

}
