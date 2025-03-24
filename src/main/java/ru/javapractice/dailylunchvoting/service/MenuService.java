package ru.javapractice.dailylunchvoting.service;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.javapractice.dailylunchvoting.model.Menu;
import ru.javapractice.dailylunchvoting.repository.datajpa.DataJpaMenuRepository;

import java.util.List;

import static ru.javapractice.dailylunchvoting.util.ValidationUtil.checkNotFound;

@Service
public class MenuService {

    private final DataJpaMenuRepository repository;

    public MenuService(DataJpaMenuRepository repository) {
        this.repository = repository;
    }

    public Menu create(Menu menu) {
        Assert.notNull(menu, "menu must not be null");
        return repository.save(menu);
    }

    public Menu get(int id) {
        return checkNotFound(repository.get(id), id);
    }

    public List<Menu> getAll() {
        return repository.getAll();
    }

    public void update(Menu menu) {
        Assert.notNull(menu, "menu must not be null");
        checkNotFound(repository.save(menu), menu.id());
    }

    public void delete(int id) {
        repository.delete(id);
    }

}
