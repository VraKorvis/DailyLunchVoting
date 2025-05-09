package ru.javapractice.dailylunchvoting.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.javapractice.dailylunchvoting.restaurant.model.Menu;
import ru.javapractice.dailylunchvoting.restaurant.model.MenuItemAssignment;
import ru.javapractice.dailylunchvoting.restaurant.to.MenuItemTo;
import ru.javapractice.dailylunchvoting.restaurant.to.MenuTo;

@Mapper(componentModel = "spring")
public interface MenuMapper {
    @Mapping(source = "menuDate", target = "menuDate")
    @Mapping(source = "assignments", target = "items")
    MenuTo toTo(Menu menu);

    @Mapping(source = "menuItem.id", target = "id")
    @Mapping(source = "menuItem.name", target = "name")
    @Mapping(source = "price", target = "price")
    MenuItemTo toMenuItemTo(MenuItemAssignment item);
}
