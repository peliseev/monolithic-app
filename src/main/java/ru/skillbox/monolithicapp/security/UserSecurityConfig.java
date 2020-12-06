package ru.skillbox.monolithicapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import ru.skillbox.monolithicapp.model.EUserRole;
import ru.skillbox.monolithicapp.service.UserService;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class UserSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService userService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            String path = request.getRequestURI();
            if (path.startsWith("/api/user/password/change") ||
                path.startsWith("/login") || path.startsWith("/api/user/login") ||
                path.startsWith("/register") || path.startsWith("/api/user/registration")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            } else {
                response.sendRedirect("/login.html");
            }
        };
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public RememberMeAuthenticationFilter rememberMeAuthenticationFilter() throws Exception {
        return new RememberMeAuthenticationFilter(authenticationManagerBean(), rememberMeServices());
    }

    @Bean
    public TokenBasedRememberMeServices rememberMeServices() {
        return new TokenBasedRememberMeServices("user-token", userService);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint()).and()
                .authorizeRequests()

                .antMatchers("/users**", "/registrationByAdmin**", "/api/user/all", "api/user/role/all", "/api/user/\\d+/delete",
                        "/api/user/\\d+/password/reset", "api/user/registerByAdmin")
                .hasAnyAuthority(EUserRole.ROLE_ADMIN.name())

                .antMatchers("/shop**", "/api/order**").hasAnyAuthority(EUserRole.ROLE_CUSTOMER.name())
                .antMatchers("/warehouse**", "/api/warehouse**").hasAnyAuthority(EUserRole.ROLE_SUPPLIER.name())
                .antMatchers("/delivery**", "/api/delivery**").hasAnyAuthority(EUserRole.ROLE_DELIVER.name())

                .antMatchers("/login**", "/api/user/login**", "/api/user/password/change").permitAll()
                .antMatchers("/registration**", "/api/user/register").permitAll()
                .antMatchers("/h2-console**").permitAll()

                .anyRequest()
                .authenticated()
                .and()
                .addFilterBefore(rememberMeAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .headers().frameOptions().disable()
                .and()
                .logout().deleteCookies("JSESSIONID")
                .and()
                .exceptionHandling().accessDeniedPage("/accessDenied.html");
    }
}
