package ru.javapractice.dailylunchvoting.restaurant.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.javapractice.dailylunchvoting.common.model.NamedEntity;

@Entity
@ToString
@NoArgsConstructor
@Table(name="menu_item")
public class MenuItem extends NamedEntity {

    public MenuItem(MenuItem item) {
        this(item.id, item.name);
    }

    public MenuItem(String name) {
        this(null, name);
    }

    public MenuItem(Integer id, String name) {
        super(id, name);
    }
}
