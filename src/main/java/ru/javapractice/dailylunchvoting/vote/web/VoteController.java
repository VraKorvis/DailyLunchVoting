package ru.javapractice.dailylunchvoting.vote.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javapractice.dailylunchvoting.app.AuthUtil;
import ru.javapractice.dailylunchvoting.vote.model.VoteResult;
import ru.javapractice.dailylunchvoting.vote.service.VoteService;
import ru.javapractice.dailylunchvoting.restaurant.to.VoteTo;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {
    static final String REST_URL = "/api/votes";

    private final VoteService service;

    @GetMapping("/for-today/me")
    public VoteTo getTodayVote() {
        return service.findByUserIdForToday(AuthUtil.get().id());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<VoteResult> vote(@RequestParam int restaurantId) {
        VoteResult result = service.vote(restaurantId, AuthUtil.get().id());
        if (result.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
    }

}
