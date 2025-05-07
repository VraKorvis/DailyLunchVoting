package ru.javapractice.dailylunchvoting.user;

import ru.javapractice.dailylunchvoting.user.model.Role;
import ru.javapractice.dailylunchvoting.user.model.User;
import ru.javapractice.dailylunchvoting.util.MatcherFactory;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import static ru.javapractice.dailylunchvoting.common.model.BaseEntity.START_SEQ;

public class UserData {
    public static final MatcherFactory.Matcher<User> USER_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(User.class,"registered", "roles");

    public static final int USER_1_ID = START_SEQ;
    public static final int USER_2_ID = START_SEQ + 1;
    public static final int ADMIN_ID = START_SEQ + 2;
    public static final int GUEST_ID = START_SEQ + 3;
    public static final int NOT_FOUND = 10;

    public static final User USER_1 = new User(USER_1_ID, "User1", "user1@yandex.ru", "password", Role.USER);
    public static final User USER_2 = new User(USER_2_ID, "User2", "user2@yandex.ru", "password", Role.USER);
    public static final User ADMIN = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", Role.ADMIN);
    public static final User GUEST = new User(GUEST_ID, "Guest", "guest@gmail.com", "guest");
    public static final String USER_1_EMAIL = "user1@yandex.ru";

    private UserData() {
    }

    public static User getNew() {
        return new User(null, "New", "new@gmail.com", "newPass", false, new Date(), Collections.singleton(Role.USER));
    }

    public static User getUpdated(User u) {
        User updated = new User(u);
        updated.setEmail("update@gmail.com");
        updated.setName("UpdatedName");
        updated.setPassword("newPass");
        updated.setEnabled(false);
        updated.setRoles(Set.of(Role.ADMIN));
        return updated;
    }
}
