package ru.javapractice.dailylunchvoting.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.javapractice.dailylunchvoting.model.Vote;
import ru.javapractice.dailylunchvoting.repository.VoteRepository;
import ru.javapractice.dailylunchvoting.util.VotingTimeChecker;
import ru.javapractice.dailylunchvoting.util.exception.VotingProcessException;

import java.util.List;

import static ru.javapractice.dailylunchvoting.util.ValidationUtil.checkNotFound;

@Service
public class VoteService {

    private final Logger log = LoggerFactory.getLogger(VoteService.class);

    private final VoteRepository repository;

    public VoteService(VoteRepository repository) {
        this.repository = repository;
    }

    public List<Vote> getAll() {
        return repository.getAll();
    }

    public List<Vote> getAllByUserId(int userId) {
        return repository.getAllByUserId(userId);
    }

    public Vote get(int id, int userId) {
        return checkNotFound(repository.get(id, userId), id);
    }

    public Vote getWithRestaurant(int id, int userId) {
        return checkNotFound(repository.getWithRestaurant(id, userId), id);
    }

    public List<Vote> getAllWithRestaurantForToday() {
        return repository.getAllWithRestaurantForToday();
    }

    public List<Vote> getAllWithRestaurantByUserId(int userId) {
        return repository.getAllWithRestaurantByUserId(userId);
    }

    public Vote create(Vote vote, int userId) {
        Assert.notNull(vote, "vote must not be null");
        if (canVote(vote)) {
            return repository.save(vote, userId);
        } else {
            throw new VotingProcessException("it is too late, vote can't be changed");
        }
    }

    public void update(Vote vote, int userId) {
        if (canVote(vote)) {
            log.info("The revote was successfully processed.");
            checkNotFound(repository.save(vote, userId), vote.id());
        } else {
            throw new VotingProcessException("it is too late, vote can't be changed");
        }
    }

    public boolean canVote(Vote vote) {
        if (VotingTimeChecker.isVotingTimeExpired()) {
            return false;
        }
        return VotingTimeChecker.isToday(vote.getDate());
    }

    public void delete(int id, int userId) {
        repository.delete(id, userId);
    }

}
