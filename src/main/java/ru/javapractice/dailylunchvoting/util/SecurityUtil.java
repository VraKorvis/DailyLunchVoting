package ru.javapractice.dailylunchvoting.util;

import ru.javapractice.dailylunchvoting.model.AbstractBaseEntity;

public class SecurityUtil {
    private static int id = AbstractBaseEntity.START_SEQ;

    public static int authUserId() {
        return id;
    }

    private SecurityUtil() {}
}
