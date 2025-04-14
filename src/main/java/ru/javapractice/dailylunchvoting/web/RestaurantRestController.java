package ru.javapractice.dailylunchvoting.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javapractice.dailylunchvoting.service.RestaurantService;
import ru.javapractice.dailylunchvoting.to.RestaurantWithMenuTo;

import java.util.List;

@RestController
@RequestMapping(value = RestaurantRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantRestController {
    private final Logger log = LoggerFactory.getLogger(RestaurantRestController.class);
    static final String REST_URL = "/api/restaurants";

    private final RestaurantService service;

    public RestaurantRestController(RestaurantService service) {
        this.service = service;
    }

    @GetMapping("/with-menu")
    public List<RestaurantWithMenuTo> getAllWithMenuForToday() {
        log.info("get all with menu for today ");
        return service.getAllWithMenuForToday();
    }

    @GetMapping("/{id}/with-menu")
    public RestaurantWithMenuTo getWithMenuForToday(@PathVariable int id) {
        log.info("get with menu {}", id);
        return service.getWithMenuForToday(id);
    }

}
