package ru.javapractice.dailylunchvoting.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;
import ru.javapractice.dailylunchvoting.common.exception.NotFoundException;

@NoRepositoryBean
public interface BaseRepository<T> extends JpaRepository<T, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM #{#entityName} e WHERE e.id=:id")
    default int delete(int id) {
        throw new UnsupportedOperationException("Deletion is not allowed. All data is stored in the database as history.");
    }

    @SuppressWarnings("all")
    default void deleteExisted(int id) {
        if (delete(id) == 0) {
            throw new NotFoundException("Entity with id=" + id + " not found");
        }
    }

    default T getExisted(int id) {
        return findById(id).orElseThrow(() -> new NotFoundException("Entity with id=" + id + " not found"));
    }
}