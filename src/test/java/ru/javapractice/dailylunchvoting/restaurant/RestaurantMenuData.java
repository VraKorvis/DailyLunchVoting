package ru.javapractice.dailylunchvoting.restaurant;

import lombok.experimental.UtilityClass;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import ru.javapractice.dailylunchvoting.restaurant.model.Menu;
import ru.javapractice.dailylunchvoting.restaurant.model.AssignedMenuItem;
import ru.javapractice.dailylunchvoting.restaurant.model.Restaurant;
import ru.javapractice.dailylunchvoting.restaurant.to.MenuTo;
import ru.javapractice.dailylunchvoting.restaurant.to.RestaurantWithAssignedMenuTo;
import ru.javapractice.dailylunchvoting.util.MatcherFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static ru.javapractice.dailylunchvoting.common.model.BaseEntity.START_SEQ;
import static ru.javapractice.dailylunchvoting.restaurant.MenuItemData.*;

@UtilityClass
public class RestaurantMenuData {

    private static final RecursiveComparisonConfiguration buildConfig = RecursiveComparisonConfiguration.builder()
            .withIgnoredCollectionOrderInFields("items")
            .build();

    public static final MatcherFactory.Matcher<Restaurant> MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "menus");
    public static final MatcherFactory.Matcher<RestaurantWithAssignedMenuTo> RESTAURANT_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(RestaurantWithAssignedMenuTo.class);
    public static final MatcherFactory.Matcher<MenuTo> MENU_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(MenuTo.class, buildConfig);

    public static final int RESTAURANT_A_ID = START_SEQ + 4;

    public static final Restaurant RESTAURANT_A = new Restaurant(RESTAURANT_A_ID, "Restaurant A");
    public static final Restaurant RESTAURANT_B = new Restaurant(RESTAURANT_A_ID + 1, "Restaurant B");
    public static final Restaurant RESTAURANT_C = new Restaurant(RESTAURANT_A_ID + 2, "Restaurant C");

    public static final Menu MENU_1 = new Menu(100017, LocalDate.now(), RESTAURANT_A, null);
    public static final Menu MENU_2 = new Menu(100018, LocalDate.now(), RESTAURANT_A, null);
    public static final Menu MENU_3 = new Menu(100019, LocalDate.now(), RESTAURANT_A, null);

    public static final Menu NEW_MENU = new Menu(null, LocalDate.now(), RESTAURANT_C, null);

    public static final Menu WRONG_MENU = new Menu(null, LocalDate.now(), RESTAURANT_B, null);
    public static final Menu UPDATED_MENU = new Menu(100017, LocalDate.now(), RESTAURANT_A, null);

    public static final List<AssignedMenuItem> MENUITEMS_1 = List.of(
            new AssignedMenuItem(MENU_1, BURGER, BigDecimal.valueOf(450)),
            new AssignedMenuItem(MENU_1, PIZZA, BigDecimal.valueOf(800)),
            new AssignedMenuItem(MENU_1, SALAD, BigDecimal.valueOf(250)),
            new AssignedMenuItem(MENU_1, DESSERT, BigDecimal.valueOf(350))
    );

    public static final List<AssignedMenuItem> MENUITEMS_2 = List.of(
            new AssignedMenuItem(MENU_2, SOUP, BigDecimal.valueOf(200)),
            new AssignedMenuItem(MENU_2, EGGS, BigDecimal.valueOf(150))
    );

    public static final List<AssignedMenuItem> MENUITEMS_3 = List.of(
            new AssignedMenuItem(MENU_3, DESSERT, BigDecimal.valueOf(350)),
            new AssignedMenuItem(MENU_3, EGGS, BigDecimal.valueOf(150)),
            new AssignedMenuItem(MENU_3, STEAK, BigDecimal.valueOf(900)),
            new AssignedMenuItem(MENU_3, SUSHI, BigDecimal.valueOf(750)),
            new AssignedMenuItem(MENU_3, COFFEE, BigDecimal.valueOf(100))
    );

    public static final List<AssignedMenuItem> NEW_MENUITEMS = List.of(
            new AssignedMenuItem(NEW_MENU, BURGER, BigDecimal.valueOf(450)),
            new AssignedMenuItem(NEW_MENU, COFFEE, BigDecimal.valueOf(100)),
            new AssignedMenuItem(NEW_MENU, SALAD, BigDecimal.valueOf(250)),
            new AssignedMenuItem(NEW_MENU, DESSERT, BigDecimal.valueOf(350))
    );

    public static final List<AssignedMenuItem> WRONG_NEW_MENUITEMS = List.of(
            new AssignedMenuItem(WRONG_MENU, SALMON, BigDecimal.valueOf(900)),
            new AssignedMenuItem(WRONG_MENU, GRILLED_CHEESE, BigDecimal.valueOf(800)),
            new AssignedMenuItem(WRONG_MENU, COFFEE, BigDecimal.valueOf(100))
    );

    public static final List<AssignedMenuItem> UPDATED_MENUITEMS_1 = List.of(
            new AssignedMenuItem(UPDATED_MENU, BURGER, BigDecimal.valueOf(500)),
            new AssignedMenuItem(UPDATED_MENU, COFFEE, BigDecimal.valueOf(150)),
            new AssignedMenuItem(UPDATED_MENU, SALAD, BigDecimal.valueOf(300)),
            new AssignedMenuItem(UPDATED_MENU, DESSERT, BigDecimal.valueOf(400))
    );

    static {
        MENU_1.setAssignedMenuItems(MENUITEMS_1);
        MENU_2.setAssignedMenuItems(MENUITEMS_2);
        MENU_3.setAssignedMenuItems(MENUITEMS_3);
        NEW_MENU.setAssignedMenuItems(NEW_MENUITEMS);
        WRONG_MENU.setAssignedMenuItems(WRONG_NEW_MENUITEMS);
        UPDATED_MENU.setAssignedMenuItems(UPDATED_MENUITEMS_1);

        RESTAURANT_A.setMenus(List.of(MENU_1));
        RESTAURANT_B.setMenus(List.of(MENU_2));
    }

    public static Restaurant getNew() {
        return new Restaurant("Restaurant D");
    }

    public static Restaurant getUpdated(Restaurant restaurant) {
        var newR = new Restaurant(restaurant);
        newR.setName("Restaurant updated name");
        return newR;
    }

}
