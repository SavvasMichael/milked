package org.savvas.milked;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
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
        http
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        RemoteAuthenticationProvider authenticationProvider = new RemoteAuthenticationProvider();
        authenticationProvider.setRemoteAuthenticationManager(new RemoteAuthenticationManager() {
            @Override
            public Collection<GrantedAuthority> attemptAuthentication(String email, String password) throws RemoteAuthenticationException {
                ArrayList grants = new ArrayList();
                String baseUrl = "http://localhost:8080";
                String userUrl = "/user/1";

                User user = rest.getForEntity(URI.create(baseUrl + userUrl), User.class).getBody();
                if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                    grants.add(new SimpleGrantedAuthority("USER"));
                    return grants;
                }
                throw new BadCredentialsException("failed to authenticate user.");
            }
        });
        auth
                .authenticationProvider(authenticationProvider);
//                .inMemoryAuthentication()
//                .withUser("user").password("password").roles("USER");
    }

    private static class User {
        private String email;
        private String password;

        public User() {
        }

        public User(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }
    }
}