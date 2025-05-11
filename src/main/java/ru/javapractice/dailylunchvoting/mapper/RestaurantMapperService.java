package ru.javapractice.dailylunchvoting.mapper;

import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.javapractice.dailylunchvoting.restaurant.model.Restaurant;
import ru.javapractice.dailylunchvoting.restaurant.to.AssignedMenuTo;
import ru.javapractice.dailylunchvoting.restaurant.to.RestaurantWithAssignedMenuTo;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class RestaurantMapperService {

    private final MenuMapperService menuMapper;

    public RestaurantWithAssignedMenuTo toWithAssignedMenuTo(@NonNull Restaurant restaurant) {

        AssignedMenuTo menu = restaurant.getMenus().stream()
                .filter(m -> m.getMenuDate().equals(LocalDate.now()))
                .findFirst()
                .map(menuMapper::toAssignedMenuTo)
                .orElse(null);

        return new RestaurantWithAssignedMenuTo(
                restaurant.getId(),
                restaurant.getName(),
                menu
        );
    }

    public List<RestaurantWithAssignedMenuTo> toWithAssignedMenuTos(List<Restaurant> restaurants) {
        return restaurants.stream()
                .map(this::toWithAssignedMenuTo)
                .toList();
    }
}
