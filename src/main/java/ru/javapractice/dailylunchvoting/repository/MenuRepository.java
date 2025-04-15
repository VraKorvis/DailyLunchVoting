package ru.javapractice.dailylunchvoting.repository;

import ru.javapractice.dailylunchvoting.model.Menu;

import java.util.List;
import java.util.Optional;

public interface MenuRepository {
    List<Menu> getAll();

    Menu getReferenceById(Integer id);

    Menu save(Menu menuItem);

    boolean delete(Integer id);

    Optional<Menu> findByRestaurantIdForToday(Integer id);


    Optional<Menu> findById(int id);
}
