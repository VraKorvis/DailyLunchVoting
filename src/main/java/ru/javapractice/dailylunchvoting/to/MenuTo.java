package ru.javapractice.dailylunchvoting.to;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.beans.ConstructorProperties;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Getter
@JsonPropertyOrder({"id", "menuDate", "items"})
public class MenuTo extends BaseTo {

    @NotNull
    private final LocalDate menuDate;

    @NotEmpty
    private final List<MenuItemTo> items;

    @ConstructorProperties({"id", "date", "items"})
    public MenuTo(Integer id, LocalDate menuDate, List<MenuItemTo> items) {
        super(id);
        this.menuDate = menuDate;
        this.items = items;
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
