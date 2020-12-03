package ru.skillbox.monolithicapp.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.stereotype.Service;
import ru.skillbox.monolithicapp.entity.Customer;
import ru.skillbox.monolithicapp.exception.UserAlreadyExistException;
import ru.skillbox.monolithicapp.exception.PasswordDoestMatchException;
import ru.skillbox.monolithicapp.model.UserView;
import ru.skillbox.monolithicapp.model.EUserRole;
import ru.skillbox.monolithicapp.model.LogInView;
import ru.skillbox.monolithicapp.repository.CustomerRepository;
import ru.skillbox.monolithicapp.repository.RoleRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Objects;

@Service("customerService")
@Transactional
public class CustomerService implements UserDetailsService {

    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenBasedRememberMeServices rememberMeServices;

    public CustomerService(CustomerRepository customerRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager consumerAuthenticationManager,
                           @Lazy TokenBasedRememberMeServices rememberCustomerServices) {
        this.customerRepository = customerRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = consumerAuthenticationManager;
        this.rememberMeServices = rememberCustomerServices;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByUsername(name);
        if (customer == null) {
            throw new UsernameNotFoundException(String.format("Doesn't have customer with %s", name));
        }
        return customer;
    }

    public Customer logIn(HttpServletRequest request,
                      HttpServletResponse response,
                      LogInView logInView) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(logInView.getLogin(), logInView.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        rememberMeServices.onLoginSuccess(request, response, authentication);
        return (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public void register(UserView registrationData) throws UserAlreadyExistException, PasswordDoestMatchException {
        String login = registrationData.getLogin();
        Customer customerFromDB = customerRepository.findByUsername(login);
        if (customerFromDB != null) {
            throw new UserAlreadyExistException(String.format("Client with login %s already exist", login));
        }
        if (!Objects.equals(registrationData.getPassword(), registrationData.getPasswordConfirm())) {
            throw new PasswordDoestMatchException("Passwords doesn't match!");
        }
        customerRepository.save(getCustomer(registrationData));
    }

    private Customer getCustomer(UserView userView) {
        Customer customer = new Customer();

        customer.setPassword(passwordEncoder.encode(userView.getPassword()));
        customer.setAddress(userView.getAddress());
        customer.setEmail(userView.getEmail());
        customer.setFirstName(userView.getFirstName());
        customer.setLastName(userView.getFirstName());
        customer.setUsername(userView.getLogin());
        customer.setRoles(Collections.singleton(roleRepository.findByName(EUserRole.ROLE_CONSUMER)));

        return customer;
    }
}
