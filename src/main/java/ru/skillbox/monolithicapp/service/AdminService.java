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
import ru.skillbox.monolithicapp.entity.Admin;
import ru.skillbox.monolithicapp.exception.PasswordDoestMatchException;
import ru.skillbox.monolithicapp.exception.UserAlreadyExistException;
import ru.skillbox.monolithicapp.model.*;
import ru.skillbox.monolithicapp.repository.AdminRepository;
import ru.skillbox.monolithicapp.repository.RoleRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Objects;

@Service("adminService")
@Transactional
public class AdminService implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenBasedRememberMeServices rememberMeServices;

    public AdminService(AdminRepository adminRepository,
                        RoleRepository roleRepository,
                        PasswordEncoder passwordEncoder,
                        AuthenticationManager adminAuthenticationManager,
                        @Lazy TokenBasedRememberMeServices rememberAdminServices) {
        this.adminRepository = adminRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = adminAuthenticationManager;
        this.rememberMeServices = rememberAdminServices;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Admin user = adminRepository.findByUsername(name);
        if (user != null) {
            throw new UsernameNotFoundException(String.format("Doesn't have user with %s", name));
        }
        return user;
    }

    public Admin logIn(HttpServletRequest request, HttpServletResponse response, LogInView logInView) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(logInView.getLogin(), logInView.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        rememberMeServices.onLoginSuccess(request, response, authentication);
        return (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public void register(UserView registrationData) throws UserAlreadyExistException, PasswordDoestMatchException {
        String login = registrationData.getLogin();
        Admin userFromDB = adminRepository.findByUsername(login);
        if (userFromDB != null) {
            throw new UserAlreadyExistException(String.format("User with login %s already exist", login));
        }
        if (!Objects.equals(registrationData.getPassword(), registrationData.getPasswordConfirm())) {
            throw new PasswordDoestMatchException("Passwords doesn't match!");
        }
        adminRepository.save(getUser(registrationData));
    }

    private Admin getUser(UserView userView) {
        Admin user = new Admin();

        user.setPassword(passwordEncoder.encode(userView.getPassword()));
        user.setAddress(userView.getAddress());
        user.setEmail(userView.getEmail());
        user.setFirstName(userView.getFirstName());
        user.setLastName(userView.getFirstName());
        user.setUsername(userView.getLogin());
        user.setRoles(Collections.singleton(roleRepository.findByName(EUserRole.ROLE_ADMIN)));

        return user;
    }
}
