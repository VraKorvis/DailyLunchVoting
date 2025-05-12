package ru.javapractice.dailylunchvoting.restaurant.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.javapractice.dailylunchvoting.common.model.NamedEntity;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "restaurant")
@NamedEntityGraph(
        name = Restaurant.WITH_MENUS,
        attributeNodes = @NamedAttributeNode("menus")
)
@NoArgsConstructor
public class Restaurant extends NamedEntity {

    public static final String WITH_MENUS = "Restaurant.withMenus";

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
    @JsonManagedReference
    public List<Menu> menus = List.of();

}
