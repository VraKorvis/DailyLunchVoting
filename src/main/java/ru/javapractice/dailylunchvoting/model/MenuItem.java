package ru.javapractice.dailylunchvoting.model;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name="menuitem")
public class MenuItem extends AbstractNamedBaseEntity {

    @Column(name = "price", nullable = false)
    @NotBlank
    @Range(min = 1)
    private Float price;

    @ManyToMany(mappedBy = "menuItems", fetch = FetchType.LAZY)
    private List<Menu> menus;

    public MenuItem() {
    }

    public MenuItem(Integer id, String name, Float price) {
        super(id, name);
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

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
