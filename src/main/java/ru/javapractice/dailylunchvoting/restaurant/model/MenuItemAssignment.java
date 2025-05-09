package ru.javapractice.dailylunchvoting.restaurant.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "menu_item_assignment")
public class MenuItemAssignment {

    @EmbeddedId
    private MenuItemAssignmentId id;

    @ManyToOne
    @MapsId("menuId")
    @JoinColumn(name = "menu_id")
    @JsonBackReference
    private Menu menu;

    @ManyToOne
    @MapsId("menuItemId")
    @JoinColumn(name = "menuitem_id")
    private MenuItem menuItem;

    @DecimalMin(value = "0.01", message = "Price must be greater than zero")
    @DecimalMax(value = "100000", message = "Price cannot be greater than 10000")
    @Positive(message = "Price must be a positive number")
    private BigDecimal price;

    public MenuItemAssignment(Menu menu, MenuItem menuItem, BigDecimal price) {
        this.menu = menu;
        this.menuItem = menuItem;
        this.price = price;
        this.id = createMenuItemAssignmentId(menu, menuItem);
    }

    private MenuItemAssignmentId createMenuItemAssignmentId(Menu menu, MenuItem menuItem) {
        return new MenuItemAssignmentId(menu.getId(), menuItem.getId(), menu.getMenuDate());
    }
}
