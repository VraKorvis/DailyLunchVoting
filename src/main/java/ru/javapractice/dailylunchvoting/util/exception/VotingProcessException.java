package ru.javapractice.dailylunchvoting.util.exception;

public class VotingProcessException extends RuntimeException {
    public VotingProcessException(String s) {
        super(s);
    }

    public VotingProcessException(String message, Throwable cause) {
        super(message, cause);
    }
}
