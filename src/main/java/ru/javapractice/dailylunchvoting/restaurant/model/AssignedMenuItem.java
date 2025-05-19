package ru.javapractice.dailylunchvoting.restaurant.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Persistable;
import ru.javapractice.dailylunchvoting.common.HasCompositeId;
import ru.javapractice.dailylunchvoting.common.validation.NoHtml;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "menu_item_assignment")
@Slf4j
public class AssignedMenuItem implements Persistable<AssignmentMenuItemId>, HasCompositeId<AssignmentMenuItemId> {

    @Version
    @Column(name = "version")
    private Integer version;

    @EmbeddedId
    private AssignmentMenuItemId id;

    @ManyToOne
    @MapsId("menuId")
    @JoinColumn(name = "menu_id")
    @JsonBackReference
    private Menu menu;

    @NotBlank
    @Size(min = 2, max = 128)
    @NoHtml
    protected String name;

    @DecimalMin(value = "0.01", message = "Price must be greater than zero")
    @DecimalMax(value = "100000", message = "Price cannot be greater than 10000")
    @Positive(message = "Price must be a positive number")
    private BigDecimal price;

    public AssignedMenuItem(Menu menu, MenuItem menuItem, BigDecimal price) {
        this.menu = menu;
        this.name = menuItem.getName();
        this.price = price;
        this.id = createAssignedMenuItemId(menu, menuItem.getId());
    }

    private AssignmentMenuItemId createAssignedMenuItemId(Menu menu, Integer id) {
        return new AssignmentMenuItemId(menu.getId(), id, menu.getMenuDate());
    }

    @Override
    public boolean isNew() {
        return this.version == null;
    }
}
