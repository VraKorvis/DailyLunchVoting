package ru.javapractice.dailylunchvoting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javapractice.dailylunchvoting.model.User;
import ru.javapractice.dailylunchvoting.repository.ProfileRepository;

import java.util.List;

import static ru.javapractice.dailylunchvoting.util.ValidationUtil.checkNotFound;

@Service
public class UserService {

    @Autowired
    private ProfileRepository repository;

    public User create(User user) {
        return repository.save(user);
    }

    public void delete(int id) {
        checkNotFound(repository.delete(id), id);
    }

    public User get(int id) {
        return checkNotFound(repository.get(id), id);
    }

    public User getByEmail(String email) {
        return checkNotFound(repository.getByEmail(email), "email=" + email);
    }

    public List<User> getAll() {
        return repository.getAll();
    }

    public void update(User user) {
        checkNotFound(repository.save(user), user.getId());
    }
}
