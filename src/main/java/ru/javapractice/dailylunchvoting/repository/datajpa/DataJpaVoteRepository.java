package ru.javapractice.dailylunchvoting.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javapractice.dailylunchvoting.model.User;
import ru.javapractice.dailylunchvoting.model.Vote;
import ru.javapractice.dailylunchvoting.repository.VoteRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class DataJpaVoteRepository implements VoteRepository {
    private static final Sort SORT_NAME = Sort.by(Sort.Direction.DESC, "date", "id");

    private final ProxyCrudVoteRepository voteRepository;
    private final ProxyCrudProfileRepository userRepository;

    public DataJpaVoteRepository(ProxyCrudVoteRepository voteRepository, ProxyCrudProfileRepository userRepository) {
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Vote> getAll() {
        return voteRepository.findAll(SORT_NAME);
    }

    @Override
    public List<Vote> getAllByUserId(int userId) {
        return voteRepository.getAllByUserId(userId);
    }

    @Override
    public List<Vote> getAllWithRestaurantForToday() {
        return voteRepository.getAllWithRestaurantForToday();
    }

    @Override
    public List<Vote> getAllWithRestaurantByUserId(int userId) {
        return voteRepository.getAllWithRestaurantByUserId(userId);
    }

    @Override
    public Vote get(int id, int userId) {
        return voteRepository.findById(id)
                .filter(v -> Objects.equals(v.getUser().getId(), userId))
                .orElse(null);
    }

    @Override
    public Vote getWithRestaurant(int id, int userId) {
        return voteRepository.getWithRestaurant(id, userId)
                .filter(v -> Objects.equals(v.getUser().getId(), userId))
                .orElse(null);
    }

    @Override
    @Transactional
    public Vote createOrUpdate(Vote vote, int userId) {
        User user = userRepository.getReferenceById(userId);
        vote.setUser(user);

        Optional<Vote> existingVoteOpt = voteRepository.getForToday(userId);

        if (existingVoteOpt.isPresent()) {
            Vote existingVote = existingVoteOpt.get();
            existingVote.setRestaurant(vote.getRestaurant());
            return voteRepository.save(existingVote);
        } else {
            return voteRepository.save(vote);
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        return voteRepository.delete(id, userId) != 0;
    }
}
