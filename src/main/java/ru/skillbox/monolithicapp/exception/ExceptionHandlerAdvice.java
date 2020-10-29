package ru.skillbox.monolithicapp.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(CustomerAlreadyExistException.class)
    protected ResponseEntity<?> handleCustomerAlreadyExistException() {
        return ResponseEntity.status(409).body("Такой пользователь уже существует");
    }

    @ExceptionHandler(PasswordDoestMatchException.class)
    protected ResponseEntity<?> handlePasswordDoestMatchException() {
        return ResponseEntity.status(409).body("Пароли не совпадают");
    }
}
