package ru.javapractice.dailylunchvoting.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="menu")
public class Menu extends AbstractBaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="restaurant_id")
    private Restaurant restaurant;

    @Column(name = "date_time", nullable = false, columnDefinition = "timestamp default now()", updatable = false)
    @NotNull
    private Date created;

    @OneToMany(fetch = FetchType.LAZY)
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
                ", created=" + created +
                ", restaurant=" + restaurant +
                '}';
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }
}
