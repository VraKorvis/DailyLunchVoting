package ru.javapractice.dailylunchvoting.to;

import java.beans.ConstructorProperties;
import java.util.Objects;

public final class MenuItemTo extends BaseTo {
    private final String name;
    private final float price;

    @ConstructorProperties({"id", "name", "price"})
    public MenuItemTo(Integer id, String name, float price) {
        super(id);
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MenuItemTo that = (MenuItemTo) o;
        return Objects.equals(id, that.id) &&
                Float.compare(price, that.price) == 0 &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price);
    }

    @Override
    public String toString() {
        return "MenuItemTo[" +
                "id=" + id + ", " +
                "name=" + name + ", " +
                "price=" + price + ']';
    }

}

