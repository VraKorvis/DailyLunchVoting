package ru.javapractice.dailylunchvoting.restaurant.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.javapractice.dailylunchvoting.restaurant.service.RestaurantService;
import ru.javapractice.dailylunchvoting.restaurant.to.RestaurantWithAssignedMenuTo;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping(value = RestaurantRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantRestController {
    static final String REST_URL = "/api/restaurants";

    private final RestaurantService service;

    @GetMapping("/with-assigned-menu")
    public List<RestaurantWithAssignedMenuTo> getAllWithMenuForToday() {
        return service.findAllWithAssignedMenuForToday();
    }

    @GetMapping("/with-assigned-menu/page")
    public List<RestaurantWithAssignedMenuTo> getAllWithMenuForToday(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        return service.findAllWithAssignedMenuForToday(pageable).getContent();
    }
}
