package ru.javapractice.dailylunchvoting.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.javapractice.dailylunchvoting.restaurant.model.Restaurant;
import ru.javapractice.dailylunchvoting.restaurant.to.MenuTo;
import ru.javapractice.dailylunchvoting.restaurant.to.RestaurantWithAssignedMenuTo;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class RestaurantMapperService {

    private final MenuMapper menuMapper;
    private final RestaurantMapper restaurantMapper;

    public RestaurantWithAssignedMenuTo toWithMenuTo(Restaurant restaurant) {
        MenuTo menu = restaurant.getMenus().stream()
                .filter(m -> m.getMenuDate().equals(LocalDate.now()))
                .findFirst()
                .map(menuMapper::toTo)
                .orElse(null);

        return new RestaurantWithAssignedMenuTo(
                restaurant.getId(),
                restaurant.getName(),
                menu
        );
    }

    public List<RestaurantWithAssignedMenuTo> toWithMenuToList(List<Restaurant> restaurants) {
        return restaurants.stream()
                .map(this::toWithMenuTo)
                .toList();
    }
}
