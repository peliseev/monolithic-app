package ru.skillbox.monolithicapp.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
import java.util.stream.Collectors;

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
    public User loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(name);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("Doesn't have user with %s", name));
        }
        return user;
    }

    public boolean isPasswordValid(String login, String password) {
        User user = userRepository.findByUsername(login);
        return passwordEncoder.matches(password, user.getPassword());
    }

    public User logIn(HttpServletRequest request, HttpServletResponse response, String login, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login, password)
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

    public void register(UserView registrationData, EUserRole userRole) throws UserAlreadyExistException, PasswordDoestMatchException {
        String login = registrationData.getLogin();
        User userFromDB = userRepository.findByUsername(login);
        if (userFromDB != null) {
            throw new UserAlreadyExistException(String.format("Client with login %s already exist", login));
        }
        if (!Objects.equals(registrationData.getPassword(), registrationData.getPasswordConfirm())) {
            throw new PasswordDoestMatchException("Passwords doesn't match!");
        }
        userRepository.save(getUser(registrationData, userRole));
    }

    private User getUser(UserView userView, EUserRole userRole) {
        User user = new User();

        user.setPassword(passwordEncoder.encode(userView.getPassword()));
        user.setPasswordMustBeChanged(false);
        user.setAddress(userView.getAddress());
        user.setEmail(userView.getEmail());
        user.setFirstName(userView.getFirstName());
        user.setLastName(userView.getFirstName());
        user.setUsername(userView.getLogin());
        user.setRoles(Collections.singleton(roleRepository.findByName(userRole)));

        return user;
    }

    public Set<String> getRoles() {
        return roleRepository.findAll().stream().map(UserRole::getAuthority).collect(Collectors.toSet());
    }

    public List<UserViewForAdminResponse> getUsers() {
        List<UserViewForAdminResponse> usersResult = new ArrayList<>();
        for (User userFromDb : userRepository.findAll()) {
            usersResult.add(convertUserFromDbToView(userFromDb));
        }
        return usersResult;
    }

    private UserViewForAdminResponse convertUserFromDbToView(User userFromDb) {
        UserViewForAdminResponse userViewForAdminResponse = new UserViewForAdminResponse();
        userViewForAdminResponse.setId(userFromDb.getId());
        userViewForAdminResponse.setLogin(userFromDb.getUsername());
        userViewForAdminResponse.setFirstName(userFromDb.getFirstName());
        userViewForAdminResponse.setLastName(userFromDb.getLastName());
        userViewForAdminResponse.setEmail(userFromDb.getEmail());
        userViewForAdminResponse.setAddress(userFromDb.getAddress());
        userViewForAdminResponse.setRoles(convertRolesFromDbToView(userFromDb));
        return userViewForAdminResponse;
    }

    private List<EUserRole> convertRolesFromDbToView(User userFromDb) {
        List<EUserRole> userRolesResult = new ArrayList<>();
        for (UserRole userRoleFromDb : userFromDb.getRoles()) {
            userRolesResult.add(userRoleFromDb.getName());
        }
        return userRolesResult;
    }

    public void resetPassword(Integer userId) {
        User user = userRepository.findById(userId).get();
        user.setPasswordMustBeChanged(true);
        userRepository.save(user);
    }

    public void changePassword(String login, String newPassword) {
        User user = userRepository.findByUsername(login);
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordMustBeChanged(false);
        userRepository.save(user);
    }

    public void delete(Integer userId) {
        userRepository.deleteById(userId);
    }
}
