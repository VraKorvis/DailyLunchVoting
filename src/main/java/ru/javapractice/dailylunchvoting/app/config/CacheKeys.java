package ru.javapractice.dailylunchvoting.app.config;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CacheKeys {
    public static final String ID = "#id";
    public static final String PAGE = "#page + '-' + #size";
    public static final String PAGEABLE = "#pageable.pageNumber + '-' + #pageable.pageSize";

    public static final String ALL_USERS = "'allUsers'";
    public static final String USER_ID = "#user.id";

    public static final String MENU_ITEM_ID = "#menuItem.id";
}
