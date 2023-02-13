package com.ecommercemicroservices.userauthservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AuthenticationResponse(@NotBlank(message = "First-name is a required field") String token) {
}
