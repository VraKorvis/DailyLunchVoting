package ru.javapractice.dailylunchvoting.restaurant.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javapractice.dailylunchvoting.common.error.ErrorType;
import ru.javapractice.dailylunchvoting.common.exception.AppException;
import ru.javapractice.dailylunchvoting.common.exception.NotFoundException;
import ru.javapractice.dailylunchvoting.mapper.VoteMapper;
import ru.javapractice.dailylunchvoting.restaurant.model.Restaurant;
import ru.javapractice.dailylunchvoting.restaurant.model.Vote;
import ru.javapractice.dailylunchvoting.restaurant.repository.RestaurantRepository;
import ru.javapractice.dailylunchvoting.restaurant.repository.VoteRepository;
import ru.javapractice.dailylunchvoting.restaurant.to.VoteTo;
import ru.javapractice.dailylunchvoting.user.model.User;
import ru.javapractice.dailylunchvoting.user.repository.UserRepository;
import ru.javapractice.dailylunchvoting.util.OperationTimeChecker;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class VoteService {

    private final Logger log = LoggerFactory.getLogger(VoteService.class);

    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final VoteMapper voteMapper;

    public List<Vote> getAllByUserId(int userId) {
        return voteRepository.findAllByUserId(userId);
    }

    public List<VoteTo> getAllForToday() {
        return voteMapper.toVoteTos(voteRepository.findAllForToday());
    }

    public List<Vote> getAllWithRestaurantByUserId(int userId) {
        return voteRepository.findAllByUserId(userId);
    }

    public VoteTo findByUserIdForToday(int userId) {
        Vote vote = voteRepository.findByUserIdForToday(userId)
                .orElseThrow(() -> new NotFoundException("Vote not found for user id=" + userId + " on today's date"));
        return voteMapper.toVoteTo(vote);
    }

    @Transactional
    public Vote vote(int restaurantId, int userId) {

        User refUser = userRepository.getReferenceById(userId);
        Restaurant refRestaurant = restaurantRepository.getReferenceById(restaurantId);

        var restaurantOpt = restaurantRepository.findByIdWithMenuForToday(restaurantId);
        if (restaurantOpt.isEmpty()) {
            throw new NotFoundException(String.format("Cannot vote for restaurant (id=%d) because it has no menu assigned for today", restaurantId));
        }

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

                    return voteRepository.save(vote);
                });
    }
}
