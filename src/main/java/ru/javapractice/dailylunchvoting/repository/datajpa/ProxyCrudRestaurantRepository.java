package ru.javapractice.dailylunchvoting.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javapractice.dailylunchvoting.model.Restaurant;

import java.util.List;

@Transactional(readOnly = true)
public interface ProxyCrudRestaurantRepository extends JpaRepository<Restaurant, Integer> {

    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.menus m WHERE m.menuDate=CURRENT_DATE ORDER BY r.name ASC")
    List<Restaurant> findRestaurantsWithMenuForToday();

    @Transactional
    @Modifying
    @Query("DELETE FROM Restaurant r WHERE r.id=:id")
    default int delete(@Param("id") int id){
        throw new UnsupportedOperationException("Deletion is not allowed. All data is stored in the database as history.");
    }
}
