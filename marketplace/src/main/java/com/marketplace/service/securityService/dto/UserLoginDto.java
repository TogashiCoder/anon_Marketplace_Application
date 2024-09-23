package com.marketplace.service.securityService.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.marketplace.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserLoginDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "Username is required")
    @Size(min = 1, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Password is required")
    @Size(min = 1, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
}
