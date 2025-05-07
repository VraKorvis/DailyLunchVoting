package ru.javapractice.dailylunchvoting.to;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.beans.ConstructorProperties;
import java.util.Objects;

@JsonPropertyOrder({"id", "name", "menu"})
public class RestaurantWithMenuTo extends BaseTo {
    @NotBlank
    @Size(min = 2, max = 100)
    private final String name;

    @NotNull
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
