package ru.javapractice.dailylunchvoting.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.javapractice.dailylunchvoting.model.Restaurant;
import ru.javapractice.dailylunchvoting.repository.RestaurantRepository;

import java.util.List;

@Repository
public class DataJpaRestaurantRepository implements RestaurantRepository {
    private static final Sort SORT_NAME = Sort.by(Sort.Direction.ASC, "name");

    private final ProxyCrudRestaurantRepository proxyCrudRestaurantRepository;

    public DataJpaRestaurantRepository(ProxyCrudRestaurantRepository proxyCrudRestaurantRepository) {
        this.proxyCrudRestaurantRepository = proxyCrudRestaurantRepository;
    }

    @Override
    public Restaurant get(Integer id) {
        return proxyCrudRestaurantRepository.findById(id).orElse(null);
    }

    @Override
    public Restaurant getReferenceById(Integer id) {
        return proxyCrudRestaurantRepository.getReferenceById(id);
    }

    @Override
    public Restaurant getWithMenuForToday(Integer id) {
        return proxyCrudRestaurantRepository.getWithMenu(id).orElse(null);
    }

    @Override
    public List<Restaurant> getAll() {
        return proxyCrudRestaurantRepository.findAll(SORT_NAME);
    }

    @Override
    public List<Restaurant> getAllRestaurantsWithMenuForToday() {
        return proxyCrudRestaurantRepository.findRestaurantsWithMenuForToday();
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        return proxyCrudRestaurantRepository.save(restaurant);
    }

    @Override
    public boolean delete(Integer id) {
        return proxyCrudRestaurantRepository.delete(id) != 0;
    }
}
