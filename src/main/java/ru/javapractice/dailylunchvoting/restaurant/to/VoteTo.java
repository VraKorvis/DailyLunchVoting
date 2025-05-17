package ru.javapractice.dailylunchvoting.restaurant.to;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import ru.javapractice.dailylunchvoting.common.to.BaseTo;

import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonPropertyOrder({"id", "votedAt", "restaurantTo"})
public class VoteTo extends BaseTo {

    @NotNull
    LocalDate votedAt;

    @NotNull
    RestaurantTo restaurantTo;
}
