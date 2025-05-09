package ru.javapractice.dailylunchvoting.restaurant.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javapractice.dailylunchvoting.common.error.ErrorType;
import ru.javapractice.dailylunchvoting.common.exception.AppException;
import ru.javapractice.dailylunchvoting.common.exception.NotFoundException;
import ru.javapractice.dailylunchvoting.restaurant.model.Restaurant;
import ru.javapractice.dailylunchvoting.restaurant.model.Vote;
import ru.javapractice.dailylunchvoting.restaurant.repository.RestaurantRepository;
import ru.javapractice.dailylunchvoting.restaurant.repository.VoteRepository;
import ru.javapractice.dailylunchvoting.user.model.User;
import ru.javapractice.dailylunchvoting.user.repository.UserRepository;
import ru.javapractice.dailylunchvoting.util.OperationTimeChecker;

import java.time.LocalDate;
import java.util.List;

@Service
public class VoteService {

    private final Logger log = LoggerFactory.getLogger(VoteService.class);

    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    public VoteService(VoteRepository voteRepository, UserRepository userRepository, RestaurantRepository restaurantRepository) {
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public List<Vote> getAll() {
        return voteRepository.findAll();
    }

    public List<Vote> getAllByUserId(int userId) {
        return voteRepository.getAllByUserId(userId);
    }

    public List<Vote> getAllWithRestaurantForToday() {
        return voteRepository.getAllForToday();
    }

    public List<Vote> getAllWithRestaurantByUserId(int userId) {
        return voteRepository.getAllByUserId(userId);
    }

    public Vote findByUserIdForToday(int userId) {
        return voteRepository.findByUserIdForToday(userId)
                .orElseThrow(() -> new NotFoundException("Vote not found for user id=" + userId + " on today's date"));
    }

    @Transactional
    public Vote vote(int restaurantId, int userId) {

        User refUser = userRepository.getReferenceById(userId);
        Restaurant refRestaurant = restaurantRepository.getReferenceById(restaurantId);

       return voteRepository.findByUserIdForToday(userId)
                .map(existingVote -> {
                    if (!OperationTimeChecker.canUpdate(existingVote)) {
                        throw new AppException("It's too late to change your vote", ErrorType.APP_ERROR);
                    }
                    existingVote.setRestaurant(refRestaurant);
                    return existingVote;
                })
                .orElseGet(() -> {
                    if (!OperationTimeChecker.canVote()) {
                        throw new AppException("It's too late to vote", ErrorType.APP_ERROR);
                    }
                    Vote vote = new Vote();
                    vote.setUser(refUser);
                    vote.setDate(LocalDate.now());
                    vote.setRestaurant(refRestaurant);
                    voteRepository.save(vote);
                    return vote;
                });
    }
}
