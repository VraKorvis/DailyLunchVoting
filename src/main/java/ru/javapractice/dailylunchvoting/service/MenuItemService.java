package ru.javapractice.dailylunchvoting.service;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.javapractice.dailylunchvoting.model.MenuItem;
import ru.javapractice.dailylunchvoting.repository.MenuItemRepository;

import java.util.List;

import static ru.javapractice.dailylunchvoting.util.ValidationUtil.checkNotFound;

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
        return checkNotFound(repository.get(id), id);
    }

    public List<MenuItem> getAll() {
        return repository.getAll();
    }

    public void update(MenuItem menuItem) {
        Assert.notNull(menuItem, "menuItem must not be null");
        checkNotFound(repository.save(menuItem), menuItem.id());
    }

    public void delete(int id) {
        repository.delete(id);
    }
}
