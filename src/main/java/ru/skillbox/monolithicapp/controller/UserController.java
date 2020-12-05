package ru.skillbox.monolithicapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.monolithicapp.entity.User;
import ru.skillbox.monolithicapp.entity.UserRole;
import ru.skillbox.monolithicapp.exception.PasswordDoestMatchException;
import ru.skillbox.monolithicapp.exception.UserAlreadyExistException;
import ru.skillbox.monolithicapp.model.*;
import ru.skillbox.monolithicapp.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponse> logIn(HttpServletRequest request,
                                               HttpServletResponse response,
                                               @RequestBody LogInView logInView) {
        User user = userService.logIn(request, response, logInView.getLogin(), logInView.getPassword());

        if (user.isPasswordMustBeChanged()) {
            userService.logOut(request, response);
            return ok(new LoginResponse(true, Collections.emptySet()));
        }

        return ok(new LoginResponse(false,
                user.getRoles().stream().map(UserRole::getAuthority).collect(Collectors.toSet())));
    }

    @PostMapping("logout")
    public ResponseEntity<Void> logOut(HttpServletRequest request,
                                       HttpServletResponse response) {
        userService.logOut(request, response);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("register")
    public void register(@RequestBody UserView registrationData)
            throws UserAlreadyExistException, PasswordDoestMatchException {
        userService.register(registrationData, EUserRole.ROLE_CUSTOMER);
    }

    @PostMapping("registerByAdmin")
    public void registerByAdmin(@RequestBody UserViewForAdminRequest registrationData)
            throws UserAlreadyExistException, PasswordDoestMatchException {
        userService.register(registrationData, registrationData.getRole());
    }

    @GetMapping("roles")
    public ResponseEntity<UserRolesResponse> getUserRoles(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ok(new UserRolesResponse(Collections.singleton(EUserRole.ROLE_ANONYMOUS.name())));
        }
        return ok(new UserRolesResponse(user.getRoles().stream().map(UserRole::getAuthority).collect(Collectors.toSet())));
    }

    @GetMapping("role/all")
    public ResponseEntity<UserRolesResponse> getUserRoles() {
        return ok(new UserRolesResponse(userService.getRoles()));
    }

    @PostMapping("{id}/delete")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("{id}/password/reset")
    public ResponseEntity<Void> resetUserPassword(@PathVariable int id) {
        userService.resetPassword(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("password/change")
    public ResponseEntity<String> changePassword(@RequestBody LoginAndChangePasswordView logInView) {
        if (!userService.isPasswordValid(logInView.getLogin(), logInView.getOldPassword())) {
            return ResponseEntity.status(403).body("Указан неверный текущий пароль.");
        }
        userService.changePassword(logInView.getLogin(), logInView.getNewPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("all")
    public ResponseEntity<List<UserViewForAdminResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

}