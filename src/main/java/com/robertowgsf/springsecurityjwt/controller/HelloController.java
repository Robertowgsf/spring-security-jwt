package com.robertowgsf.springsecurityjwt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping
    public String authenticated() {
        return "Hello any user authenticated";
    }

    @GetMapping("user")
    public String user() {
        return "Hello user";
    }

    @GetMapping("admin")
    public String admin() {
        return "Hello admin";
    }

    @GetMapping("user-admin")
    public String userOrAdmin() {
        return "Hello user or admin";
    }
}
