package ru.javapractice.dailylunchvoting.repository;

import ru.javapractice.dailylunchvoting.model.User;

import java.util.List;

public interface ProfileRepository {
    List<User> getAll();
    User get(int id);
    User getByEmail(String email);
    User save(User user);
    boolean delete(int id);

    User getReferenceById(int id);
}
