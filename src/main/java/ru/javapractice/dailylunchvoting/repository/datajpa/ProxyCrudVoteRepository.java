package ru.javapractice.dailylunchvoting.repository.datajpa;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javapractice.dailylunchvoting.model.Vote;

import java.util.List;

@Transactional(readOnly = true)
public interface ProxyCrudVoteRepository extends JpaRepository<Vote, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Vote v WHERE v.id=:id AND v.user.id=:userId")
    default int delete(@Param("id") int id, @Param("userId") int userId){
        throw new UnsupportedOperationException("Deletion is not allowed. All data is stored in the database as history.");
    }

    @Query("SELECT v FROM Vote v WHERE v.user.id=:userId ORDER BY v.dateTime DESC")
    List<Vote> getAllByUserId(@Param("userId") int userId);

    @EntityGraph(attributePaths = {"restaurant"})
    @Query("SELECT v FROM Vote v ORDER BY v.dateTime DESC")
    List<Vote> getAllWithRestaurant();

    @EntityGraph(attributePaths = {"restaurant"})
    @Query("SELECT v FROM Vote v WHERE v.user.id=:userId ORDER BY v.dateTime DESC")
    List<Vote> getAllWithRestaurantByUserId(@Param("userId") int userId);

    @EntityGraph(attributePaths = {"restaurant"})
    @Query("SELECT v FROM Vote v WHERE v.id = :id AND v.user.id = :userId ORDER BY v.dateTime DESC")
    Vote getWithRestaurant(@Param("id") int id, @Param("userId") int userId);

}
