package ru.javapractice.dailylunchvoting.web;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javapractice.dailylunchvoting.model.Menu;
import ru.javapractice.dailylunchvoting.service.MenuService;
import ru.javapractice.dailylunchvoting.to.MenuTo;

import java.net.URI;

import static ru.javapractice.dailylunchvoting.util.ValidationUtil.assureIdConsistent;
import static ru.javapractice.dailylunchvoting.web.AdminMenuController.REST_URL;

@RestController
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminMenuController {
    public static final String REST_URL = "/api/admin";
    private final MenuService menuService;

    public AdminMenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping("/restaurants/{id}/menu")
    public ResponseEntity<Menu> createMenu(@PathVariable int id,
                                           @RequestBody MenuTo menuTo) {
        Menu created = menuService.create(menuTo, id);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{restaurantId}/menu")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping("/menus/{id}")
    public void updateMenu(@PathVariable int id,
                           @RequestBody MenuTo menuTo) {
        assureIdConsistent(menuTo, id);
        menuService.update(menuTo);
    }
}