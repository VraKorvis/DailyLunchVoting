package ru.javapractice.dailylunchvoting.vote.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javapractice.dailylunchvoting.common.exception.NotFoundException;
import ru.javapractice.dailylunchvoting.mapper.VoteMapper;
import ru.javapractice.dailylunchvoting.vote.model.Vote;
import ru.javapractice.dailylunchvoting.restaurant.repository.RestaurantRepository;
import ru.javapractice.dailylunchvoting.vote.model.VoteResult;
import ru.javapractice.dailylunchvoting.vote.repository.VoteRepository;
import ru.javapractice.dailylunchvoting.restaurant.to.VoteTo;
import ru.javapractice.dailylunchvoting.user.model.User;
import ru.javapractice.dailylunchvoting.user.repository.UserRepository;
import ru.javapractice.dailylunchvoting.util.OperationTimeChecker;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class VoteService {

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
        var vote = voteRepository.findByUserIdForToday(userId)
                .orElseThrow(() -> new NotFoundException("Vote not found for user id=" + userId + " on today's date"));
        return voteMapper.toVoteTo(vote);
    }

    @Transactional
    public VoteResult vote(int restaurantId, int userId) {

        var refRestaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant with id=" + restaurantId + " not found"));

        var refUser = userRepository.getReferenceById(userId);

        return voteRepository.findByUserIdForToday(userId)
                .map(existingVote -> {
                    if (!OperationTimeChecker.canUpdateVote(existingVote.getVotedAt())) {
                        return createVoteResult(false, "Voting period has ended, you can no longer change your vote", restaurantId);
                    }
                    existingVote.setRestaurant(refRestaurant);
                    return new VoteResult(true, "Your vote has been successfully updated", restaurantId);
                })
                .orElseGet(() -> {
                    if (!OperationTimeChecker.canVote()) {
                        return createVoteResult(false, "Voting period has ended, you can no longer vote", restaurantId);
                    }
                    var vote = new Vote();
                    vote.setUser(refUser);
                    vote.setVotedAt(LocalDate.now());
                    vote.setRestaurant(refRestaurant);
                    voteRepository.save(vote);
                    return createVoteResult(true, "Your vote has been successfully accepted", restaurantId);
                });
    }

    private VoteResult createVoteResult(boolean success, String message, int restaurantId) {
        return new VoteResult(success, message, restaurantId);
    }
}
