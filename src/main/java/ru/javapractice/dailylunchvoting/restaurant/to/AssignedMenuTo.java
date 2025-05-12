package ru.javapractice.dailylunchvoting.restaurant.to;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.javapractice.dailylunchvoting.common.to.BaseTo;

import java.beans.ConstructorProperties;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@JsonPropertyOrder({"id", "menuDate", "pricedMenuItemTos"})
public class AssignedMenuTo extends BaseTo {

    @NotNull
    LocalDate menuDate;

    @NotEmpty
    @NotNull
    List<PricedMenuItemTo> pricedMenuItemTos;

    @ConstructorProperties({"id", "menuDate", "pricedMenuItemTos"})
    public AssignedMenuTo(Integer id, LocalDate menuDate, List<PricedMenuItemTo> pricedMenuItemTos) {
        super(id);
        this.menuDate = menuDate;
        this.pricedMenuItemTos = pricedMenuItemTos;
    }

}
