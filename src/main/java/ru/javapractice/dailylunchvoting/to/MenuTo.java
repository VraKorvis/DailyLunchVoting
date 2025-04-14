package ru.javapractice.dailylunchvoting.to;

import java.beans.ConstructorProperties;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class MenuTo extends BaseTo {
    private final LocalDate date;
    private final List<MenuItemTo> items;

    @ConstructorProperties({"id", "date", "items"})
    public MenuTo(Integer id, LocalDate date, List<MenuItemTo> items) {
        super(id);
        this.date = date;
        this.items = items;
    }

    public LocalDate getDate() {
        return date;
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
                Objects.equals(date, menuTo.date) &&
                Objects.equals(items, menuTo.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, items);
    }

    @Override
    public String toString() {
        return "MenuTo{" +
                "id=" + id +
                ", date=" + date +
                ", items=" + items +
                '}';
    }
}
