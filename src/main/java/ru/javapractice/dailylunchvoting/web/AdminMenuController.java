package ru.javapractice.dailylunchvoting.web;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javapractice.dailylunchvoting.restaurant.service.MenuService;
import ru.javapractice.dailylunchvoting.to.MenuTo;

import java.net.URI;
import java.util.stream.Collectors;

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
    public ResponseEntity<MenuTo> createAndAssignMenuToRestaurant(@Valid @RequestBody MenuTo menuTo, @PathVariable int id, BindingResult result) {
        if (result.hasErrors()) {
            String errorFieldsMsg = result.getFieldErrors().stream()
                    .map(fe -> String.format("[%s] %s", fe.getField(), fe.getDefaultMessage()))
                    .collect(Collectors.joining("<br>"));
            throw new IllegalArgumentException(errorFieldsMsg);
        }

        MenuTo createdTo = menuService.create(menuTo, id);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/restaurants/{id}/menu")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(uriOfNewResource).body(createdTo);
    }

    @PutMapping("/restaurants/{id}/menu")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateAssignedMenu(@PathVariable int id,
                                   @RequestBody MenuTo menuTo) {
        menuService.update(menuTo, id);
    }
}