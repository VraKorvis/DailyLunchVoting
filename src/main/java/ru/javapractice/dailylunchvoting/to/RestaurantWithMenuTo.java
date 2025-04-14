package ru.javapractice.dailylunchvoting.to;

import java.beans.ConstructorProperties;
import java.util.Objects;

public class RestaurantWithMenuTo extends BaseTo {
    private final String name;
    private final MenuTo menu;

    @ConstructorProperties({"id", "name", "menu"})
    public RestaurantWithMenuTo(Integer id, String name, MenuTo menu) {
        super(id);
        this.name = name;
        this.menu = menu;
    }

    public String getName() {
        return name;
    }

    public MenuTo getMenu() {
        return menu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        RestaurantWithMenuTo that = (RestaurantWithMenuTo) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(menu, that.menu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, menu);
    }

    @Override
    public String toString() {
        return "RestaurantWithMenuTo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", menu=" + menu +
                '}';
    }
}
