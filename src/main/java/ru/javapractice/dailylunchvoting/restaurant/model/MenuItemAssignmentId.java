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
public class MenuItemAssignmentId implements Serializable {

    private Integer menuId;
    private Integer menuItemId;

    @Column(name = "menu_date")
    private LocalDate menuDate;

    public MenuItemAssignmentId(Integer id, Integer id1, LocalDate menuDate) {
        this.menuId = id;
        this.menuItemId = id1;
        this.menuDate = menuDate;
    }
}