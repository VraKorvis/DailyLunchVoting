package ru.javapractice.dailylunchvoting.vote.model;

import lombok.Data;

@Data
public class VoteResult {
    private final boolean success;
    private final String message;
    private final int restaurantId;
}
