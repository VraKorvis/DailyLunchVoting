package ru.javapractice.dailylunchvoting.restaurant.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javapractice.dailylunchvoting.common.BaseRepository;
import ru.javapractice.dailylunchvoting.restaurant.model.Menu;

import java.time.LocalDate;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuRepository extends BaseRepository<Menu> {

    @Query("SELECT m FROM Menu m WHERE m.restaurant.id=:restaurantId AND m.menuDate =:menuDate")
    Optional<Menu> findByRestaurantIdAndMenuDate(@Param("restaurantId") Integer restaurantId, @Param("menuDate") LocalDate menuDate);

    boolean existsByRestaurantIdAndMenuDate(Integer restaurantId, LocalDate menuDate);
}
