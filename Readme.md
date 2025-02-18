# Spring Security

## Table of Contents
1. [Default Behavior of Spring Security](#default-behavior-of-spring-security)
2. [Setting Up Spring Security Configuration](#setting-up-spring-security-configuration)
3. [Changing Default Login Method](#changing-default-login-method)

---

## Default Behavior of Spring Security
By default, Spring Security adds form-based login features to an application with:
- **Username:** `user`
- **Password:** Generated and displayed in the debug console.

---
## Setting Up Spring Security Configuration
To configure a default user, you can set the following properties in `application.properties`:

```properties
#default user
spring.security.user.name=rowdy

#default creates everytime new when restart server
spring.security.user.password=rowdy123
```

___

## Changing Default Login Method
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