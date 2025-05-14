package ru.javapractice.dailylunchvoting.restaurant.to;

import lombok.*;
import org.checkerframework.checker.units.qual.A;
import ru.javapractice.dailylunchvoting.common.to.NamedTo;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RestaurantTo extends NamedTo {

    public RestaurantTo(Integer id, String name) {
        super(id, name);
    }
}