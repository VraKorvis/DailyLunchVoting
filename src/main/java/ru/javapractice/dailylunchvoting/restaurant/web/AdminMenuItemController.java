package ru.javapractice.dailylunchvoting.restaurant.web;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javapractice.dailylunchvoting.common.validation.ValidationUtil;
import ru.javapractice.dailylunchvoting.restaurant.model.MenuItem;
import ru.javapractice.dailylunchvoting.restaurant.service.MenuItemService;

import java.net.URI;
import java.util.List;

import static ru.javapractice.dailylunchvoting.restaurant.web.AdminMenuItemController.REST_URL;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminMenuItemController {

    public static final String REST_URL = "/api/admin/menu-item-catalog";

    private final MenuItemService menuItemService;

    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> get(@PathVariable int id) {
        log.info("Get menuItem id={}", id);
        MenuItem item = menuItemService.get(id);
        return ResponseEntity.ok(item);
    }

    @GetMapping("/page")
    public List<MenuItem> getMenuItemsPage(@RequestParam int page, @RequestParam int size) {
        log.info("get users page {} with size {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return menuItemService.findAll(pageable).getContent();
    }

    @PostMapping
    public ResponseEntity<MenuItem> create(@Valid @RequestBody MenuItem menuItem) {
        log.info("Create menuItem {}", menuItem);
        ValidationUtil.checkIsNew(menuItem);
        MenuItem created = menuItemService.create(menuItem);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(uri).body(created);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id, @Valid @RequestBody MenuItem menuItem) {
        log.info("Update menuItem id={} with {}", id, menuItem);
        ValidationUtil.assureIdConsistent(menuItem, id);
        menuItemService.update(menuItem);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("Delete menuItem id={}", id);
        menuItemService.delete(id);
    }
}
