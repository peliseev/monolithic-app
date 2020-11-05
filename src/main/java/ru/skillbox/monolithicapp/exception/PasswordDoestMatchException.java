package ru.skillbox.monolithicapp.exception;

public class PasswordDoestMatchException extends RuntimeException {
    public PasswordDoestMatchException(String message) {
        super(message);
    }
}
