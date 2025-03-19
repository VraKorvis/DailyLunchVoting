package ru.javapractice.dailylunchvoting.service;

import ru.javapractice.dailylunchvoting.model.Vote;
import ru.javapractice.dailylunchvoting.repository.datajpa.VoteRepository;

import java.time.LocalDateTime;
import java.util.List;

public class VoteService {

    private VoteRepository repository;
    public List<Vote> getAll() {
        repository.getAll();
        return null;
    }

    public Vote create(Vote vote) {
        return repository.save(vote);
    }

    public Vote get(int id, LocalDateTime localDateTime) {
        return repository.get(id, localDateTime);
    }

    public void delete(int id) {
    }

    public void update(Vote vote) {
    }
}
