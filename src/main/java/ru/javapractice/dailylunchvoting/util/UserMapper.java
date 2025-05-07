package ru.javapractice.dailylunchvoting.util;

import ru.javapractice.dailylunchvoting.to.UserTo;
import ru.javapractice.dailylunchvoting.user.model.Role;
import ru.javapractice.dailylunchvoting.user.model.User;

public class UserMapper {

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