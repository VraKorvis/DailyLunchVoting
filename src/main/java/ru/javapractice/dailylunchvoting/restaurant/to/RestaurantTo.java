package ru.javapractice.dailylunchvoting.restaurant.to;

import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.javapractice.dailylunchvoting.common.to.NamedTo;

@Value
@EqualsAndHashCode(callSuper = true)
public class RestaurantTo extends NamedTo {

    public RestaurantTo(Integer id, String name) {
        super(id, name);
    }
}