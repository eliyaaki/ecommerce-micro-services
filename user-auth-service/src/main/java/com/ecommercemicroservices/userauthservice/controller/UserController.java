package com.ecommercemicroservices.userauthservice.controller;

import com.ecommercemicroservices.userauthservice.dto.AuthenticationRequest;
import com.ecommercemicroservices.userauthservice.dto.AuthenticationResponse;
import com.ecommercemicroservices.userauthservice.dto.RegistrationRequest;
import com.ecommercemicroservices.userauthservice.model.User;
import com.ecommercemicroservices.userauthservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {


    private final UserService userService;


    @GetMapping("/getRoles")
    public List<String> GetRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<String> authorities = authentication.getAuthorities().stream().map(a -> a.getAuthority()).toList();
        log.error("Authorities:  " + authorities);
        return authorities;
    }

    @GetMapping("/GetUserByEmail/{email}")
    public User GetUserByEmail(@PathVariable @Valid String email) {
        return userService.getUserByEmail(email);
    }

    @GetMapping("/getUsers")
    public List<User> GetUsers() {
        return userService.getUsers();
    }


    @PostMapping("/registration")
    public AuthenticationResponse registration(@RequestBody @Valid RegistrationRequest req) {

        return userService.userRegistration(req);
    }

    @PostMapping("/authentication")
    public AuthenticationResponse authentication(@RequestBody @Valid AuthenticationRequest req) {
        return userService.userAuthentication(req);
    }
}
