package ru.javapractice.dailylunchvoting.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javapractice.dailylunchvoting.model.Vote;
import ru.javapractice.dailylunchvoting.repository.VoteRepository;

import java.util.List;

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
    public List<Vote> getAllWithRestaurant() {
        return voteRepository.getAllWithRestaurant();
    }

    @Override
    public List<Vote> getAllWithRestaurantByUserId(int userId) {
        return voteRepository.getAllWithRestaurantByUserId(userId);
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public Vote get(int id, int userId) {
        var vote = voteRepository.findById(id).orElse(null);
        return vote != null && vote.getUser().getId() == userId ? vote : null;
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public Vote getWithRestaurant(int id, int userId) {
        var vote = voteRepository.getWithRestaurant(id, userId);
        return vote != null && vote.getUser().getId() == userId ? vote : null;
    }

    @Override
    @Transactional
    public Vote save(Vote vote, int userId) {
        var user = userRepository.getReferenceById(userId);
        vote.setUser(user);
        if (vote.isNew()){
            return voteRepository.save(vote);
        }
       return get(vote.id(), userId) == null ? null : voteRepository.save(vote);
    }

    @Override
    public boolean delete(int id, int userId) {
        return voteRepository.delete(id, userId) != 0;
    }
}
