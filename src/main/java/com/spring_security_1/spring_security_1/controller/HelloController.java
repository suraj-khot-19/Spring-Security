package com.spring_security_1.spring_security_1.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
