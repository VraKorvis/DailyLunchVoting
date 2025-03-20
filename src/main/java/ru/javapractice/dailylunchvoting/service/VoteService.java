package ru.javapractice.dailylunchvoting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javapractice.dailylunchvoting.model.Vote;
import ru.javapractice.dailylunchvoting.repository.VoteRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VoteService {

    @Autowired
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
