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

    public Vote create(Vote vote, int userId) {
        Assert.notNull(vote, "vote must not be null");
        return processVote(vote, userId);
    }

    public Vote get(int id, int userId) {
        return checkNotFound(repository.get(id, userId), id);
    }

    //TODO probably dont need delete votes
    public void delete(int id, int userId) {
        checkNotFound(repository.delete(id, userId), id);
    }

    public void update(Vote vote, int userId) {
        if (VotingTimeChecker.isVotingTimeExpired()){
            log.info("The time for voting has expired. You can't revote for the restaurant.");
            throw new VotingProcessException("The time for voting has expired. You can't vote for the restaurant.");
        }
        else {
            checkNotFound(repository.save(vote, userId), vote.id());
        }
    }

    private Vote processVote(Vote vote, int userId) {
        if (VotingTimeChecker.isVotingTimeExpired()) {
            log.info("The time for voting has expired. You can't vote for the restaurant.");
            throw new VotingProcessException("The time for voting has expired. You can't vote for the restaurant.");
        } else {
            log.info("The vote was successfully processed.");
            return repository.save(vote, userId);
        }
    }
}
