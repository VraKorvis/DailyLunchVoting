package ru.javapractice.dailylunchvoting.restaurant.web;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javapractice.dailylunchvoting.common.validation.ValidationUtil;
import ru.javapractice.dailylunchvoting.mapper.RestaurantMapper;
import ru.javapractice.dailylunchvoting.restaurant.service.RestaurantService;
import ru.javapractice.dailylunchvoting.restaurant.to.RestaurantTo;
import ru.javapractice.dailylunchvoting.restaurant.to.RestaurantWithAssignedMenuTo;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.javapractice.dailylunchvoting.restaurant.web.AdminRestaurantRestController.REST_URL;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminRestaurantRestController {
    static final String REST_URL = "/api/admin/restaurants";

    RestaurantMapper restaurantMapper;
    RestaurantService service;

    @GetMapping
    public List<RestaurantTo> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public RestaurantTo get(@PathVariable int id) {
        return service.get(id);
    }

    @GetMapping("/without-assigned-menu/today")
    public List<RestaurantTo> getAllWithoutAssignedMenuForToday() {
        return service.findAllWithoutAssignedMenuForToday();
    }

    @GetMapping("/without-assigned-menu")
    public List<RestaurantTo> getAllWithoutAssignedMenuForDate(@RequestParam("date") LocalDate date) {
        return service.findAllWithoutAssignedMenuForDate(date);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestaurantTo> createWithLocation(@Valid @RequestBody RestaurantTo restaurant) {
        ValidationUtil.checkIsNew(restaurant);
        var created = service.create(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(restaurantMapper.toRestaurantTo(created));
    }

}
