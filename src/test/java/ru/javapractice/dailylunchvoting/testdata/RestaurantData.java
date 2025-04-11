package ru.javapractice.dailylunchvoting.testdata;

import ru.javapractice.dailylunchvoting.model.Menu;
import ru.javapractice.dailylunchvoting.model.MenuItem;
import ru.javapractice.dailylunchvoting.model.Restaurant;
import ru.javapractice.dailylunchvoting.util.MatcherFactory;

import java.time.LocalDate;
import java.util.Set;

import static ru.javapractice.dailylunchvoting.model.AbstractBaseEntity.START_SEQ;
import static ru.javapractice.dailylunchvoting.testdata.MenuItemData.*;

public class RestaurantData {
    public static final MatcherFactory.Matcher<Restaurant> MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class,"menus");
    public static final MatcherFactory.Matcher<Restaurant> MATCHER_WITH_MENU = MatcherFactory.usingEqualsComparator(Restaurant.class);

    public static final int RESTAURANT_A_ID = START_SEQ + 4;

    public static final Restaurant RESTAURANT_A = new Restaurant(RESTAURANT_A_ID,"Restaurant A");
    public static final Restaurant RESTAURANT_B = new Restaurant(RESTAURANT_A_ID + 1,"Restaurant B");
    public static final Restaurant RESTAURANT_C = new Restaurant(RESTAURANT_A_ID + 2,"Restaurant C");

    public static final Set<MenuItem> menuitems = Set.of(BURGER, PIZZA, SALAD, DESSERT);
    public static final Menu menu_one = new Menu(100017, LocalDate.now(), RESTAURANT_A, menuitems);

    static {
        RESTAURANT_A.setMenus(Set.of(menu_one));
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
