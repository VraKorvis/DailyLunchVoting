package ru.javapractice.dailylunchvoting.restaurant.web;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javapractice.dailylunchvoting.mapper.MenuMapperService;
import ru.javapractice.dailylunchvoting.restaurant.model.Menu;
import ru.javapractice.dailylunchvoting.restaurant.service.MenuService;
import ru.javapractice.dailylunchvoting.restaurant.to.AssignedMenuTo;

import java.net.URI;

import static ru.javapractice.dailylunchvoting.restaurant.web.AdminMenuController.REST_URL;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminMenuController {
    public static final String REST_URL = "/api/admin";

    private final MenuMapperService menuMapper;
    private final MenuService menuService;

    @PostMapping("/restaurants/{id}/menu")
    public ResponseEntity<AssignedMenuTo> createAndAssignMenuToRestaurant(@Valid @RequestBody AssignedMenuTo menuTo, @PathVariable int id) {

        Menu createdMenu = menuService.create(menuTo, id);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/restaurants/{id}/menu")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(uriOfNewResource).body(menuMapper.toAssignedMenuTo(createdMenu));
    }

    @PutMapping("/restaurants/{id}/menu")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateAssignedMenu(@PathVariable int id, @RequestBody AssignedMenuTo menuTo) {
        menuService.update(menuTo, id);
    }
}