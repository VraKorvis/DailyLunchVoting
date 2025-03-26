package ru.javapractice.dailylunchvoting.model;

import javax.persistence.*;

@Entity
@Table(name = "restaurant")
public class Restaurant extends AbstractNamedBaseEntity {

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

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
