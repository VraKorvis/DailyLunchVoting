package ru.javapractice.dailylunchvoting.model;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name="menuitem")
public class MenuItem extends AbstractNamedBaseEntity {

    @Column(name = "price", nullable = false)
    @Range(min = 1)
    private Float price;

    public MenuItem() {
    }

    public MenuItem(MenuItem item) {
        this(item.id, item.name, item.price);
    }

    public MenuItem(String name, Float price) {
        this(null, name, price);
    }

    public MenuItem(Integer id, String name, Float price) {
        super(id, name);
        this.price = price;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
