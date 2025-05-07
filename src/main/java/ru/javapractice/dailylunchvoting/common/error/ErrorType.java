package ru.javapractice.dailylunchvoting.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {
    APP_ERROR("Application error", HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_DATA("Wrong data", HttpStatus.UNPROCESSABLE_ENTITY),
    BAD_REQUEST("Bad request", HttpStatus.UNPROCESSABLE_ENTITY),
    DATA_CONFLICT("Database conflict", HttpStatus.CONFLICT),
    NOT_FOUND("Resource not found", HttpStatus.NOT_FOUND),
    AUTH_ERROR("Authorization error", HttpStatus.FORBIDDEN),
    UNAUTHORIZED("Request unauthorized", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("Request forbidden", HttpStatus.FORBIDDEN);

    public final String title;
    public final HttpStatus status;

    ErrorType(String title, HttpStatus status) {
        this.title = title;
        this.status = status;
    }

}
