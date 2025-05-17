package ru.javapractice.dailylunchvoting.vote.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javapractice.dailylunchvoting.common.BaseRepository;
import ru.javapractice.dailylunchvoting.vote.model.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Vote> findAllByVotedAtOrderByVotedAtDesc(LocalDate votedAt);

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    Page<Vote> findAllByVotedAtOrderByVotedAtDesc(LocalDate votedAt, Pageable pageable);

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v WHERE v.user.id=:userId ORDER BY v.votedAt DESC, v.id DESC")
    List<Vote> findAllByUserId(@Param("userId") int userId);

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v WHERE v.user.id = :userId and v.votedAt = CURRENT_DATE")
    Optional<Vote> findByUserIdForToday(@Param("userId") int userId);
}
