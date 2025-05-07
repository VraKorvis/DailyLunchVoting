package ru.javapractice.dailylunchvoting.restaurant.service;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.javapractice.dailylunchvoting.restaurant.model.MenuItem;
import ru.javapractice.dailylunchvoting.restaurant.repository.MenuItemRepository;

import java.util.List;

@Service
public class MenuItemService {

    private final MenuItemRepository repository;

    public MenuItemService(MenuItemRepository repository) {
        this.repository = repository;
    }

    public MenuItem create(MenuItem menuItem) {
        Assert.notNull(menuItem, "menuItem must not be null");
        return repository.save(menuItem);
    }

    public MenuItem get(int id) {
        return repository.getExisted(id);
    }

    public List<MenuItem> getAll() {
        return repository.findAll();
    }

    public void update(MenuItem menuItem) {
        Assert.notNull(menuItem, "menuItem must not be null");
        repository.save(menuItem);
    }

    public void delete(int id) {
        repository.delete(id);
    }
}
