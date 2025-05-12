package ru.javapractice.dailylunchvoting.restaurant.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javapractice.dailylunchvoting.app.AuthUtil;
import ru.javapractice.dailylunchvoting.restaurant.service.VoteService;
import ru.javapractice.dailylunchvoting.restaurant.to.VoteTo;

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

    @GetMapping("/for-today")
    public List<VoteTo> getTodayVotes() {
        return service.getAllForToday();
    }

    @GetMapping("/for-today/me")
    public VoteTo getTodayVote() {
        return service.findByUserIdForToday(AuthUtil.get().id());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void vote(@RequestParam int restaurantId) {
        service.vote(restaurantId, AuthUtil.get().id());
    }

}
