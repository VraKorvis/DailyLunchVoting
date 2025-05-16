package ru.javapractice.dailylunchvoting.restaurant.to;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;
import ru.javapractice.dailylunchvoting.common.to.BaseTo;

import java.beans.ConstructorProperties;
import java.math.BigDecimal;

@Value
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@JsonPropertyOrder({"id", "name", "price"})
public class PricedMenuItemTo extends BaseTo {

    @NotBlank
    @Size(min = 2, max = 100)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    String name;

    @DecimalMin(value = "0.01", message = "Price must be greater than zero")
    @DecimalMax(value = "100000", message = "Price cannot be greater than 10000")
    @Positive(message = "Price must be a positive number")
    BigDecimal price;

    @ConstructorProperties({"id", "name", "price"})
    public PricedMenuItemTo(Integer id, String name, BigDecimal price) {
        super(id);
        this.name = name;
        this.price = price;
    }

    @Schema(description = "ID of the menu item")
    @Override
    public Integer getId() {
        return super.getId();
    }
}

