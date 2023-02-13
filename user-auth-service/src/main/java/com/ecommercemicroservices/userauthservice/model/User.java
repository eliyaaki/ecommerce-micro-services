package com.ecommercemicroservices.userauthservice.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_users")
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private Integer id;
    @NotBlank(message = "First-name is a required field")
    private String firstName;
    @NotBlank(message = "last-name is a required field")
    private String lastName;
    @NotBlank(message = "password is a required field")
    private String password;
    @NotBlank(message = "email is a required field")
    private String email;

    private LocalDate lastConnectivity;

    private Boolean isOnline;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<GrantedAuthority> authorities;
    @Enumerated(EnumType.STRING)
    private Role role;


    public void setAuthorities(final Set<GrantedAuthority> authorities) {
        this.authorities = authorities.stream().collect(Collectors.toSet());
    }

    @Override
    public Set<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
