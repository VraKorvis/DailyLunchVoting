package ru.javapractice.dailylunchvoting.restaurant;

import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import ru.javapractice.dailylunchvoting.restaurant.model.Menu;
import ru.javapractice.dailylunchvoting.restaurant.model.MenuItem;
import ru.javapractice.dailylunchvoting.restaurant.model.Restaurant;
import ru.javapractice.dailylunchvoting.to.MenuTo;
import ru.javapractice.dailylunchvoting.to.RestaurantWithMenuTo;
import ru.javapractice.dailylunchvoting.user.UserData;
import ru.javapractice.dailylunchvoting.util.MatcherFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static ru.javapractice.dailylunchvoting.common.model.BaseEntity.START_SEQ;
import static ru.javapractice.dailylunchvoting.restaurant.MenuItemData.*;

public class RestaurantMenuData {

    private static final RecursiveComparisonConfiguration buildConfig = RecursiveComparisonConfiguration.builder()
            .withIgnoredCollectionOrderInFields("items")
            .build();

    public static final MatcherFactory.Matcher<Restaurant> MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "menus");
    public static final MatcherFactory.Matcher<RestaurantWithMenuTo> RESTAURANT_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(RestaurantWithMenuTo.class);
    public static final MatcherFactory.Matcher<MenuTo> MENU_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(MenuTo.class, buildConfig);

    public static final int RESTAURANT_A_ID = START_SEQ + 4;

    public static final Restaurant RESTAURANT_A = new Restaurant(RESTAURANT_A_ID, "Restaurant A");
    public static final Restaurant RESTAURANT_B = new Restaurant(RESTAURANT_A_ID + 1, "Restaurant B");
    public static final Restaurant RESTAURANT_C = new Restaurant(RESTAURANT_A_ID + 2, "Restaurant C");

    public static final List<MenuItem> MENUITEMS_1 = List.of(BURGER, PIZZA, SALAD, DESSERT);
    public static final List<MenuItem> MENUITEMS_2 = List.of(SOUP, EGGS);
    public static final List<MenuItem> MENUITEMS_3 = List.of(DESSERT, EGGS, STEAK, SUSHI, COFFEE);

    public static final List<MenuItem> NEW_MENUITEMS = List.of(BURGER, COFFEE, SALAD, DESSERT);
    public static final List<MenuItem> WRONG_NEW_MENUITEMS = List.of(SALMON, GRILLED_CHEESE, new MenuItem(UserData.USER_1_ID, "Coffee", BigDecimal.valueOf(100.0)));
    public static final List<MenuItem> UPDATED_MENUITEMS_1 = List.of(BURGER, COFFEE, SALAD, DESSERT);

    public static final Menu MENU_1 = new Menu(100017, LocalDate.now(), RESTAURANT_A, MENUITEMS_1);
    public static final Menu MENU_2 = new Menu(100018, LocalDate.now(), RESTAURANT_A, MENUITEMS_2);
    public static final Menu MENU_3 = new Menu(100019, LocalDate.now(), RESTAURANT_A, MENUITEMS_3);

    public static final Menu NEW_MENU = new Menu(null, LocalDate.now(), RESTAURANT_C, NEW_MENUITEMS);

    public static final Menu WRONG_MENU = new Menu(null, LocalDate.now(), RESTAURANT_B, WRONG_NEW_MENUITEMS);
    public static final Menu UPDATED_MENU = new Menu(100017, LocalDate.now(), RESTAURANT_A, UPDATED_MENUITEMS_1);

    static {
        RESTAURANT_A.setMenus(List.of(MENU_1));
        RESTAURANT_B.setMenus(List.of(MENU_2));
    }

    private RestaurantMenuData() {
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
