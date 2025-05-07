package ru.javapractice.dailylunchvoting.common.error;

import lombok.Getter;

@Getter
public class ApiError {
    private final String type;
    private final String title;
    private final int status;
    private final String detail;
    private final String instance;

    public ApiError(String type, String title, int status, String detail, String instance) {
        this.type = type;
        this.title = title;
        this.status = status;
        this.detail = detail;
        this.instance = instance;
    }
}
