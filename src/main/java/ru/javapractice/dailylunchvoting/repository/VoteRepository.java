package ru.javapractice.dailylunchvoting.repository;

import ru.javapractice.dailylunchvoting.model.Vote;

import java.util.List;

public interface VoteRepository {

    List<Vote> getAll();
    List<Vote> getAllByUserId(int userId);
    Vote get(int id, int userId);
    Vote createOrUpdate(Vote vote, int userId);
    boolean delete(int id, int userId);
    Vote getWithRestaurant(int id, int userId);
    List<Vote> getAllWithRestaurantForToday();
    List<Vote> getAllWithRestaurantByUserId(int userId);

}
