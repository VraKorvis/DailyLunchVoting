package ru.javapractice.dailylunchvoting.vote.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.javapractice.dailylunchvoting.vote.service.VoteService;
import ru.javapractice.dailylunchvoting.restaurant.to.VoteTo;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping(value = AdminVoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminVoteController {

    static final String REST_URL = "/api/admin/votes";

    private final VoteService service;

    @GetMapping("/for-today")
    public List<VoteTo> getTodayVotes() {
        return service.findAllForToday();
    }

    @GetMapping("/for-today/page")
    public List<VoteTo> getTodayVotesPage(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        return service.findAllForToday(pageable).getContent();
    }
}
