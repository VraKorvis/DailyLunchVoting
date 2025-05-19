package ru.javapractice.dailylunchvoting.restaurant.service;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.javapractice.dailylunchvoting.mapper.RestaurantMapper;
import ru.javapractice.dailylunchvoting.mapper.RestaurantMapperService;
import ru.javapractice.dailylunchvoting.restaurant.model.Restaurant;
import ru.javapractice.dailylunchvoting.restaurant.repository.RestaurantRepository;
import ru.javapractice.dailylunchvoting.restaurant.to.RestaurantTo;
import ru.javapractice.dailylunchvoting.restaurant.to.RestaurantWithAssignedMenuTo;

import java.time.LocalDate;
import java.util.List;

import static ru.javapractice.dailylunchvoting.app.config.CacheKeys.*;
import static ru.javapractice.dailylunchvoting.app.config.CacheNames.*;

@Service
@AllArgsConstructor
public class RestaurantService {

    private static final Sort SORT_NAME = Sort.by(Sort.Direction.ASC, "name");

    private final RestaurantMapperService restaurantMapperService;
    private final RestaurantMapper restaurantMapper;
    private final RestaurantRepository repository;

    @CacheEvict(value = {
            RESTAURANT_LIST,
            RESTAURANTS_WITH_TODAY_MENU_LIST,
            RESTAURANT
    },
            allEntries = true)
    public Restaurant create(RestaurantTo restaurantDto) {
        return repository.save(new Restaurant(restaurantDto.getName()));
    }

    @CacheEvict(value = {
            RESTAURANT_LIST,
            RESTAURANTS_WITH_TODAY_MENU_LIST,
            RESTAURANT
    },
            allEntries = true)
    public void update(Restaurant restaurant) {
        repository.save(restaurant);
    }

    @Cacheable(value = RESTAURANT, key = ID)
    public RestaurantTo get(int id) {
        return restaurantMapper.toRestaurantTo(repository.getExisted(id));
    }

    @Cacheable(RESTAURANT_LIST)
    public List<RestaurantTo> getAll() {
        return restaurantMapper.toRestaurantTos(repository.findAll(SORT_NAME));
    }

    @Cacheable(value = RESTAURANTS_WITH_TODAY_MENU_PAGE, key = PAGEABLE)
    public Page<RestaurantWithAssignedMenuTo> findAllWithAssignedMenuForToday(Pageable pageable) {
        return findAllWithAssignedMenuForDate(LocalDate.now(), pageable);
    }

    public Page<RestaurantWithAssignedMenuTo> findAllWithAssignedMenuForDate(LocalDate menuDate, Pageable pageable) {
        Page<Restaurant> page = repository.findPageWithAssignedMenuForDate(menuDate, pageable);
        List<RestaurantWithAssignedMenuTo> mapped = restaurantMapperService.toWithAssignedMenuTos(page.getContent());
        return new PageImpl<>(mapped, pageable, page.getTotalElements());
    }

    @Cacheable(RESTAURANTS_WITH_TODAY_MENU_LIST)
    public List<RestaurantWithAssignedMenuTo> findAllWithAssignedMenuForToday() {
        return findAllWithAssignedMenuForDate(LocalDate.now());
    }

    public List<RestaurantWithAssignedMenuTo> findAllWithAssignedMenuForDate(LocalDate date) {
        return restaurantMapperService.toWithAssignedMenuTos(repository.findAllWithAssignedMenuForDate(date));
    }

    public List<RestaurantTo> findAllWithoutAssignedMenuForToday() {
        return findAllWithoutAssignedMenuForDate(LocalDate.now());
    }

    public List<RestaurantTo> findAllWithoutAssignedMenuForDate(LocalDate date) {
        return restaurantMapper.toRestaurantTos(repository.findAllWithoutAssignedMenuForDate(date));
    }

}
