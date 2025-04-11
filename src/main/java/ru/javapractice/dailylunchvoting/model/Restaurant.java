package ru.javapractice.dailylunchvoting.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "restaurant")
public class Restaurant extends AbstractNamedBaseEntity {

    public Restaurant() {}

    @SuppressWarnings("CopyConstructorMissesField")
    public Restaurant(Restaurant r) {
        this(r.id, r.name);
    }

    public Restaurant(String name) {
        this(null, name);
    }

    public Restaurant(Integer id, String name) {
        super(id, name);
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    public Set<Menu> menus = Set.of();

    public Set<Menu> getMenus() {
        return menus;
    }

    public void setMenus(Set<Menu> menus) {
        this.menus = menus;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
