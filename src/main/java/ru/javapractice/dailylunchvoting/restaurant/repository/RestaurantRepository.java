package ru.javapractice.dailylunchvoting.restaurant.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javapractice.dailylunchvoting.common.BaseRepository;
import ru.javapractice.dailylunchvoting.restaurant.model.Restaurant;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Restaurant r WHERE r.id=:id")
    default int delete(@Param("id") int id){
        throw new UnsupportedOperationException("Deletion is not allowed. All data is stored in the database as history.");
    }

    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.menus m WHERE m.menuDate=CURRENT_DATE ORDER BY r.name ASC")
    List<Restaurant> getAllWithMenuForToday();

    @Query("SELECT r FROM Restaurant r LEFT JOIN Menu m ON r.id = m.restaurant.id AND m.menuDate = CURRENT_DATE WHERE m.id IS NULL")
    List<Restaurant> getAllWithoutAssignedMenuForToday();

    @Query("SELECT r FROM Restaurant r INNER JOIN Menu m ON r.id = m.restaurant.id AND m.menuDate = CURRENT_DATE")
    List<Restaurant> getAllWithAssignedMenuForToday();

    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.menus m WHERE r.id=:id AND m.menuDate=CURRENT_DATE ORDER BY r.name ASC")
    Optional<Restaurant> findByIdWithMenuForToday(@Param("id") Integer id);

}
