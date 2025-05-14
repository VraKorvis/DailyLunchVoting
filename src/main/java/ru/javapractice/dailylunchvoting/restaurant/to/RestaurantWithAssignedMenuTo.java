package ru.javapractice.dailylunchvoting.restaurant.to;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonPropertyOrder({"id", "name", "menu"})
public class RestaurantWithAssignedMenuTo extends BaseTo {
    @NotBlank
    @Size(min = 2, max = 100)
    String name;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    AssignedMenuTo menu;

    @ConstructorProperties({"id", "name", "menu"})
    public RestaurantWithAssignedMenuTo(Integer id, String name, AssignedMenuTo menu) {
        super(id);
        this.name = name;
        this.menu = menu;
    }

}
