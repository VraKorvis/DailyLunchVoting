package ru.javapractice.dailylunchvoting.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javapractice.dailylunchvoting.model.User;

@Transactional(readOnly = true)
public interface ProxyCrudProfileRepository extends JpaRepository<User, Integer> {

    @Transactional
    @Modifying
//    @Query(name = User.DELETE)
    @Query("DELETE FROM User u WHERE u.id=:id")
    default int delete(@Param("id") int id){
        throw new UnsupportedOperationException("Deletion is not allowed. All data is stored in the database as history.");
    }

    User getByEmail(String email);
}
