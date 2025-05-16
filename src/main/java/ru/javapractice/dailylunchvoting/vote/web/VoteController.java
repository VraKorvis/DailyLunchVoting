package ru.javapractice.dailylunchvoting.vote.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javapractice.dailylunchvoting.app.AuthUserProvider;
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
    private final AuthUserProvider authUtil;

    @GetMapping("/for-today/me")
    @ResponseBody
    public VoteTo getTodayVote() {
        return service.findByUserIdForToday(authUtil.get().id());
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<VoteResult> vote(@RequestParam int restaurantId) {
        var result = service.vote(restaurantId, authUtil.get().id());
        if (result.success()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
    }

}
