package ru.javapractice.dailylunchvoting.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.javapractice.dailylunchvoting.model.User;
import ru.javapractice.dailylunchvoting.model.Vote;
import ru.javapractice.dailylunchvoting.repository.VoteRepository;

import java.util.List;

@Repository
public class DataJpaVoteRepository implements VoteRepository {
    private static final Sort SORT_NAME = Sort.by(Sort.Direction.DESC, "dateTime");

    private ProxyCrudVoteRepository voteRepository;
    private ProxyCrudProfileRepository userRepository;

    public DataJpaVoteRepository(ProxyCrudVoteRepository voteRepository, ProxyCrudProfileRepository userRepository) {
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Vote> getAll() {
        return voteRepository.findAll(SORT_NAME);
    }

    @Override
    public List<Vote> getAllByUser(int userId) {
        return voteRepository.getAllByUserId(userId);
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public Vote get(int id, int userId) {
        Vote vote = voteRepository.findById(id).orElse(null);
        return vote != null && vote.getUser().getId() == userId ? vote : null;
    }

    @Override
    public Vote save(Vote vote, int userId) {
        User user = userRepository.getReferenceById(userId);
        vote.setUser(user);
        if (vote.isNew()){
            voteRepository.save(vote);
        }
        return get(vote.id(), userId) == null ? null : voteRepository.save(vote);
    }

    @Override
    public boolean delete(int id, int userId) {
        return voteRepository.delete(id, userId) != 0;
    }
}
