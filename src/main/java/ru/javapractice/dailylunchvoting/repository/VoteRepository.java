package ru.javapractice.dailylunchvoting.repository;

import ru.javapractice.dailylunchvoting.model.Vote;

import java.time.LocalDateTime;
import java.util.List;

public interface VoteRepository {

    List<Vote> getAll();
    List<Vote> getAllByUser(int userId);
    Vote get(int id, int userId);
    Vote save(Vote vote, int userId);
    boolean delete(int id, int userId);
}
