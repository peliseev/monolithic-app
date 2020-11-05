package ru.skillbox.monolithicapp.exception;

public class NoItemsException extends RuntimeException {
    public NoItemsException(String message) {
        super(message);
    }
}
