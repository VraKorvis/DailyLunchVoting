package ru.javapractice.dailylunchvoting.testdata;

import ru.javapractice.dailylunchvoting.model.Menu;
import ru.javapractice.dailylunchvoting.model.MenuItem;
import ru.javapractice.dailylunchvoting.model.Restaurant;
import ru.javapractice.dailylunchvoting.to.RestaurantWithMenuTo;
import ru.javapractice.dailylunchvoting.util.MatcherFactory;

import java.time.LocalDate;
import java.util.*;

import static ru.javapractice.dailylunchvoting.model.AbstractBaseEntity.START_SEQ;
import static ru.javapractice.dailylunchvoting.testdata.MenuItemData.*;

public class RestaurantData {

    public static final MatcherFactory.Matcher<Restaurant> MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "menus");
    public static final MatcherFactory.Matcher<Restaurant> MATCHER_WITH_MENU = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "menus.restaurant");
    public static final MatcherFactory.Matcher<RestaurantWithMenuTo> MATCHER_TO_WITH_MENU = MatcherFactory.usingIgnoringFieldsComparator(RestaurantWithMenuTo.class);

    public static final int RESTAURANT_A_ID = START_SEQ + 4;

    public static final Restaurant RESTAURANT_A = new Restaurant(RESTAURANT_A_ID, "Restaurant A");
    public static final Restaurant RESTAURANT_B = new Restaurant(RESTAURANT_A_ID + 1, "Restaurant B");
    public static final Restaurant RESTAURANT_C = new Restaurant(RESTAURANT_A_ID + 2, "Restaurant C");

    public static final List<MenuItem> MENUITEMS_1 = List.of(BURGER, PIZZA, SALAD, DESSERT);
    public static final List<MenuItem> MENUITEMS_2 = List.of(SOUP, EGGS);
    public static final List<MenuItem> MENUITEMS_3 = List.of(DESSERT, EGGS, STEAK, SUSHI, COFFEE);

    public static final Menu MENU_1 = new Menu(100017, LocalDate.now(), RESTAURANT_A, MENUITEMS_1);
    public static final Menu MENU_2 = new Menu(100018, LocalDate.now(), RESTAURANT_A, MENUITEMS_2);
    public static final Menu MENU_3 = new Menu(100019, LocalDate.now(), RESTAURANT_A, MENUITEMS_3);

    static {
        RESTAURANT_A.setMenus(List.of(MENU_1));
        RESTAURANT_B.setMenus(List.of(MENU_2));
        RESTAURANT_C.setMenus(List.of(MENU_3));
    }

    private RestaurantData() {
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
