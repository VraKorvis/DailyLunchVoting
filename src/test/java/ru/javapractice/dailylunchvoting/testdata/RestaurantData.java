package ru.javapractice.dailylunchvoting.testdata;

import ru.javapractice.dailylunchvoting.model.Restaurant;
import ru.javapractice.dailylunchvoting.util.MatcherFactory;

import static ru.javapractice.dailylunchvoting.model.AbstractBaseEntity.START_SEQ;

public class RestaurantData {
    public static final MatcherFactory.Matcher<Restaurant> MATCHER = MatcherFactory.usingIgnoringFieldsComparator("menu");

    public static final int RESTAURANT_A_ID = START_SEQ + 4;

    public static final Restaurant RESTAURANT_A = new Restaurant(RESTAURANT_A_ID,"Restaurant A");
    public static final Restaurant RESTAURANT_B = new Restaurant(RESTAURANT_A_ID + 1,"Restaurant B");
    public static final Restaurant RESTAURANT_C = new Restaurant(RESTAURANT_A_ID + 2,"Restaurant C");

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
