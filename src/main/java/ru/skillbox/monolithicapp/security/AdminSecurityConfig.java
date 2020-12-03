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
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import ru.skillbox.monolithicapp.service.AdminService;

import javax.servlet.http.HttpServletResponse;

@Configuration
@Order(1)
public class AdminSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private AdminService adminService;

    @Autowired
    private AuthenticationEntryPoint adminAuthEntryPoint;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public AuthenticationEntryPoint adminAuthEntryPoint() {
        return (request, response, authException) -> {
            String path = request.getRequestURI();
            if (path.startsWith("/admin/login") || path.startsWith("/api/admin/login") ||
                path.startsWith("/admin/register") || path.startsWith("/api/admin/registration")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            }
            request.getRequestDispatcher("/admin/login.html").forward(request, response);
        };
    }

    @Bean(name = "adminAuthenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean(name = "adminAuthenticationFilter")
    public RememberMeAuthenticationFilter rememberMeAuthenticationFilter() throws Exception {
        return new RememberMeAuthenticationFilter(authenticationManagerBean(), tokenBasedRememberAdminServices());
    }

    @Bean(name = "rememberAdminServices")
    public TokenBasedRememberMeServices tokenBasedRememberAdminServices() {
        return new TokenBasedRememberMeServices("admin-token", adminService);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(adminService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .regexMatcher("(/api)?/admin/.*")
                .cors().and().csrf().disable()
                .authorizeRequests().anyRequest().hasRole("ADMIN")
                .and()
                .exceptionHandling().authenticationEntryPoint(adminAuthEntryPoint)
                .and()
                .headers().frameOptions().disable()
                .and()
                .logout().deleteCookies("JSESSIONID")
                .and()
                .exceptionHandling().accessDeniedPage("/accessDenied.html");
    }

}
