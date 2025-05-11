package ru.javapractice.dailylunchvoting.restaurant.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import ru.javapractice.dailylunchvoting.common.model.BaseEntity;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name="menu", uniqueConstraints = @UniqueConstraint(columnNames = {"restaurant_id", "menu_date"}, name = "unique_menu_per_day"))
public class Menu extends BaseEntity {

    @NotNull
    @Column(name = "menu_date", nullable = false)
    private LocalDate menuDate;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 20)
    @JsonManagedReference
    private List<AssignedMenuItem> assignedMenuItems;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonBackReference
    private Restaurant restaurant;

    public Menu(Integer id, LocalDate menuDate, Restaurant restaurant, List<AssignedMenuItem> assignedMenuItems) {
        super(id);
        this.menuDate = menuDate;
        this.restaurant = restaurant;
        this.assignedMenuItems = assignedMenuItems;
    }

    @Override
    public String toString() {
        return "Menu{id=" + id + ", menuDate=" + menuDate + "}";
    }
}
