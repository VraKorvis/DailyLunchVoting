package ru.javapractice.dailylunchvoting.common.exception;

import static ru.javapractice.dailylunchvoting.common.error.ErrorType.BAD_REQUEST;

public class IllegalRequestDataException extends AppException {
    public IllegalRequestDataException(String msg) {
        super(msg, BAD_REQUEST);
    }
}