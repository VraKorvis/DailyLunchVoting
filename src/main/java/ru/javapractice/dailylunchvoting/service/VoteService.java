package ru.javapractice.dailylunchvoting.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.javapractice.dailylunchvoting.model.Restaurant;
import ru.javapractice.dailylunchvoting.model.User;
import ru.javapractice.dailylunchvoting.model.Vote;
import ru.javapractice.dailylunchvoting.repository.ProfileRepository;
import ru.javapractice.dailylunchvoting.repository.RestaurantRepository;
import ru.javapractice.dailylunchvoting.repository.VoteRepository;
import ru.javapractice.dailylunchvoting.util.VotingTimeChecker;
import ru.javapractice.dailylunchvoting.util.exception.VotingProcessException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static ru.javapractice.dailylunchvoting.util.ValidationUtil.checkNotFound;

@Service
public class VoteService {

    private final Logger log = LoggerFactory.getLogger(VoteService.class);

    private final VoteRepository voteRepository;
    private final ProfileRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    public VoteService(VoteRepository voteRepository, ProfileRepository userRepository, RestaurantRepository restaurantRepository) {
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public List<Vote> getAll() {
        return voteRepository.getAll();
    }

    public List<Vote> getAllByUserId(int userId) {
        return voteRepository.getAllByUserId(userId);
    }

    public Vote get(int id, int userId) {
        return checkNotFound(voteRepository.get(id, userId), id);
    }

    public Vote getWithRestaurant(int id, int userId) {
        return checkNotFound(voteRepository.getWithRestaurant(id, userId), id);
    }

    public Optional<Vote> getWithRestaurantForToday(int userId) {
        return checkNotFound(voteRepository.findByUserIdForToday(userId), userId);
    }

    public List<Vote> getAllWithRestaurantForToday() {
        return voteRepository.getAllWithRestaurantForToday();
    }

    public List<Vote> getAllWithRestaurantByUserId(int userId) {
        return voteRepository.getAllWithRestaurantByUserId(userId);
    }

    @Transactional
    public Vote vote(Vote vote, int id) {
        Assert.notNull(vote, "vote must not be null");

        User refUser = userRepository.getReferenceById(id);
        Restaurant refRestaurant = restaurantRepository.getReferenceById(vote.getRestaurant().getId());

       return voteRepository.findByUserIdForToday(id)
                .map(existingVote -> {
                    if (!VotingTimeChecker.canUpdateVote(existingVote)) {
                        throw new VotingProcessException("It's too late to change your vote");
                    }
                    existingVote.setRestaurant(refRestaurant);
                    return existingVote;
                })
                .orElseGet(() -> {
                    if (!VotingTimeChecker.canVoteToday()) {
                        throw new VotingProcessException("It's too late to vote");
                    }
                    vote.setUser(refUser);
                    vote.setDate(LocalDate.now());
                    vote.setRestaurant(refRestaurant);
                    voteRepository.save(vote, refUser.id());
                    return vote;
                });
    }

    public void delete(int id, int userId) {
        voteRepository.delete(id, userId);
    }


}
