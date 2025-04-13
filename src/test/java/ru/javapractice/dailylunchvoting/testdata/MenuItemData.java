package ru.javapractice.dailylunchvoting.testdata;

import ru.javapractice.dailylunchvoting.model.MenuItem;
import ru.javapractice.dailylunchvoting.util.MatcherFactory;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.javapractice.dailylunchvoting.model.AbstractBaseEntity.START_SEQ;

public class MenuItemData {
    public static final MatcherFactory.Matcher<MenuItem> MATCHER = MatcherFactory.usingEqualsComparator(MenuItem.class);

    public static final int BURGER_ID = START_SEQ + 7;
    public static final MenuItem BURGER = new MenuItem(BURGER_ID, "Burger", 450.0f);
    public static final MenuItem PIZZA = new MenuItem(BURGER_ID + 1, "Pizza", 800.0f);
    public static final MenuItem SALAD = new MenuItem(BURGER_ID + 2, "Salad", 250.0f);
    public static final MenuItem DESSERT = new MenuItem(BURGER_ID + 3, "Dessert", 350.0f);
    public static final MenuItem SOUP = new MenuItem(BURGER_ID + 4, "Soup", 200.0f);
    public static final MenuItem EGGS = new MenuItem(BURGER_ID + 5, "Eggs", 150.0f);
    public static final MenuItem STEAK = new MenuItem(BURGER_ID + 6, "Steak", 900.0f);
    public static final MenuItem SUSHI = new MenuItem(BURGER_ID + 7, "Sushi", 750.0f);
    public static final MenuItem CHICKEN = new MenuItem(BURGER_ID + 8, "Chicken", 400.0f);
    public static final MenuItem COFFEE = new MenuItem(BURGER_ID + 9, "Coffee", 100.0f);

    private MenuItemData(){}

    public static MenuItem getUpdated(MenuItem item) {
        var updatedItem = new MenuItem(item);
        updatedItem.setName("Updated Burger");
        updatedItem.setPrice(499.99f);
        return updatedItem;
    }

    public static MenuItem getNew() {
        return new MenuItem(null, "Tea", 60.0f);
    }

    public static List<MenuItem> getAllSorted() {
        return Stream.of(BURGER, PIZZA, SALAD, DESSERT, SOUP, EGGS, STEAK, SUSHI, CHICKEN, COFFEE)
                .sorted(Comparator.comparing(MenuItem::getName))
                .collect(Collectors.toList());
    }

}
