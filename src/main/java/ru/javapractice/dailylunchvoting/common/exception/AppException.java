package ru.javapractice.dailylunchvoting.common.exception;

import lombok.Getter;
import org.springframework.lang.NonNull;
import ru.javapractice.dailylunchvoting.common.error.ErrorType;

@Getter
public class AppException extends RuntimeException {
    private final ErrorType errorType;

    public AppException(@NonNull String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }}
