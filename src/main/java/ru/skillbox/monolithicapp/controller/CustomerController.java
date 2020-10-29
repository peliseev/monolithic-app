package ru.skillbox.monolithicapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.monolithicapp.model.CustomerView;
import ru.skillbox.monolithicapp.model.LogInView;
import ru.skillbox.monolithicapp.service.CustomerService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class CustomerController extends BaseController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<?> register(@RequestBody CustomerView customerView) {
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/login")
    public ResponseEntity<?> logIn(HttpServletRequest request, HttpServletResponse response,
                                   @RequestBody LogInView logInView) {
        customerService.logIn(request, response, logInView);
        return ResponseEntity.ok("Logged In!");
    }

}
