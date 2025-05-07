package ru.javapractice.dailylunchvoting.common.exception;

import ru.javapractice.dailylunchvoting.common.error.ErrorType;

public class VotingProcessException extends AppException {
    public VotingProcessException(String message) {
        super(message, ErrorType.APP_ERROR);
    }

}
