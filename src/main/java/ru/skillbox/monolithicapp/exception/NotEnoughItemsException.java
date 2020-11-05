package ru.skillbox.monolithicapp.exception;

public class NotEnoughItemsException extends RuntimeException {
    public NotEnoughItemsException(String message) {
        super(message);
    }
}
