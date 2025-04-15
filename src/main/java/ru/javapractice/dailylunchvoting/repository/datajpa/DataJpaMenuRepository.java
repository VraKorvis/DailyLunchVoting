package ru.javapractice.dailylunchvoting.repository.datajpa;

import org.springframework.stereotype.Repository;
import ru.javapractice.dailylunchvoting.model.Menu;
import ru.javapractice.dailylunchvoting.repository.MenuRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class DataJpaMenuRepository implements MenuRepository {

    private final ProxyCrudMenuRepository proxyCrudMenuRepository;

    public DataJpaMenuRepository(ProxyCrudMenuRepository proxyCrudMenuRepository) {
        this.proxyCrudMenuRepository = proxyCrudMenuRepository;
    }

    @Override
    public List<Menu> getAll() {
        return proxyCrudMenuRepository.findAll();
    }

    @Override
    public Menu getReferenceById(Integer id) {
        return proxyCrudMenuRepository.getReferenceById(id);
    }

    @Override
    public Optional<Menu> findByRestaurantIdForToday(Integer id) {
        return proxyCrudMenuRepository.findByRestaurantIdForToday(id);
    }

    @Override
    public Optional<Menu> findById(int id) {
        return proxyCrudMenuRepository.findById(id);
    }

    @Override
    public Menu save(Menu menu) {
        return proxyCrudMenuRepository.save(menu);
    }

    @Override
    public boolean delete(Integer id) {
        return proxyCrudMenuRepository.delete(id) != 0;
    }
}
