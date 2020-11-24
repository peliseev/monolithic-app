package ru.skillbox.monolithicapp.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import ru.skillbox.monolithicapp.model.ERole;
import ru.skillbox.monolithicapp.security.AuthEntryPoint;
import ru.skillbox.monolithicapp.service.CustomerService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    CustomerService customerService;

    @Autowired
    AuthEntryPoint authEntryPoint;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public RememberMeAuthenticationFilter authenticationFilter() throws Exception {
        return new RememberMeAuthenticationFilter(authenticationManagerBean(), tokenBasedRememberMeServices());
    }

    @Bean
    public TokenBasedRememberMeServices tokenBasedRememberMeServices() {
        return new TokenBasedRememberMeServices("secret", customerService);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customerService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(authEntryPoint).and()
                .authorizeRequests()
                .antMatchers("/shop**").hasAnyAuthority(ERole.ROLE_USER.name())
                .antMatchers("/warehouse**").hasAnyAuthority(ERole.ROLE_ADMIN.name())
                .antMatchers("/login**", "/api/login").permitAll()
                .antMatchers("/registration**", "/api/register").permitAll()
                .antMatchers("/h2-console**").permitAll()
                .anyRequest()
                .authenticated();
        http
                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.headers().frameOptions().disable();
        http.exceptionHandling().accessDeniedPage("/accessDenied.html");
    }

}
