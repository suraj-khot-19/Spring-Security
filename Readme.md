# Spring Security

## Table of Contents
1. [Default Behavior of Spring Security](#1-default-behavior-of-spring-security)
2. [Setting Up Spring Security Configuration](#2-setting-up-spring-security-configuration)
3. [Changing Default Login Method](#3-changing-default-login-method)
4. [User Details Manager](#4-user-details-manager)
5. [Role Based Access](#5-role-based-access)

---

## 1. Default Behavior of Spring Security
By default, Spring Security adds form-based login features to an application with:
- **Username:** `user`
- **Password:** Generated and displayed in the debug console.

---
## 2. Setting Up Spring Security Configuration
To configure a default user, you can set the following properties in `application.properties`:

```properties
#default user
spring.security.user.name=rowdy

#default creates everytime new when restart server
spring.security.user.password=rowdy123
```

___

## 3. Changing Default Login Method
You can override the default login method by defining a custom security configuration:
```java
package com.spring_security_1.spring_security_1.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration /// this tells spring it is config class
public class SecurityConfig {
    
    @Bean /// it will create bean for it
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)requests.anyRequest()).authenticated());

        /// this will make application which won't store cookies(STATELESS)
        /// otherwise it always store "JSESSIONID" in browser storage
        http.sessionManagement(session
                ->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        /// form based login
        //http.formLogin(Customizer.withDefaults());

        /// basic alert based login
        http.httpBasic(Customizer.withDefaults());
        
        return (SecurityFilterChain)http.build();
    }
}
```
### Form-Based Authentication vs. Basic Authentication

| Feature                 | Form-Based Authentication      | Basic Authentication                              |
|-------------------------|--------------------------------|---------------------------------------------------|
| **Login Interface**     | HTML login form                | Browser popup or API header                       |
| **Session Management**  | Uses `JSESSIONID`              | Stateless (no session)                            |
| **Best for**            | Web applications               | REST APIs                                         |
| **Security Level**      | High (if HTTPS is used)        | Secure, but credentials are sent on every request |
| **User Experience**     | User-friendly                  | Less user-friendly                                |
| **Logout Feature**      | Yes (supports logout endpoint) | No (requires client-side handling)                |
___

## 4. User Details Manager
Creating user detail manager with the help of InMemoryUserDetailsManager class 
```java
@Configuration
public class SecurityConfig {
    
/// in memory user details manager for login with role and password
@Bean
public UserDetailsService userDetailsService() {
        /*
        {noop} tells Spring Security not to encode the password.

        UserDetails user1 = User.withUsername("user1").password("{noop}password1").roles("USER").build();
        UserDetails admin = User.withUsername("admin").password("{noop}adminpass").roles("ADMIN").build();
        
        */

        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder(); /// encoder
    
        UserDetails user1 = User.withUsername("user1").password(encoder.encode("password1")).roles("USER").build();
        UserDetails admin = User.withUsername("admin").password(encoder.encode("adminpass")).roles("ADMIN").build();
    
        return new InMemoryUserDetailsManager(user1, admin);
    }
}
```
___

## 5. Role Based Access
@PreAuthorize("hasRole('ROLE')"), @Configuration and @EnableMethodSecurity these annotations helps us for role based access
1. Spring Configuration
    ```java
    @Configuration ///  tells spring it has some configuration
    @EnableMethodSecurity(prePostEnabled = true) ///  tells spring it has allowed method security(for role-based)
    public class SecurityConfig {
   
        /// in memory user details manager for login with role and password
        @Bean
        public UserDetailsService userDetailsService() {
            /// password encoder
            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    
            UserDetails user1 = User.withUsername("user1").password(encoder.encode("password1")).roles("USER").build();
            UserDetails admin = User.withUsername("admin").password(encoder.encode("adminpass")).roles("ADMIN").build();
    
            return new InMemoryUserDetailsManager(user1, admin);
        }
    }
    ```
2. Controller
    ```java
   @RestController
   public class HelloController {

    /// anyone can access
    @GetMapping("/hello")
    public String greet() {
        return "Hello!";
    }

    /// only access for user
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public String helloUser(){
        return "Hello User!";
    }

    /// only access for admin
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String helloAdmin(){
        return "Hello Admin!";
    }
   }
    ```
3. output for unauthorized access
    ```json
    {
    "timestamp": "2025-02-19T05:19:44.416+00:00",
    "status": 403,
    "error": "Forbidden",
    "path": "/admin"
    }
    ```
4. Key Features of Role-Based Access Control

| Feature                                            | Description                                                  |
|----------------------------------------------------|--------------------------------------------------------------|
| **`@PreAuthorize("hasRole('ROLE')")`**             | Restricts access to methods based on user roles.             |
| **`@Configuration`**                               | Marks the class as a Spring configuration.                   |
| **`@EnableMethodSecurity(prePostEnabled = true)`** | Enables method-level security for role-based access control. |
| **In-Memory Authentication**                       | Users and roles are stored in memory for testing purposes.   |
| **Password Encoding**                              | Uses a secure password encoder to store passwords safely.    |
