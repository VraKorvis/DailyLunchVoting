package ru.javapractice.dailylunchvoting.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javapractice.dailylunchvoting.model.MenuItem;

@Transactional(readOnly = true)
public interface ProxyCrudMenuItemRepository extends JpaRepository<MenuItem, Integer> {

    @Transactional
    @Modifying
    // @Query(name = MenuItem.DELETE)
    @Query("DELETE FROM MenuItem item WHERE item.id=:id")
    int delete(@Param("id") int id);
}
