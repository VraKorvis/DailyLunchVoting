package ru.javapractice.dailylunchvoting.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.javapractice.dailylunchvoting.user.UserData;
import ru.javapractice.dailylunchvoting.user.model.User;

import java.util.Map;

import static java.util.Objects.requireNonNull;

@Component
@Profile("test")
@Slf4j
public class AuthUtilTestImpl implements AuthUserProvider {
    private static final User DEFAULT_USER = new User(UserData.USER_1);

    private static final Map<String, User> TEST_USERS = Map.of(
            UserData.USER_1.getEmail(), DEFAULT_USER,
            UserData.ADMIN.getEmail(), new User(UserData.ADMIN)
    );

    @Override
    public AuthUser safeGet() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            log.warn("Authentication is null, returning default user");
            return new AuthUser(DEFAULT_USER);
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof AuthUser authUser) {
            return authUser;
        }

        if (principal instanceof org.springframework.security.core.userdetails.User springUser) {
            String username = springUser.getUsername();
            User user = TEST_USERS.getOrDefault(username, DEFAULT_USER);
            return new AuthUser(user);
        }

        log.warn("[TEST] Unknown principal type: {}, returning default user", principal.getClass());
        return new AuthUser(DEFAULT_USER);
    }

    @Override
    public AuthUser get() {
        return requireNonNull(safeGet(), "No authorized user found");
    }
}
