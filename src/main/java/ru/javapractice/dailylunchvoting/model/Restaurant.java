package ru.javapractice.dailylunchvoting.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "restaurant")
public class Restaurant extends AbstractNamedBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "restaurant_menu_history",
            joinColumns = @JoinColumn(name = "restaurant_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_id"))
    private Menu menu;

    //TODO fk field
//    @Column(name="fk_key", updatable=false, insertable=false)
//    private Long menu_fk;

    public Restaurant() {}

    public Restaurant(Restaurant r) {
        this(r.id, r.name);
    }

    public Restaurant(String name) {
        this(null, name);
    }

    public Restaurant(Integer id, String name) {
        super(id, name);
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
