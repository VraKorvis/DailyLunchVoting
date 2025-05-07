package ru.javapractice.dailylunchvoting.util;

import ru.javapractice.dailylunchvoting.restaurant.model.Menu;
import ru.javapractice.dailylunchvoting.restaurant.model.MenuItem;
import ru.javapractice.dailylunchvoting.restaurant.to.MenuItemTo;
import ru.javapractice.dailylunchvoting.restaurant.to.MenuTo;

public class MenuMapper {
    public static MenuTo toTo(Menu menu) {
        return new MenuTo(
                menu.getId(),
                menu.getMenuDate(),
                menu.getItems().stream()
                        .map(MenuMapper::toMenuItemTo)
                        .toList()
        );
    }

    public static MenuItemTo toMenuItemTo(MenuItem item) {
        return new MenuItemTo(item.getId(), item.getName(), item.getPrice());
    }
}
