package ru.javapractice.dailylunchvoting.user.web;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javapractice.dailylunchvoting.app.AuthUser;
import ru.javapractice.dailylunchvoting.mapper.UserMapper;
import ru.javapractice.dailylunchvoting.user.model.User;
import ru.javapractice.dailylunchvoting.user.to.UserTo;

import java.net.URI;

import static ru.javapractice.dailylunchvoting.common.validation.ValidationUtil.assureIdConsistent;
import static ru.javapractice.dailylunchvoting.common.validation.ValidationUtil.checkIsNew;

@RestController
@RequestMapping(value = ProfileController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class ProfileController extends AbstractUserController {
    static final String REST_URL = "/api/profile";

    UserMapper userMapper;

    @GetMapping
    public User get(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get {}", authUser);
        return authUser.getUser();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        super.delete(authUser.id());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> register(@Valid @RequestBody UserTo userTo) {
        log.info("register {}", userTo);
        checkIsNew(userTo);
        var created = repository.prepareAndSave(userMapper.createNewFromTo(userTo));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@RequestBody @Valid UserTo userTo, @AuthenticationPrincipal AuthUser authUser) {
        log.info("update {} with id={}", userTo, authUser.id());
        assureIdConsistent(userTo, authUser.id());
        var user = authUser.getUser();
        userMapper.updateFromTo(user, userTo);
        repository.prepareAndSave(user);
    }
}