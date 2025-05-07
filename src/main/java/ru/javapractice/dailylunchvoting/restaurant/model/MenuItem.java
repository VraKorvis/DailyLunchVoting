package ru.javapractice.dailylunchvoting.restaurant.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;
import ru.javapractice.dailylunchvoting.common.model.BaseEntity;
import ru.javapractice.dailylunchvoting.common.model.NamedEntity;

import java.math.BigDecimal;

@Entity
@Table(name="menuitem")
public class MenuItem extends NamedEntity {

    @Column(name = "price", nullable = false)
    @DecimalMin(value = "0.01", message = "Price must be greater than zero")
    @DecimalMax(value = "100000", message = "Price cannot be greater than 10000")
    @Positive(message = "Price must be a positive number")
    private BigDecimal price;

    public MenuItem() {
    }

    public MenuItem(MenuItem item) {
        this(item.id, item.name, item.price);
    }

    public MenuItem(String name, BigDecimal price) {
        this(null, name, price);
    }

    public MenuItem(Integer id, String name, BigDecimal price) {
        super(id, name);
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
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
