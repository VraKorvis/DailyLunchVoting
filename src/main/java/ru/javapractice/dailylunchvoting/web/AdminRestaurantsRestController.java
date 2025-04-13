package ru.javapractice.dailylunchvoting.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javapractice.dailylunchvoting.model.Restaurant;
import ru.javapractice.dailylunchvoting.service.RestaurantService;

import java.util.List;

import static ru.javapractice.dailylunchvoting.web.AdminRestaurantsRestController.REST_URL;

@RestController
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminRestaurantsRestController {
    private final Logger log = LoggerFactory.getLogger(AdminRestaurantsRestController.class);
    static final String REST_URL = "/rest/admin/restaurants";

    private final RestaurantService service;

    public AdminRestaurantsRestController(RestaurantService service) {
        this.service = service;
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Restaurant create(Restaurant restaurant) {
        log.info("create {}", restaurant);
        return service.create(restaurant);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id);
    }

}
