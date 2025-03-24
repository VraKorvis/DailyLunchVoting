package ru.javapractice.dailylunchvoting.model;

import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="menu")
public class Menu extends AbstractBaseEntity {

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "menu_menuitem_link",
            joinColumns = @JoinColumn(name = "menu_id"),
            inverseJoinColumns = @JoinColumn(name = "menuitem_id"))
    @BatchSize(size = 200)
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
                '}';
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }
}
