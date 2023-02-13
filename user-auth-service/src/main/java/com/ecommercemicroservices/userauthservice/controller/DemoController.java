package com.ecommercemicroservices.userauthservice.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/demo")
@Slf4j
public class DemoController {

    @GetMapping("/getRoles")
    public List<String> GetRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<String> authorities = authentication.getAuthorities().stream().map(a -> a.getAuthority()).toList();
        log.error("Authorities:  " + authorities);
        return authorities;
    }

    @GetMapping("/withAuthority")
    @PreAuthorize("hasAuthority('ROLE_MANAGER_WRITE')")
    public String GetHello1() {
        return "hello world";
    }

    @GetMapping("/withoutAuthority")
    public String GetHello2() {
        return "hello world";
    }
}
