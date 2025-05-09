package ru.javapractice.dailylunchvoting.restaurant.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javapractice.dailylunchvoting.app.AuthUtil;
import ru.javapractice.dailylunchvoting.restaurant.model.Vote;
import ru.javapractice.dailylunchvoting.restaurant.service.VoteService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {
    static final String REST_URL = "/api/votes";

    private final VoteService service;

    public VoteController(VoteService service) {
        this.service = service;
    }

    @GetMapping
    public List<Vote> getAll() {
        return service.getAll();
    }

    @GetMapping("/today")
    public Vote getTodayVote() {
        return service.findByUserIdForToday(AuthUtil.get().id());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void vote(@RequestParam int restaurantId) {
        service.vote(restaurantId, AuthUtil.get().id());
    }

}
