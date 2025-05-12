package ru.javapractice.dailylunchvoting.restaurant.to;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.javapractice.dailylunchvoting.common.to.BaseTo;

import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
public class VoteTo extends BaseTo {

    @NotNull
    LocalDate date;

    @NotNull
    RestaurantTo restaurantTo;
}
