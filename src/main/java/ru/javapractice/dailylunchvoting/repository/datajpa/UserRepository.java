package ru.javapractice.dailylunchvoting.repository.datajpa;

import ru.javapractice.dailylunchvoting.model.User;

import java.util.List;

public interface UserRepository {
    List<User> getAll();
    User get(Integer id);
    User getByEmail(String email);
    User save(User user);
    boolean delete(Integer id);
}
