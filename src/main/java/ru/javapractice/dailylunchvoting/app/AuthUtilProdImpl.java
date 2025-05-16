package ru.javapractice.dailylunchvoting.app;

import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static java.util.Objects.requireNonNull;

@Component
@Profile("!test")
public class AuthUtilProdImpl implements AuthUserProvider {

    @Override
    public AuthUser safeGet() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        return (auth.getPrincipal() instanceof AuthUser au) ? au : null;
    }

    @Override
    public AuthUser get() {
        return requireNonNull(safeGet(), "No authorized user found");
    }
}
