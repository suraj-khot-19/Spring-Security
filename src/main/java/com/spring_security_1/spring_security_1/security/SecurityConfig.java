package com.spring_security_1.spring_security_1.security;

import org.apache.tomcat.util.buf.UEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl) requests.anyRequest()).authenticated());

        /// this will make application which won't store cookies(STATELESS)
        /// otherwise it always store "JSESSIONID" in browser storage
        http.sessionManagement(session
                -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        /// form based login
        //http.formLogin(Customizer.withDefaults());

        /// basic alert based login
        http.httpBasic(Customizer.withDefaults());
        return (SecurityFilterChain) http.build();
    }

    /// in memory user details manager for login with role and password
    @Bean
    public UserDetailsService userDetailsService() {
        /*
        {noop} tells Spring Security not to encode the password.

        UserDetails user1 = User.withUsername("user1").password("{noop}password1").roles("USER").build();
        UserDetails admin = User.withUsername("admin").password("{noop}adminpass").roles("ADMIN").build();
        */

        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        UserDetails user1 = User.withUsername("user1").password(encoder.encode("password1")).roles("USER").build();
        UserDetails admin = User.withUsername("admin").password(encoder.encode("adminpass")).roles("ADMIN").build();

        return new InMemoryUserDetailsManager(user1, admin);
    }
}
