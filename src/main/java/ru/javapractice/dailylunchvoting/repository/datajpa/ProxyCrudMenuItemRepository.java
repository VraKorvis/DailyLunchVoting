package ru.javapractice.dailylunchvoting.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javapractice.dailylunchvoting.model.MenuItem;

import java.util.List;
import java.util.Set;

@Transactional(readOnly = true)
public interface ProxyCrudMenuItemRepository extends JpaRepository<MenuItem, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM MenuItem item WHERE item.id=:id")
    default int delete(@Param("id") int id){
        throw new UnsupportedOperationException("Deletion is not allowed. All data is stored in the database as history.");
    }

    List<MenuItem> findItemsById(Integer id);

    @Query("SELECT m FROM MenuItem m WHERE m.name IN :names")
    List<MenuItem> findByNames(@Param("names") Set<String> names);
}
