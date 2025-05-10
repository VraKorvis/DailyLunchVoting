package ru.javapractice.dailylunchvoting.restaurant.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Embeddable
public class AssignmentMenuItemId implements Serializable {

    @Column(name = "menu_id")
    private Integer menuId;

    @Column(name = "menu_item_id")
    private Integer menuItemId;

    @Column(name = "menu_date")
    private LocalDate menuDate;

    public AssignmentMenuItemId(Integer menuId, Integer menuItemId, LocalDate menuDate) {
        this.menuId = menuId;
        this.menuItemId = menuItemId;
        this.menuDate = menuDate;
    }
}