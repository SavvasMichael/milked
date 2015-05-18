package org.savvas.milked;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.rcp.RemoteAuthenticationException;
import org.springframework.security.authentication.rcp.RemoteAuthenticationManager;
import org.springframework.security.authentication.rcp.RemoteAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebMvcSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    RestTemplate rest = new RestTemplate();

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/css/*", "/js/**", "/img/*", "/registration", "/activation/*", "/user/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        RemoteAuthenticationProvider authenticationProvider = new RemoteAuthenticationProvider();
        authenticationProvider.setRemoteAuthenticationManager(new RemoteAuthenticationManager() {
            @Override
            public Collection<GrantedAuthority> attemptAuthentication(String email, String password) throws RemoteAuthenticationException {
                ArrayList grants = new ArrayList();
                String loginUrl = "http://localhost:8080/login";
                Map loginRequest = new HashMap();
                loginRequest.put("email", email);
                loginRequest.put("password", password);
                ResponseEntity<User> user = rest.postForEntity(URI.create(loginUrl), loginRequest, User.class);
                User fetchedUser = user.getBody();
                HttpStatus status = user.getStatusCode();
                if (status.is2xxSuccessful()) {
                    grants.add(new SimpleGrantedAuthority(fetchedUser.getId() + ""));
                    return grants;
                }
                throw new BadCredentialsException("failed to authenticate user.");
            }
        });
        auth
                .authenticationProvider(authenticationProvider);
    }

    private static class User {
        private Long id;
        private String email;
        private String password;

        public User() {
        }
        public User(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public Long getId() {
            return id;
        }
    }
}