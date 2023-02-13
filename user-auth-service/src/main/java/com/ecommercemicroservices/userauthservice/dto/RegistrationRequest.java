package com.ecommercemicroservices.userauthservice.dto;

import com.ecommercemicroservices.userauthservice.model.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record RegistrationRequest(
        @NotBlank(message = "First-name is a required field") String firstName,
        @NotBlank(message = "First-name is a required field") String lastName,
        @NotBlank(message = "First-name is a required field") String email,
        @NotBlank(message = "First-name is a required field") String password,
        Role role
) {
}
