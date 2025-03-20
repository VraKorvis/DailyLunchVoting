package ru.javapractice.dailylunchvoting.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.javapractice.dailylunchvoting.model.Vote;
import ru.javapractice.dailylunchvoting.repository.VoteRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaVoteRepository implements VoteRepository {
    private static final Sort SORT_NAME = Sort.by(Sort.Direction.ASC, "dateTime");

    private ProxyCrudVoteRepository voteRepository;

    public DataJpaVoteRepository(ProxyCrudVoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @Override
    public List<Vote> getAll() {
        return voteRepository.findAll(SORT_NAME);
    }

    @Override
    public List<Vote> getAllByUser(Integer userId) {
        return voteRepository.getAllByUserId(userId);
    }

    @Override
    public Vote get(Integer userId, LocalDateTime localDateTime) {
        return null;
    }

    @Override
    public Vote save(Vote vote) {
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }
}
