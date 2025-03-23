package ru.javapractice.dailylunchvoting.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.javapractice.dailylunchvoting.model.User;
import ru.javapractice.dailylunchvoting.repository.ProfileRepository;

import java.util.List;

@Repository
public class DataJpaProfileRepository implements ProfileRepository {

    private static final Sort SORT_NAME_EMAIL = Sort.by(Sort.Direction.ASC, "name", "email");

    private final ProxyCrudProfileRepository proxyCrudProfileRepository;

    public DataJpaProfileRepository(ProxyCrudProfileRepository proxyCrudProfileRepository) {
        this.proxyCrudProfileRepository = proxyCrudProfileRepository;
    }

    @Override
    public List<User> getAll() {
        return proxyCrudProfileRepository.findAll(SORT_NAME_EMAIL);
    }

    @Override
    public User get(int id) {
        return proxyCrudProfileRepository.findById(id).orElse(null);
    }

    @Override
    public User getByEmail(String email) {
        return proxyCrudProfileRepository.getByEmail(email);
    }

    @Override
    public User save(User user) {
        return proxyCrudProfileRepository.save(user);
    }

    @Override
    public boolean delete(int id) {
        return proxyCrudProfileRepository.delete(id) != 0;
    }
}
