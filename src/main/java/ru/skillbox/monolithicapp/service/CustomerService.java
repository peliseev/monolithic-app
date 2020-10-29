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
import ru.skillbox.monolithicapp.model.LogInView;
import ru.skillbox.monolithicapp.repository.CustomerRepository;
import ru.skillbox.monolithicapp.repository.RoleRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Objects;

@Service("customerService")
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

    public boolean saveCustomer(Customer customer) {
        Customer customerFromBD = customerRepository.findByUsername(customer.getUsername());
        if (customerFromBD != null) {
            return false;
        }
        if (!Objects.equals(customer.getPassword(), customer.getPasswordConfirm())) {
            return false;
        }
        Role userRole = roleRepository.findByName("ROLE_USER");
        customer.setRoles(Collections.singleton(userRole));
        customer.setPassword(bcPasswordEncoder.encode(customer.getPassword()));
        customerRepository.save(customer);

        return true;
    }

    public void logIn(HttpServletRequest request, HttpServletResponse response, LogInView logInView) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(logInView.getLogin(), logInView.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        rememberMeServices.onLoginSuccess(request, response, authentication);
    }
}
