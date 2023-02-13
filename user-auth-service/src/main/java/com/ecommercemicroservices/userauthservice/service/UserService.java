package com.ecommercemicroservices.userauthservice.service;

import com.ecommercemicroservices.userauthservice.repository.UserRepository;
import com.ecommercemicroservices.userauthservice.dto.AuthenticationRequest;
import com.ecommercemicroservices.userauthservice.dto.AuthenticationResponse;
import com.ecommercemicroservices.userauthservice.dto.RegistrationRequest;
import com.ecommercemicroservices.userauthservice.model.Role;
import com.ecommercemicroservices.userauthservice.model.User;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder PasswordEncoder;
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;


    private Set<GrantedAuthority> defineAuthorities(Role userRole) {

        log.error("userRole:  " + userRole);
        log.error("Role.ADMIN:  " + Role.ADMIN);
        log.error("their equality:  " + userRole.equals(Role.ADMIN));
        if (userRole.equals(Role.ADMIN)) {
            log.error("is admin!!! ");
            var bs = Set.of();
            return Set.of(new SimpleGrantedAuthority("ROLE_MANAGER_READ"), new SimpleGrantedAuthority("ROLE_MANAGER_WRITE"), new SimpleGrantedAuthority("ROLE_STUDENT_WRITE"), new SimpleGrantedAuthority("ROLE_STUDENT_READ"));
        }
        log.error("is admin!!! ");
        return Set.of(new SimpleGrantedAuthority("ROLE_STUDENT_READ"));
    }

    public AuthenticationResponse userRegistration(RegistrationRequest req) {
        try {
            Set<GrantedAuthority> authorities = defineAuthorities(req.role());
            var user = User.builder()
                    .firstName(req.firstName())
                    .lastName(req.lastName())
                    .email(req.email())
                    .password(PasswordEncoder.encode(req.password()))
                    .role(req.role())
                    .authorities(authorities)
                    .build();

            log.error("user authorities after checking :  " + authorities);
//        user.setAuthorities(authorities);
            log.error("USER:  " + user);
            userRepository.save(user);
            var jwtToken = jwtService.generateToken(user.getEmail(), authorities);
            return AuthenticationResponse.builder().token(jwtToken).build();
        } catch (RuntimeException e) {
            throw new SecurityException("User not matching");
        }


    }

    public AuthenticationResponse userAuthentication(AuthenticationRequest req) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.email(), req.password()));
            var user = userRepository.findByEmail(req.email()).orElseThrow(() -> new UsernameNotFoundException("user not found"));
            var jwtToken = jwtService.generateToken(user.getEmail(), user.getAuthorities());
            return AuthenticationResponse.builder().token(jwtToken).build();
        } catch (RuntimeException e) {
            throw new SecurityException("User not matching");
        }
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUserByEmail(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
        log.error("UserDetailsServiceImpl user.getAuthorities:  " + user.getAuthorities());
        return user;
    }

}
