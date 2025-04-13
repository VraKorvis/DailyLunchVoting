package ru.javapractice.dailylunchvoting.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.javapractice.dailylunchvoting.model.Restaurant;
import ru.javapractice.dailylunchvoting.repository.RestaurantRepository;
import ru.javapractice.dailylunchvoting.util.exception.NotFoundException;

import java.util.List;

@Repository
public class DataJpaRestaurantRepository implements RestaurantRepository {
    private static final Sort SORT_NAME = Sort.by(Sort.Direction.ASC, "name");

    private final ProxyCrudRestaurantRepository proxyCrudRestaurantRepository;

    public DataJpaRestaurantRepository(ProxyCrudRestaurantRepository proxyCrudRestaurantRepository) {
        this.proxyCrudRestaurantRepository = proxyCrudRestaurantRepository;
    }

    @Override
    public List<Restaurant> getAll() {
        return proxyCrudRestaurantRepository.findAll(SORT_NAME);
    }

    @Override
    public Restaurant get(Integer id) {
        return proxyCrudRestaurantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurant with id=" + id + " not found"));
    }

    @Override
    public Restaurant getReferenceById(Integer id) {
        return proxyCrudRestaurantRepository.getReferenceById(id);
    }

    @Override
    public List<Restaurant> getAllWithMenuForToday() {
        return proxyCrudRestaurantRepository.getAllWithMenuForToday();
    }

    @Override
    public Restaurant findByIdWithMenuForToday(Integer id) {
        return proxyCrudRestaurantRepository.findByIdWithMenuForToday(id)
                .orElseThrow(() -> new NotFoundException("Restaurant with id=" + id + " not found"));
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        return proxyCrudRestaurantRepository.save(restaurant);
    }

}
