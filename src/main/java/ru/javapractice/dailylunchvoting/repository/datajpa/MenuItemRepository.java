package ru.javapractice.dailylunchvoting.repository.datajpa;

import ru.javapractice.dailylunchvoting.model.MenuItem;
import ru.javapractice.dailylunchvoting.model.Restaurant;

import java.util.List;

public interface MenuItemRepository {
    List<MenuItem> getAll();
    MenuItem get(String id);
    MenuItem save(Restaurant restaurant);
    boolean delete(Integer id);
}
