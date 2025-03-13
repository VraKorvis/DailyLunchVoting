package ru.javapractice.dailylunchvoting.model;

import java.time.LocalDateTime;

public class Vote extends AbstractBaseEntity {

    private Integer userId;
    private Integer restaurantId;
    private LocalDateTime date;

    public Vote(Integer id) {
        super(id);
    }
}
