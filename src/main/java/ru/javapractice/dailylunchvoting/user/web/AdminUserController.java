package ru.javapractice.dailylunchvoting.user.web;

import jakarta.validation.Valid;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.cache.annotation.Cacheable;

import ru.javapractice.dailylunchvoting.app.config.CacheKeys;
import ru.javapractice.dailylunchvoting.app.config.CacheNames;
import ru.javapractice.dailylunchvoting.user.model.User;

import java.net.URI;
import java.util.List;

import static ru.javapractice.dailylunchvoting.common.validation.ValidationUtil.assureIdConsistent;
import static ru.javapractice.dailylunchvoting.common.validation.ValidationUtil.checkIsNew;

@RestController
@RequestMapping(value = AdminUserController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminUserController extends AbstractUserController {

    public static final String REST_URL = "/api/admin/users";
    private static final Sort SORT = Sort.by(Sort.Direction.ASC, "name", "email");

    @Override
    @Cacheable(value = CacheNames.USER_CACHE, key = CacheKeys.ID)
    @GetMapping("/{id}")
    public User get(@PathVariable int id) {
        return super.get(id);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheNames.USERS_LIST, allEntries = true, beforeInvocation = true),
            @CacheEvict(value = CacheNames.USERS_PAGE, allEntries = true, beforeInvocation = true),
            @CacheEvict(value = CacheNames.USER_CACHE, key = CacheKeys.ID, beforeInvocation = true)
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @Cacheable(value = CacheNames.USERS_LIST, key = CacheKeys.ALL_USERS)
    @GetMapping
    public List<User> getAll() {
        log.info("getAll");
        return repository.findAll(SORT);
    }

    @Cacheable(value = CacheNames.USERS_PAGE, key = CacheKeys.PAGE)
    @GetMapping("/page")
    public List<User> getUsersPage(@RequestParam int page, @RequestParam int size) {
        log.info("get users page {} with size {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable).getContent();
    }

    @Caching(evict = {
            @CacheEvict(value = CacheNames.USERS_LIST, allEntries = true),
            @CacheEvict(value = CacheNames.USERS_PAGE, allEntries = true),
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createWithLocation(@Valid @RequestBody User user) {
        log.info("create {}", user);
        checkIsNew(user);
        var created = repository.prepareAndSave(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Caching(evict = {
            @CacheEvict(value = CacheNames.USERS_LIST, allEntries = true, beforeInvocation = true),
            @CacheEvict(value = CacheNames.USERS_PAGE, allEntries = true, beforeInvocation = true),
            @CacheEvict(value = CacheNames.USER_CACHE, key = CacheKeys.ID, beforeInvocation = true),
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody User user, @PathVariable int id) {
        log.info("update {} with id={}", user, id);
        assureIdConsistent(user, id);
        repository.prepareAndSave(user);
    }

    @GetMapping("/by-email")
    public User getByEmail(@RequestParam String email) {
        log.info("getByEmail {}", email);
        return repository.getExistedByEmail(email);
    }

    @Caching(evict = {
            @CacheEvict(value = CacheNames.USERS_LIST, allEntries = true),
            @CacheEvict(value = CacheNames.USERS_PAGE, allEntries = true),
            @CacheEvict(value = CacheNames.USER_CACHE, key = CacheKeys.ID),
    })
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void enable(@PathVariable int id, @RequestParam boolean enabled) {
        log.info(enabled ? "enable {}" : "disable {}", id);
        User user = repository.getExisted(id);
        user.setEnabled(enabled);
    }
}