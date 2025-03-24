package ru.javapractice.dailylunchvoting.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name="vote")
@NamedEntityGraph(
        name = Vote.WITH_RESTAURANT,
        attributeNodes = @NamedAttributeNode("restaurant")
)
public class Vote extends AbstractBaseEntity {

    public static final String WITH_RESTAURANT = "Vote.withRestaurant";

    @Column(name = "date_time", nullable = false, columnDefinition = "timestamp default now()", updatable = false)
    @NotNull
    private LocalDateTime dateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    private Restaurant restaurant;

    public Vote() {
    }

    @SuppressWarnings("CopyConstructorMissesField")
    public Vote(Vote v) {
        this(v.id, v.dateTime, v.restaurant);
    }

    public Vote(Integer id, LocalDateTime dateTime, Restaurant restaurant) {
        super(id);
        this.dateTime = dateTime;
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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                '}';
    }
}
