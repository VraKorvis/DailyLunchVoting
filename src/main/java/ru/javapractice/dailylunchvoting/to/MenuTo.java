package ru.javapractice.dailylunchvoting.to;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.beans.ConstructorProperties;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@JsonPropertyOrder({"id", "menuDate", "items"})
public class MenuTo extends BaseTo {
    private final LocalDate menuDate;
    private final List<MenuItemTo> items;

    @ConstructorProperties({"id", "date", "items"})
    public MenuTo(Integer id, LocalDate menuDate, List<MenuItemTo> items) {
        super(id);
        this.menuDate = menuDate;
        this.items = items;
    }

    public LocalDate getDate() {
        return menuDate;
    }

    public List<MenuItemTo> getItems() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MenuTo menuTo = (MenuTo) o;
        return Objects.equals(id, menuTo.id) &&
                Objects.equals(menuDate, menuTo.menuDate) &&
                Objects.equals(items, menuTo.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, menuDate, items);
    }

    @Override
    public String toString() {
        return "MenuTo{" +
                "id=" + id +
                ", date=" + menuDate +
                ", items=" + items +
                '}';
    }
}
