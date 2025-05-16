package ru.javapractice.dailylunchvoting.vote.model;

import lombok.AllArgsConstructor;
import lombok.Data;

public record VoteResult(boolean success, String message, int restaurantId) {
}
