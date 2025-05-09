package ru.javapractice.dailylunchvoting.restaurant.to;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;
import ru.javapractice.dailylunchvoting.common.to.BaseTo;

import java.beans.ConstructorProperties;

@Value
@EqualsAndHashCode(callSuper = true)
@JsonPropertyOrder({"id", "name", "menu"})
public class RestaurantWithAssignedMenuTo extends BaseTo {
    @NotBlank
    @Size(min = 2, max = 100)
    private final String name;

    @NotNull
    private final MenuTo menu;

    @ConstructorProperties({"id", "name", "menu"})
    public RestaurantWithAssignedMenuTo(Integer id, String name, MenuTo menu) {
        super(id);
        this.name = name;
        this.menu = menu;
    }

}
