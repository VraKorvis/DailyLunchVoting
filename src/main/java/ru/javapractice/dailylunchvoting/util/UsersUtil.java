package ru.javapractice.dailylunchvoting.util;

import ru.javapractice.dailylunchvoting.model.Role;
import ru.javapractice.dailylunchvoting.model.User;
import ru.javapractice.dailylunchvoting.to.UserTo;

public class UsersUtil {

    public static User createNewFromTo(UserTo userTo) {
        return new User(null, userTo.getName(), userTo.getEmail().toLowerCase(), userTo.getPassword(), Role.USER);
    }

    public static User updateFromTo(User user, UserTo userTo) {
        user.setName(userTo.getName());
        user.setEmail(userTo.getEmail().toLowerCase());
        user.setPassword(userTo.getPassword());
        return user;
    }
}