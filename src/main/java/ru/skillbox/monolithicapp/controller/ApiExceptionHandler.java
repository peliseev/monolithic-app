package ru.skillbox.monolithicapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.skillbox.monolithicapp.exception.*;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(CustomerAlreadyExistException.class)
    protected ResponseEntity<?> handleCustomerAlreadyExistException() {
        return ResponseEntity.status(409).body("Такой пользователь уже существует");
    }

    @ExceptionHandler(PasswordDoestMatchException.class)
    protected ResponseEntity<?> handlePasswordDoestMatchException() {
        return ResponseEntity.status(409).body("Пароли не совпадают");
    }

    @ExceptionHandler(NotEnoughItemsException.class)
    protected ResponseEntity<?> handleNotEnoughItemsException() {
        return ResponseEntity.status(400).body("Недостоточо позиций на складе");
    }

    @ExceptionHandler(NoItemsException.class)
    protected ResponseEntity<?> handleNoItemsException() {
        return ResponseEntity.status(400).body("Корзина пуста");
    }

    @ExceptionHandler(ItemNoFoundException.class)
    protected ResponseEntity<?> handleItemNoFoundException() {
        return ResponseEntity.status(400).body("Такой позиции не существует");
    }

}
