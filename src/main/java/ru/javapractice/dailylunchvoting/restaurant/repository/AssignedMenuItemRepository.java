package ru.javapractice.dailylunchvoting.restaurant.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javapractice.dailylunchvoting.common.BaseRepository;
import ru.javapractice.dailylunchvoting.restaurant.model.AssignedMenuItem;

public interface AssignedMenuItemRepository extends BaseRepository<AssignedMenuItem> {

    @Transactional
    @Modifying
    @Query("DELETE FROM AssignedMenuItem ami WHERE ami.id=:id")
    default int delete(@Param("id") int id){
        throw new UnsupportedOperationException("Deletion is not allowed. All data is stored in the database as history.");
    }
}
