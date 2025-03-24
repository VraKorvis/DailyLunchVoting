package ru.javapractice.dailylunchvoting.repository;

import ru.javapractice.dailylunchvoting.model.MenuItem;

import java.util.List;

public interface MenuItemRepository {
    List<MenuItem> getAll();
    List<MenuItem> getAllByMenuId(Integer id);
    MenuItem get(Integer id);
    MenuItem save(MenuItem menuItem);
    boolean delete(Integer id);
}
