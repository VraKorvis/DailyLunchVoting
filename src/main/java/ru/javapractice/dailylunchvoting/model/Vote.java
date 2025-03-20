package ru.javapractice.dailylunchvoting.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name="vote")
public class Vote extends AbstractBaseEntity {

    @Column(name = "vote_date", nullable = false, columnDefinition = "timestamp default now()", updatable = false)
    @NotNull
    private Date voteDate = new Date();

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

    public Vote(Integer id) {
        super(id);
        this.voteDate = new Date();
    }

    @Override
    public String toString() {
        return "Vote{" +
                "id=" + id +
                ", voteDate=" + voteDate +
                ", restaurant=" + restaurant +
                ", user=" + user +
                '}';
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getVoteDate() {
        return voteDate;
    }

    public void setVoteDate(Date voteDate) {
        this.voteDate = voteDate;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }


}
