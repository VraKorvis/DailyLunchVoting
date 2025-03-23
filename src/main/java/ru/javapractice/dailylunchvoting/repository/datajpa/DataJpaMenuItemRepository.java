package ru.javapractice.dailylunchvoting.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.javapractice.dailylunchvoting.model.MenuItem;
import ru.javapractice.dailylunchvoting.repository.MenuItemRepository;

import java.util.List;

@Repository
public class DataJpaMenuItemRepository implements MenuItemRepository {

    private static final Sort SORT_NAME = Sort.by(Sort.Direction.ASC, "name");

    private ProxyCrudMenuItemRepository proxyCrudRepository;

    public DataJpaMenuItemRepository(ProxyCrudMenuItemRepository proxyCrudRepository) {
        this.proxyCrudRepository = proxyCrudRepository;
    }

    @Override
    public List<MenuItem> getAll() {
        return proxyCrudRepository.findAll(SORT_NAME);
    }

    @Override
    public MenuItem get(Integer id) {
        return proxyCrudRepository.findById(id).orElse(null);
    }

    @Override
    public MenuItem save(MenuItem menuItem) {
        return proxyCrudRepository.save(menuItem);
    }

    @Override
    public boolean delete(Integer id) {
        return proxyCrudRepository.delete(id) != 0;
    }
}
