package ru.javapractice.dailylunchvoting.mapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.javapractice.dailylunchvoting.restaurant.model.AssignedMenuItem;
import ru.javapractice.dailylunchvoting.restaurant.model.Menu;
import ru.javapractice.dailylunchvoting.restaurant.model.MenuItem;
import ru.javapractice.dailylunchvoting.restaurant.to.AssignedMenuTo;
import ru.javapractice.dailylunchvoting.restaurant.to.PricedMenuItemTo;
import org.springframework.lang.NonNull;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class MenuMapperService {
    public AssignedMenuTo toAssignedMenuTo(@NonNull Menu menu) {
        List<PricedMenuItemTo> items = menu.getAssignedMenuItems().stream()
                        .map(this::toPricedMenuItemTo)
                        .toList();

        return new AssignedMenuTo(menu.getId(), menu.getMenuDate(), items);
    }

    public PricedMenuItemTo toPricedMenuItemTo(@NonNull AssignedMenuItem item) {
        return new PricedMenuItemTo(
                item.getMenuItem().getId(),
                item.getMenuItem().getName(),
                item.getPrice()
        );
    }

    public MenuItem toMenuItem(@NonNull PricedMenuItemTo pricedMenuItemTo) {
        return new MenuItem(pricedMenuItemTo.id(), pricedMenuItemTo.getName());
    }

    public List<MenuItem> toMenuItems(@NonNull List<PricedMenuItemTo> pricedMenuItemTos) {
        return pricedMenuItemTos.stream()
                .map(this::toMenuItem)
                .toList();
    }
}
