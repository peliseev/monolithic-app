package ru.skillbox.monolithicapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import ru.skillbox.monolithicapp.model.EUserRole;
import ru.skillbox.monolithicapp.service.CustomerService;

import javax.servlet.http.HttpServletResponse;

@Configuration
@Order(2)
public class ConsumerSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationEntryPoint customerAuthEntryPoint;

    @Bean
    public AuthenticationEntryPoint customerAuthEntryPoint() {
        return (request, response, authException) -> {
            String path = request.getRequestURI();
            if (path.startsWith("/login") || path.startsWith("/api/login") ||
                path.startsWith("/register") || path.startsWith("/api/registration")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            }
            request.getRequestDispatcher("/login.html").forward(request, response);
        };
    }

    @Bean(name = "consumerAuthenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean(name = "consumerAuthenticationFilter")
    public RememberMeAuthenticationFilter rememberMeAuthenticationFilter() throws Exception {
        return new RememberMeAuthenticationFilter(authenticationManagerBean(), rememberMeServices());
    }

    @Bean(name = "rememberCustomerServices")
    public TokenBasedRememberMeServices rememberMeServices() {
        return new TokenBasedRememberMeServices("customer-token", customerService);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customerService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(customerAuthEntryPoint).and()
                .authorizeRequests()
                .antMatchers("/shop**").hasAnyAuthority(EUserRole.ROLE_CONSUMER.name(), EUserRole.ROLE_ADMIN.name())
                .antMatchers("/api/order**").hasAnyAuthority(EUserRole.ROLE_CONSUMER.name(), EUserRole.ROLE_ADMIN.name())
                .antMatchers("/warehouse**").hasAnyAuthority(EUserRole.ROLE_SUPPLIER.name(), EUserRole.ROLE_ADMIN.name())
                .antMatchers("/api/warehouse**").hasAnyAuthority(EUserRole.ROLE_SUPPLIER.name(), EUserRole.ROLE_ADMIN.name())
                .antMatchers("/delivery**").hasAnyAuthority(EUserRole.ROLE_DELIVERY.name(), EUserRole.ROLE_ADMIN.name())
                .antMatchers("/api/delivery**").hasAnyAuthority(EUserRole.ROLE_DELIVERY.name(), EUserRole.ROLE_ADMIN.name())
                .antMatchers("/login**", "/api/login").permitAll()
                .antMatchers("/registration**", "/api/register").permitAll()
                .antMatchers("/api/roles").permitAll()
                .antMatchers("/h2-console**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .addFilterBefore(rememberMeAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .headers().frameOptions().disable()
                .and()
                .logout().deleteCookies("JSESSIONID")
                .and()
                .exceptionHandling().accessDeniedPage("/accessDenied.html")
                .and();
    }
}
