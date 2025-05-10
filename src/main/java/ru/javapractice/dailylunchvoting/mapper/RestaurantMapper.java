package ru.javapractice.dailylunchvoting.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ru.javapractice.dailylunchvoting.restaurant.model.Restaurant;
import ru.javapractice.dailylunchvoting.restaurant.to.RestaurantTo;

@Mapper(componentModel = "spring")
public abstract class RestaurantMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    public abstract RestaurantTo toRestaurantTo(Restaurant restaurant);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    public abstract Restaurant toRestaurant(RestaurantTo restaurantTo);

}

