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

    public DataJpaVoteRepository(ProxyCrudVoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @Override
    public List<Vote> getAll() {
        return voteRepository.findAll(SORT_NAME);
    }

    @Override
    public List<Vote> getAllForToday() {
        return voteRepository.getAllForToday();
    }

    @Override
    public List<Vote> getAllByUserId(int userId) {
        return voteRepository.getAllByUserId(userId);
    }

    @Override
    public Optional<Vote> findByUserIdForToday(int userId){
        return voteRepository.findByUserIdForToday(userId)
                .filter(v -> Objects.equals(v.getUser().getId(), userId));
    }

    @Override
    public Vote save(Vote vote, int userId){
        return voteRepository.save(vote);
    }

}
