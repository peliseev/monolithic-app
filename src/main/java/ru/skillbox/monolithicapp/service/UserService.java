package ru.skillbox.monolithicapp.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.stereotype.Service;
import ru.skillbox.monolithicapp.entity.User;
import ru.skillbox.monolithicapp.entity.UserRole;
import ru.skillbox.monolithicapp.exception.PasswordDoestMatchException;
import ru.skillbox.monolithicapp.exception.UserAlreadyExistException;
import ru.skillbox.monolithicapp.model.*;
import ru.skillbox.monolithicapp.repository.RoleRepository;
import ru.skillbox.monolithicapp.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenBasedRememberMeServices rememberMeServices;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager consumerAuthenticationManager,
                       @Lazy TokenBasedRememberMeServices rememberMeServices) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = consumerAuthenticationManager;
        this.rememberMeServices = rememberMeServices;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(name);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("Doesn't have user with %s", name));
        }
        return user;
    }

    public User logIn(HttpServletRequest request, HttpServletResponse response, LogInView logInView) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(logInView.getLogin(), logInView.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        rememberMeServices.onLoginSuccess(request, response, authentication);
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public void logOut(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        rememberMeServices.logout(request, response, authentication);
        SecurityContextHolder.clearContext();
    }

    public void register(UserView registrationData) throws UserAlreadyExistException, PasswordDoestMatchException {
        String login = registrationData.getLogin();
        User userFromDB = userRepository.findByUsername(login);
        if (userFromDB != null) {
            throw new UserAlreadyExistException(String.format("Client with login %s already exist", login));
        }
        if (!Objects.equals(registrationData.getPassword(), registrationData.getPasswordConfirm())) {
            throw new PasswordDoestMatchException("Passwords doesn't match!");
        }
        userRepository.save(getUser(registrationData));
    }

    private User getUser(UserView userView) {
        User user = new User();

        user.setPassword(passwordEncoder.encode(userView.getPassword()));
        user.setAddress(userView.getAddress());
        user.setEmail(userView.getEmail());
        user.setFirstName(userView.getFirstName());
        user.setLastName(userView.getFirstName());
        user.setUsername(userView.getLogin());
        user.setRoles(Collections.singleton(roleRepository.findByName(EUserRole.ROLE_CUSTOMER)));

        return user;
    }

    public List<UserViewForAdmin> getUsers() {
        List<UserViewForAdmin> usersResult = new ArrayList<>();
        for (User userFromDb : userRepository.findAll()) {
            usersResult.add(convertUserFromDbToView(userFromDb));
        }
        return usersResult;
    }

    private UserViewForAdmin convertUserFromDbToView(User userFromDb) {
        UserViewForAdmin userViewForAdmin = new UserViewForAdmin();
        userViewForAdmin.setId(userFromDb.getId());
        userViewForAdmin.setLogin(userFromDb.getUsername());
        userViewForAdmin.setFirstName(userFromDb.getFirstName());
        userViewForAdmin.setLastName(userFromDb.getLastName());
        userViewForAdmin.setEmail(userFromDb.getEmail());
        userViewForAdmin.setAddress(userFromDb.getAddress());
        userViewForAdmin.setRoles(convertRolesFromDbToView(userFromDb));
        return userViewForAdmin;
    }

    private List<EUserRole> convertRolesFromDbToView(User userFromDb) {
        List<EUserRole> userRolesResult = new ArrayList<>();
        for (UserRole userRoleFromDb : userFromDb.getRoles()) {
            userRolesResult.add(userRoleFromDb.getName());
        }
        return userRolesResult;
    }

    public void changePassword(Integer userId, String newPassword) {
        User user = userRepository.findById(userId).get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void delete(Integer userId) {
        userRepository.deleteById(userId);
    }
}
