package ru.javapractice.dailylunchvoting.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
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
    public Optional<Vote> findByUserIdForToday(int id){
        return voteRepository.findByUserIdAndDate(id);
    }

    @Override
    public Vote save(Vote vote, int userId){
        return voteRepository.save(vote);
    }

    @Override
    public boolean delete(int id, int userId) {
        return voteRepository.delete(id, userId) != 0;
    }
}
