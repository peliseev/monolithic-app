package ru.skillbox.monolithicapp.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.stereotype.Service;
import ru.skillbox.monolithicapp.entity.Customer;
import ru.skillbox.monolithicapp.entity.security.Role;
import ru.skillbox.monolithicapp.exception.CustomerAlreadyExistException;
import ru.skillbox.monolithicapp.exception.PasswordDoestMatchException;
import ru.skillbox.monolithicapp.model.CustomerView;
import ru.skillbox.monolithicapp.model.ERole;
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
    private final BCryptPasswordEncoder bcPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenBasedRememberMeServices rememberMeServices;

    public CustomerService(CustomerRepository customerRepository,
                           RoleRepository roleRepository,
                           BCryptPasswordEncoder bcPasswordEncoder,
                           AuthenticationManager authenticationManager,
                           @Lazy TokenBasedRememberMeServices rememberMeServices) {
        this.customerRepository = customerRepository;
        this.roleRepository = roleRepository;
        this.bcPasswordEncoder = bcPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.rememberMeServices = rememberMeServices;
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

    public void register(CustomerView registrationData) throws CustomerAlreadyExistException, PasswordDoestMatchException {
        String login = registrationData.getLogin();
        Customer customerFromDB = customerRepository.findByUsername(login);
        if (customerFromDB != null) {
            throw new CustomerAlreadyExistException(String.format("Client with login %s already exist", login));
        }
        if (!Objects.equals(registrationData.getPassword(), registrationData.getPasswordConfirm())) {
            throw new PasswordDoestMatchException("Passwords doesn't match!");
        }
        Role role = roleRepository.findByName(ERole.ROLE_USER);

        Customer customerToSave = fromViewToCustomer(registrationData);
        customerToSave.setRoles(Collections.singleton(role));

        customerRepository.save(customerToSave);
    }

    private Customer fromViewToCustomer(CustomerView customerView) {
        Customer customer = new Customer();

        customer.setPassword(bcPasswordEncoder.encode(customerView.getPassword()));
        customer.setAddress(customerView.getAddress());
        customer.setEmail(customerView.getEmail());
        customer.setFirstName(customerView.getFirstName());
        customer.setLastName(customerView.getFirstName());
        customer.setUsername(customerView.getLogin());

        return customer;
    }
}
