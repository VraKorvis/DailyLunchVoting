package ru.javapractice.dailylunchvoting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javapractice.dailylunchvoting.model.Vote;
import ru.javapractice.dailylunchvoting.repository.VoteRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.javapractice.dailylunchvoting.util.ValidationUtil.checkNotFound;

@Service
public class VoteService {

    @Autowired
    private VoteRepository repository;

    public List<Vote> getAll() {
        repository.getAll();
        return null;
    }

    public Vote create(Vote vote, int userId) {
        return repository.save(vote, userId);
    }

    public Vote get(int id, int userId) {
        return checkNotFound(repository.get(id, userId), id);
    }

    public void delete(int id, int userId) {
        checkNotFound(repository.delete(id, userId), id);
    }

    public void update(Vote vote, int userId) {
        checkNotFound(repository.save(vote, userId), vote.id());
    }
}
