package ru.javapractice.dailylunchvoting.restaurant.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.javapractice.dailylunchvoting.app.config.CacheKeys;
import ru.javapractice.dailylunchvoting.app.config.CacheNames;
import ru.javapractice.dailylunchvoting.common.exception.NotFoundException;
import ru.javapractice.dailylunchvoting.restaurant.model.MenuItem;
import ru.javapractice.dailylunchvoting.restaurant.repository.MenuItemRepository;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.javapractice.dailylunchvoting.app.config.CacheNames.MENU_ITEMS_MAP;
import static ru.javapractice.dailylunchvoting.common.MessageConstants.MENU_ITEM_NOT_FOUND;

@Service
@AllArgsConstructor
@Slf4j
public class MenuItemService {

    private final MenuItemRepository repository;

    @Caching(evict = {
            @CacheEvict(value = CacheNames.MENU_ITEMS_LIST, allEntries = true),
            @CacheEvict(value = CacheNames.MENU_ITEMS_PAGE, allEntries = true),
            @CacheEvict(value = CacheNames.MENU_ITEMS_MAP, allEntries = true),
    })    public MenuItem create(MenuItem menuItem) {
        Assert.notNull(menuItem, "menuItem must not be null");
        return repository.save(menuItem);
    }

    @Cacheable(value = CacheNames.MENU_ITEM, key = CacheKeys.ID)
    public MenuItem get(int id) {
        return repository.getExisted(id);
    }

    @Cacheable(value = CacheNames.MENU_ITEMS_LIST)
    public List<MenuItem> findAll() {
        return repository.findAll();
    }

    @Cacheable(value = CacheNames.MENU_ITEMS_PAGE, key = CacheKeys.PAGEABLE)
    public Page<MenuItem> findAll(Pageable pageable) {
        log.debug("[CACHE] ENTER Service.findAll, pageable = {}", pageable);
        return repository.findAll(pageable);
    }

    @Caching(evict = {
            @CacheEvict(value = CacheNames.MENU_ITEMS_LIST, allEntries = true),
            @CacheEvict(value = CacheNames.MENU_ITEMS_PAGE, allEntries = true),
            @CacheEvict(value = CacheNames.MENU_ITEMS_MAP, allEntries = true),
            @CacheEvict(value = CacheNames.MENU_ITEM, key = CacheKeys.MENU_ITEM_ID)
    })
    public void update(MenuItem menuItem) {
        Assert.notNull(menuItem, "menuItem must not be null");
        if (!repository.existsById(menuItem.getId())) {
            throw new NotFoundException(MENU_ITEM_NOT_FOUND.formatted(menuItem.getId()));
        }
        repository.save(menuItem);
    }

    @Caching(evict = {
            @CacheEvict(value = CacheNames.MENU_ITEMS_LIST, allEntries = true),
            @CacheEvict(value = CacheNames.MENU_ITEMS_PAGE, allEntries = true),
            @CacheEvict(value = CacheNames.MENU_ITEMS_MAP, allEntries = true),
            @CacheEvict(value = CacheNames.MENU_ITEM, key = CacheKeys.ID)
    })
    public void delete(int id) {
        repository.delete(id);
    }

    @Cacheable(MENU_ITEMS_MAP)
    public Map<Integer, MenuItem> getItemMap() {
        return repository.findAll().stream()
                .collect(Collectors.toMap(MenuItem::getId, Function.identity()));
    }

}
