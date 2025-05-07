package ru.javapractice.dailylunchvoting.common.exception;

import ru.javapractice.dailylunchvoting.common.error.ErrorType;

public class ConflictException extends AppException {
    public ConflictException(String message) {
        super(message, ErrorType.DATA_CONFLICT);
    }
}