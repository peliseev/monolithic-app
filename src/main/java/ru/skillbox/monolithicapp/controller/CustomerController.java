package ru.skillbox.monolithicapp.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.monolithicapp.exception.CustomerAlreadyExistException;
import ru.skillbox.monolithicapp.exception.PasswordDoestMatchException;
import ru.skillbox.monolithicapp.model.CustomerView;
import ru.skillbox.monolithicapp.model.LogInView;
import ru.skillbox.monolithicapp.service.CustomerService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/login")
    public void logIn(HttpServletRequest request, HttpServletResponse response,
                                   @RequestBody LogInView logInView) {
        customerService.logIn(request, response, logInView);
    }

    @PostMapping("/register")
    public void register(HttpServletResponse response,
                         @RequestBody CustomerView registrationData)
            throws CustomerAlreadyExistException, PasswordDoestMatchException {
        customerService.register(registrationData);
    }

}
