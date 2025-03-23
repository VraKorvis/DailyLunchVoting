package ru.javapractice.dailylunchvoting.service;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.javapractice.dailylunchvoting.model.User;
import ru.javapractice.dailylunchvoting.repository.ProfileRepository;

import java.util.List;

import static ru.javapractice.dailylunchvoting.util.ValidationUtil.checkNotFound;

@Service
public class UserService {

    private final ProfileRepository repository;

    public UserService(ProfileRepository repository) {
        this.repository = repository;
    }

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
        Assert.notNull(user, "user must not be null");
        checkNotFound(repository.save(user), user.id());
    }
}
