package ru.javapractice.dailylunchvoting.app.config;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CacheNames {
    public static final String USER_CACHE = "userCache";
    public static final String USERS_LIST = "usersList";
    public static final String USERS_PAGE = "usersPage";

    public static final String RESTAURANT = "restaurant";
    public static final String RESTAURANT_LIST = "restaurantsList";
    public static final String RESTAURANTS_WITH_TODAY_MENU_LIST = "restaurantsWithTodayMenuList";
    public static final String RESTAURANTS_WITH_TODAY_MENU_PAGE = "restaurantsWithTodayMenuPage";

    public static final String TODAY_MENUS = "todayMenus";

    public static final String MENU_ITEMS_LIST = "menuItemsList";
    public static final String MENU_ITEMS_PAGE = "menuItemsPage";
    public static final String MENU_ITEM = "menuItem";
    public static final String MENU_ITEMS_MAP = "menuItemsMap";
}
