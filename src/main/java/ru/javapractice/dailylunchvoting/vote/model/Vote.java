package ru.javapractice.dailylunchvoting.vote.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.javapractice.dailylunchvoting.common.model.BaseEntity;
import ru.javapractice.dailylunchvoting.restaurant.model.Restaurant;
import ru.javapractice.dailylunchvoting.user.model.User;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name="vote")
@NoArgsConstructor
@NamedEntityGraph(
        name = Vote.WITH_RESTAURANT,
        attributeNodes = @NamedAttributeNode("restaurant")
)
public class Vote extends BaseEntity {

    public static final String WITH_RESTAURANT = "Vote.withRestaurant";

    @Column(name = "voted_at", nullable = false, columnDefinition = "timestamp default now()", updatable = false)
    @NotNull
    private LocalDate votedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    private Restaurant restaurant;

    @SuppressWarnings("CopyConstructorMissesField")
    public Vote(Vote v) {
        this(v.id, v.votedAt, v.restaurant);
    }

    public Vote(Integer id, LocalDate votedAt, Restaurant restaurant) {
        super(id);
        this.votedAt = votedAt;
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "Vote{" +
               "id=" + id +
               ", voteDate=" + votedAt +
               '}';
    }
}
