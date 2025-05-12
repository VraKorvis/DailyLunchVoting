package ru.javapractice.dailylunchvoting.restaurant.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javapractice.dailylunchvoting.common.BaseRepository;
import ru.javapractice.dailylunchvoting.restaurant.model.Restaurant;

import java.util.List;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.menus m WHERE m.menuDate=CURRENT_DATE ORDER BY r.name ASC")
    List<Restaurant> findAllWithAssignedMenuForToday();

    @Query("SELECT DISTINCT r FROM Restaurant r LEFT JOIN FETCH r.menus m WHERE m.menuDate IS NULL OR m.menuDate <> CURRENT_DATE")
    List<Restaurant> findAllWithoutAssignedMenuForToday();

}
