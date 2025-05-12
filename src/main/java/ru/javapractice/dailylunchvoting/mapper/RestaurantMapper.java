package ru.javapractice.dailylunchvoting.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ru.javapractice.dailylunchvoting.restaurant.model.Restaurant;
import ru.javapractice.dailylunchvoting.restaurant.to.RestaurantTo;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    RestaurantTo toRestaurantTo(Restaurant restaurant);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    Restaurant toRestaurant(RestaurantTo restaurantTo);

    List<RestaurantTo> toRestaurantTos(List<Restaurant> restaurants);
}

