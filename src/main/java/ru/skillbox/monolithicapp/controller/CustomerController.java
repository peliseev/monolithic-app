package ru.skillbox.monolithicapp.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.monolithicapp.entity.Customer;
import ru.skillbox.monolithicapp.entity.security.Role;
import ru.skillbox.monolithicapp.exception.CustomerAlreadyExistException;
import ru.skillbox.monolithicapp.exception.PasswordDoestMatchException;
import ru.skillbox.monolithicapp.model.CustomerView;
import ru.skillbox.monolithicapp.model.LogInView;
import ru.skillbox.monolithicapp.service.CustomerService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/login")
    public ResponseEntity logIn(HttpServletRequest request, HttpServletResponse response,
                                @RequestBody LogInView logInView) {
        Customer customer = customerService.logIn(request, response, logInView);
        return ResponseEntity.ok(new CustomerRoles(
                customer.getRoles().stream()
                        .map(Role::getAuthority)
                        .collect(Collectors.toSet())));
    }

    @PostMapping("/register")
    public void register(HttpServletResponse response,
                         @RequestBody CustomerView registrationData)
            throws CustomerAlreadyExistException, PasswordDoestMatchException {
        customerService.register(registrationData);
    }

    @AllArgsConstructor
    @Data
    protected static class CustomerRoles {
        private  Set<String> roles;
    }

}