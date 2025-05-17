package ru.javapractice.dailylunchvoting.restaurant.web;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javapractice.dailylunchvoting.common.validation.ValidationUtil;
import ru.javapractice.dailylunchvoting.mapper.MenuMapperService;
import ru.javapractice.dailylunchvoting.restaurant.service.MenuService;
import ru.javapractice.dailylunchvoting.restaurant.to.AssignedMenuTo;

import java.net.URI;

import static ru.javapractice.dailylunchvoting.restaurant.web.AdminMenuAssignmentController.REST_URL;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminMenuAssignmentController {
    public static final String REST_URL = "/api/admin/restaurants";

    private final MenuMapperService menuMapper;
    private final MenuService menuService;

    @GetMapping("/{id}/menu")
    public ResponseEntity<AssignedMenuTo> getMenu(@PathVariable int id) {
        var menu = menuService.get(id);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}/menu")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(uriOfNewResource).body(menuMapper.toAssignedMenuTo(menu));
    }

    @PostMapping("/{id}/menu")
    public ResponseEntity<AssignedMenuTo> createAndAssignMenuToRestaurant(@PathVariable int id, @Valid @RequestBody AssignedMenuTo menuTo) {

        ValidationUtil.checkIsNew(menuTo);
        var createdMenu = menuService.create(menuTo, id);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}/menu")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(uriOfNewResource).body(menuMapper.toAssignedMenuTo(createdMenu));
    }

    @PutMapping("/{id}/menu")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateAssignedMenu(@PathVariable int id, @Valid @RequestBody AssignedMenuTo menuTo) {
        menuService.update(menuTo, id);
    }
}