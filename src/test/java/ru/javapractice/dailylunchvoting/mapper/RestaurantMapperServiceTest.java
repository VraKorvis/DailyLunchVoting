package ru.javapractice.dailylunchvoting.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.javapractice.dailylunchvoting.restaurant.to.RestaurantWithAssignedMenuTo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.javapractice.dailylunchvoting.restaurant.MenuItemData.BURGER;
import static ru.javapractice.dailylunchvoting.restaurant.RestaurantMenuData.RESTAURANT_A;

@SpringBootTest
public class RestaurantMapperServiceTest {
    @Autowired
    private RestaurantMapperService restaurantMapperService;

    @Test
    public void testToWithAssignedMenuTo() {

        RestaurantWithAssignedMenuTo result = restaurantMapperService.toWithAssignedMenuTo(RESTAURANT_A);

        assertNotNull(result);
        assertEquals(RESTAURANT_A.getId(), result.getId());
        assertEquals(RESTAURANT_A.getName(), result.getName());
        assertNotNull(result.getMenu());
        assertEquals(4, result.getMenu().getPricedMenuItemTos().size());
        assertEquals(BURGER.getName(), result.getMenu().getPricedMenuItemTos().getFirst().getName());
    }
}
