package ru.javapractice.dailylunchvoting.util;

import static ru.javapractice.dailylunchvoting.common.model.BaseEntity.START_SEQ;

public class SecurityUtil {
    private static final int id = START_SEQ;

    public static int authUserId() {
        return id;
    }

    private SecurityUtil() {}
}
