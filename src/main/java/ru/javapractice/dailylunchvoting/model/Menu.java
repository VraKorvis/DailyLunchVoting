package ru.javapractice.dailylunchvoting.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="menu", uniqueConstraints = @UniqueConstraint(columnNames = {"restaurant_id", "menu_date"}, name = "unique_menu_per_day"))
public class Menu extends AbstractBaseEntity {

    @Column(name = "menu_date", nullable = false)
    private LocalDate menuDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "menu_menuitems_link",
            joinColumns = @JoinColumn(name = "menu_id"),
            inverseJoinColumns = @JoinColumn(name = "menuitem_id"))
    @BatchSize(size = 200)
    private List<MenuItem> menuItems;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonBackReference
    private Restaurant restaurant;

//    TODO try to simplify using fk field
//    @Column(name="fk_key", updatable=false, insertable=false)
//    private Long menu_fk;

    public Menu() {
    }

    public Menu(Integer id, LocalDate menuDate, Restaurant restaurant, List<MenuItem> menuItems) {
        super(id);
        this.menuDate = menuDate;
        this.restaurant = restaurant;
        this.menuItems = menuItems;
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

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }



    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", menuDate=" + menuDate +
                '}';
    }
}
