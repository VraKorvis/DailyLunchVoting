package ru.javapractice.dailylunchvoting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javapractice.dailylunchvoting.model.MenuItem;
import ru.javapractice.dailylunchvoting.repository.MenuItemRepository;

import java.util.List;

import static ru.javapractice.dailylunchvoting.util.ValidationUtil.checkNotFound;

@Service
public class MenuService {

    @Autowired
    private MenuItemRepository repository;

    public MenuItem create(MenuItem menuItem) {
        return repository.save(menuItem);
    }

    public void delete(int id) {
        checkNotFound(repository.delete(id), id);
    }

    public MenuItem get(int id) {
        return checkNotFound(repository.get(id), id);
    }

    public List<MenuItem> getAll() {
        return repository.getAll();
    }

    public void update(MenuItem menuItem) {
        checkNotFound(repository.save(menuItem), menuItem.getId());
    }
}
