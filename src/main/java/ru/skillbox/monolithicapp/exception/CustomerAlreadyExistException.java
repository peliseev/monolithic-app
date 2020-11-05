package ru.skillbox.monolithicapp.exception;

public class CustomerAlreadyExistException extends RuntimeException {
    public CustomerAlreadyExistException(String message) {
        super(message);
    }
}
