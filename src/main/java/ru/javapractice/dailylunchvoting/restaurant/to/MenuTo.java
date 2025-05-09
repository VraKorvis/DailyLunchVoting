package ru.javapractice.dailylunchvoting.restaurant.to;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.javapractice.dailylunchvoting.common.to.BaseTo;

import java.beans.ConstructorProperties;
import java.time.LocalDate;
import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
@JsonPropertyOrder({"id", "menuDate", "items"})
public class MenuTo extends BaseTo {

    @NotNull
    LocalDate menuDate;

    @NotEmpty
    @NotNull
    List<MenuItemTo> items;

    @ConstructorProperties({"id", "menuDate", "items"})
    public MenuTo(Integer id, LocalDate menuDate, List<MenuItemTo> items) {
        super(id);
        this.menuDate = menuDate;
        this.items = items;
    }

}
