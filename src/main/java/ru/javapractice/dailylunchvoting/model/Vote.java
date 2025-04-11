package ru.javapractice.dailylunchvoting.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name="vote")
@NamedEntityGraph(
        name = Vote.WITH_RESTAURANT,
        attributeNodes = @NamedAttributeNode("restaurant")
)
public class Vote extends AbstractBaseEntity {

    public static final String WITH_RESTAURANT = "Vote.withRestaurant";

    @Column(name = "vote_date", nullable = false, columnDefinition = "timestamp default now()", updatable = false)
    @NotNull
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    private Restaurant restaurant;

    public Vote() {
    }

    @SuppressWarnings("CopyConstructorMissesField")
    public Vote(Vote v) {
        this(v.id, v.date, v.restaurant);
    }

    public Vote(Integer id, LocalDate date, Restaurant restaurant) {
        super(id);
        this.date = date;
        this.restaurant = restaurant;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


    @Override
    public String toString() {
        return "Vote{" +
                "id=" + id +
                ", voteDate=" + date +
                '}';
    }
}
