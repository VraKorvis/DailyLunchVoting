package ru.javapractice.dailylunchvoting.restaurant.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.BatchSize;
import ru.javapractice.dailylunchvoting.common.model.BaseEntity;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="menu", uniqueConstraints = @UniqueConstraint(columnNames = {"restaurant_id", "menu_date"}, name = "unique_menu_per_day"))
public class Menu extends BaseEntity {

    @NotNull
    @Column(name = "menu_date", nullable = false)
    private LocalDate menuDate;

    @NotEmpty
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "menu_menuitems_link",
            joinColumns = @JoinColumn(name = "menu_id"),
            inverseJoinColumns = @JoinColumn(name = "menuitem_id"))
    @BatchSize(size = 200)
    private List<MenuItem> items;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonBackReference
    private Restaurant restaurant;

//    TODO try to simplify using fk field
//    @Column(name="fk_key", updatable=false, insertable=false)
//    private Long menu_fk;

    public Menu() {
    }

    public Menu(Integer id, LocalDate menuDate, Restaurant restaurant, List<MenuItem> items) {
        super(id);
        this.menuDate = menuDate;
        this.restaurant = restaurant;
        this.items = items;
    }

    public LocalDate getMenuDate() {
        return menuDate;
    }

    public void setMenuDate(LocalDate menuDate) {
        this.menuDate = menuDate;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public void setItems(List<MenuItem> menuItems) {
        this.items = menuItems;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", menuDate=" + menuDate +
                '}';
    }
}
