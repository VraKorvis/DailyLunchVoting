package ru.javapractice.dailylunchvoting.restaurant.to;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.*;
import lombok.Getter;
import ru.javapractice.dailylunchvoting.common.to.BaseTo;

import java.beans.ConstructorProperties;
import java.math.BigDecimal;
import java.util.Objects;

@Getter
@JsonPropertyOrder({"id", "name", "price"})
public final class MenuItemTo extends BaseTo {

    @NotBlank
    @Size(min = 2, max = 100)
    private final String name;

    @DecimalMin(value = "0.01", message = "Price must be greater than zero")
    @DecimalMax(value = "100000", message = "Price cannot be greater than 10000")
    @Positive(message = "Price must be a positive number")
    private final BigDecimal price;

    @ConstructorProperties({"id", "name", "price"})
    public MenuItemTo(Integer id, String name, BigDecimal price) {
        super(id);
        this.name = name;
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MenuItemTo that = (MenuItemTo) o;
        return Objects.equals(id, that.id) &&
                price.compareTo(that.price) == 0 &&
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

