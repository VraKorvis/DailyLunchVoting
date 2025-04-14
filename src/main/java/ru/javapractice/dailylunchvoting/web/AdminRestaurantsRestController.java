package ru.javapractice.dailylunchvoting.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javapractice.dailylunchvoting.model.Restaurant;
import ru.javapractice.dailylunchvoting.service.RestaurantService;
import ru.javapractice.dailylunchvoting.to.RestaurantWithMenuTo;

import java.net.URI;
import java.util.List;

import static ru.javapractice.dailylunchvoting.web.AdminRestaurantsRestController.REST_URL;

@RestController
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminRestaurantsRestController {
    private final Logger log = LoggerFactory.getLogger(AdminRestaurantsRestController.class);
    static final String REST_URL = "/api/admin/restaurants";

    private final RestaurantService service;

    public AdminRestaurantsRestController(RestaurantService service) {
        this.service = service;
    }

    @GetMapping
    public List<Restaurant> getAll() {
        log.info("get all ");
        return service.getAll();
    }

    @GetMapping("/with-assigned-menu")
    public List<RestaurantWithMenuTo> getAllWithAssignedMenuForToday() {
        return service.getAllWithAssignedMenuForToday();
    }

    @GetMapping("/without-assigned-menu")
    public List<RestaurantWithMenuTo> getAllWithoutAssignedMenuForToday() {
        return service.getAllWithoutAssignedMenuForToday();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> createWithLocation(@RequestBody Restaurant user) {
        Restaurant created = service.create(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

}
