package ru.javapractice.dailylunchvoting.util;

import ru.javapractice.dailylunchvoting.restaurant.to.MenuItemTo;
import ru.javapractice.dailylunchvoting.restaurant.to.MenuTo;

import java.util.HashMap;
import java.util.Map;

public class TestUtil {

    public static void assignGeneratedIds(MenuTo newMenuTo, MenuTo createdMenuTo) {

        Map<String, Integer> itemIdMap = new HashMap<>();

        for (MenuItemTo item : createdMenuTo.getItems()) {
            String key = item.getName() + "_" + item.getPrice();
            itemIdMap.put(key, item.getId());
        }

        for (MenuItemTo item : newMenuTo.getItems()) {
            String key = item.getName() + "_" + item.getPrice();
            item.setId(itemIdMap.getOrDefault(key, null));
        }
    }
}
