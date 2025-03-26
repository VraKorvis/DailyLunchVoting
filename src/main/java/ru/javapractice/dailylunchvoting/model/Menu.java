package ru.javapractice.dailylunchvoting.model;

import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="menus")
public class Menu extends AbstractBaseEntity {

    @Column(name = "menu_date")
    private Date menuDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "menus_menuitems_link",
            joinColumns = @JoinColumn(name = "menu_id"),
            inverseJoinColumns = @JoinColumn(name = "menuitem_id"))
    @BatchSize(size = 200)
    private List<MenuItem> menuItems;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

//    TODO try to simplify using fk field
//    @Column(name="fk_key", updatable=false, insertable=false)
//    private Long menu_fk;

    public Menu() {
    }

    public Menu(Integer id) {
        super(id);
    }

    public Date getMenuDate() {
        return menuDate;
    }

    public void setMenuDate(Date menuDate) {
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
                '}';
    }
}
