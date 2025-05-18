package ru.javapractice.dailylunchvoting.restaurant.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@ToString
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