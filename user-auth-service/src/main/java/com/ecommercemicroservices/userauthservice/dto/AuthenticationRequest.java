package com.ecommercemicroservices.userauthservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AuthenticationRequest(
        @NotBlank(message = "First-name is a required field") String email,
        @NotBlank(message = "First-name is a required field") String password
) {
}
