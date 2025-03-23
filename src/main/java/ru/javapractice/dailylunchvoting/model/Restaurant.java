package ru.javapractice.dailylunchvoting.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "restaurant")
public class Restaurant extends AbstractNamedBaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @NotNull
    private Menu menu;

    public Restaurant() {}

    public Restaurant(String name, Menu menu) {
        this(null, name, menu);
    }

    public Restaurant(Integer id, String name, Menu menu) {
        super(id, name);
        this.menu = menu;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", menu=" + menu +
                '}';
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }
}
