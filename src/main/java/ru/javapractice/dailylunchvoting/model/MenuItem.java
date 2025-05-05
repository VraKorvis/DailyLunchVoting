package ru.javapractice.dailylunchvoting.model;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name="menuitem")
public class MenuItem extends AbstractNamedBaseEntity {

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
