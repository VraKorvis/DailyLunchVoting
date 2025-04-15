package ru.javapractice.dailylunchvoting.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javapractice.dailylunchvoting.model.Menu;

import java.util.Optional;

@Transactional(readOnly = true)
public interface ProxyCrudMenuRepository extends JpaRepository<Menu, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Menu m WHERE m.id=:id")
    default int delete(@Param("id") int id){
        throw new UnsupportedOperationException("Deletion is not allowed. All data is stored in the database as history.");
    }

    @Query("SELECT m FROM Menu m WHERE m.restaurant.id = :restaurantId AND m.menuDate = CURRENT_DATE")
    Optional<Menu> findByRestaurantIdForToday(@Param("restaurantId") Integer restaurantId);
}
