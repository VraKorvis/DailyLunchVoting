package ru.javapractice.dailylunchvoting.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="menu")
public class Menu extends AbstractBaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="restaurant_id")
    private Restaurant restaurant;

    @ManyToMany(mappedBy = "menu", fetch = FetchType.LAZY)
    @JoinTable(name = "menu_menuitem",
            joinColumns = @JoinColumn(name = "menu_id"),
            inverseJoinColumns = @JoinColumn(name = "menuitem_id"))
    private List<MenuItem> menuItems;

    public Menu() {
    }

    public Menu(Integer id) {
        super(id);
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", menuItems=" + menuItems +
                ", restaurant=" + restaurant +
                '}';
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }
}
