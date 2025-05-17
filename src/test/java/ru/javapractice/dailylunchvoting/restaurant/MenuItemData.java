package ru.javapractice.dailylunchvoting.restaurant;

import ru.javapractice.dailylunchvoting.restaurant.model.MenuItem;
import ru.javapractice.dailylunchvoting.util.MatcherFactory;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.javapractice.dailylunchvoting.common.model.BaseEntity.START_SEQ;

public class MenuItemData {
    public static final MatcherFactory.Matcher<MenuItem> MATCHER = MatcherFactory.usingEqualsComparator(MenuItem.class);

    public static final int BURGER_ID = START_SEQ + 7;
    public static final int UNKNOWN_ID = 404;
    public static final MenuItem BURGER = new MenuItem(BURGER_ID, "Burger");
    public static final MenuItem PIZZA = new MenuItem(BURGER_ID + 1, "Pizza");
    public static final MenuItem SALAD = new MenuItem(BURGER_ID + 2, "Salad");
    public static final MenuItem DESSERT = new MenuItem(BURGER_ID + 3, "Dessert");
    public static final MenuItem SOUP = new MenuItem(BURGER_ID + 4, "Soup");
    public static final MenuItem EGGS = new MenuItem(BURGER_ID + 5, "Eggs");
    public static final MenuItem STEAK = new MenuItem(BURGER_ID + 6, "Steak");
    public static final MenuItem SUSHI = new MenuItem(BURGER_ID + 7, "Sushi");
    public static final MenuItem CHICKEN = new MenuItem(BURGER_ID + 8, "Chicken");
    public static final MenuItem COFFEE = new MenuItem(BURGER_ID + 9, "Coffee");

    public static final MenuItem SALMON = new MenuItem(null, "Salmon");
    public static final MenuItem GRILLED_CHEESE = new MenuItem(null, "Grilled Cheese Sandwich");

    private MenuItemData(){}

    public static MenuItem getUpdated(MenuItem item) {
        var updatedItem = new MenuItem(item);
        updatedItem.setName("Updated Burger");
        return updatedItem;
    }

    public static MenuItem getNew() {
        return new MenuItem(null, "Tea");
    }

    public static List<MenuItem> getAllSorted() {
        return Stream.of(BURGER, PIZZA, SALAD, DESSERT, SOUP, EGGS, STEAK, SUSHI, CHICKEN, COFFEE)
                .sorted(Comparator.comparing(MenuItem::getName))
                .collect(Collectors.toList());
    }

}
