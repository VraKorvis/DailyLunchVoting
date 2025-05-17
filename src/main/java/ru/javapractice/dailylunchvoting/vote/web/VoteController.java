package ru.javapractice.dailylunchvoting.vote.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.javapractice.dailylunchvoting.app.AuthUser;
import ru.javapractice.dailylunchvoting.vote.model.VoteResult;
import ru.javapractice.dailylunchvoting.vote.service.VoteService;
import ru.javapractice.dailylunchvoting.restaurant.to.VoteTo;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {
    static final String REST_URL = "/api/votes";

    private final VoteService service;

    @GetMapping("/for-today/me")
    @ResponseBody
    public VoteTo getTodayVote(@AuthenticationPrincipal AuthUser authUser) {
        return service.findByUserIdForToday(authUser.id());
    }

    @GetMapping
    @ResponseBody
    public List<VoteTo> getAll(@AuthenticationPrincipal AuthUser authUser) {
        return service.findAllByUserId(authUser.id());
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<VoteResult> vote(@RequestParam int restaurantId, @AuthenticationPrincipal AuthUser authUser) {
        var result = service.vote(restaurantId, authUser.id());
        if (result.success()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
    }

}
