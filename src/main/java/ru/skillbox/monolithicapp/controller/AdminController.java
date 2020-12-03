package ru.skillbox.monolithicapp.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.monolithicapp.entity.*;
import ru.skillbox.monolithicapp.exception.UserAlreadyExistException;
import ru.skillbox.monolithicapp.exception.PasswordDoestMatchException;
import ru.skillbox.monolithicapp.model.*;
import ru.skillbox.monolithicapp.model.EUserRole;
import ru.skillbox.monolithicapp.service.AdminService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/admin/")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("login")
    public ResponseEntity<AdminRoles> logIn(HttpServletRequest request,
                                            HttpServletResponse response,
                                            @RequestBody LogInView logInView) {
        Admin admin = adminService.logIn(request, response, logInView);
        return ResponseEntity.ok(new AdminRoles(
                admin.getRoles().stream()
                        .map(UserRole::getAuthority)
                        .collect(Collectors.toSet())));
    }

    @PostMapping("register")
    public void register(@RequestBody UserView registrationData)
            throws UserAlreadyExistException, PasswordDoestMatchException {
        adminService.register(registrationData);
    }

    @GetMapping("roles")
    public ResponseEntity<AdminRoles> roles(@AuthenticationPrincipal Customer customer) {
        if (customer == null) {
            return ResponseEntity.ok(new AdminRoles(Collections.singleton(EUserRole.ROLE_ANONYMOUS.name())));
        }

        return ResponseEntity.ok(new AdminRoles(
                customer.getRoles().stream()
                        .map(UserRole::getAuthority)
                        .collect(Collectors.toSet())));
    }

    @AllArgsConstructor
    @Data
    protected static class AdminRoles {
        private Set<String> roles;
    }

}