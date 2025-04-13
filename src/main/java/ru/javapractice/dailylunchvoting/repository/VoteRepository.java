package ru.javapractice.dailylunchvoting.repository;

import ru.javapractice.dailylunchvoting.model.Vote;

import java.util.List;
import java.util.Optional;

public interface VoteRepository {

    Vote save(Vote vote, int userId);

    List<Vote> getAll();

    List<Vote> getAllForToday();
    List<Vote> getAllByUserId(int userId);

    Optional<Vote> findByUserIdForToday(int userId);
}
