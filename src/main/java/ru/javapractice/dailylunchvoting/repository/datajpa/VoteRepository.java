package ru.javapractice.dailylunchvoting.repository.datajpa;

import ru.javapractice.dailylunchvoting.model.Vote;

import java.time.LocalDateTime;
import java.util.List;

public interface VoteRepository {

    List<Vote> getAll();
    List<Vote> getAllByUser(Integer userId);
    Vote get(Integer userId, LocalDateTime localDateTime);
    Vote save(Vote vote);
    boolean delete(Integer id);
}
