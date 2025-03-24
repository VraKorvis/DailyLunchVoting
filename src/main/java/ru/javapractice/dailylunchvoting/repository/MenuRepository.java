package ru.javapractice.dailylunchvoting.repository;

import ru.javapractice.dailylunchvoting.model.Menu;

import java.util.List;

public interface MenuRepository {
    List<Menu> getAll();
    Menu get(Integer id);
    Menu save(Menu menuItem);
    boolean delete(Integer id);
}
