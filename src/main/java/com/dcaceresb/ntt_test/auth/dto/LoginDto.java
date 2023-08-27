package com.dcaceresb.ntt_test.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class LoginDto implements Serializable {
    @JsonProperty("email")
    @Email(message = "Must be a valid email")
    @NotNull(message = "Email must provided")
    private String email;

    @JsonProperty("password")
    @NotNull(message = "Password must provided")
    @Pattern(regexp = "[A-Z][a-z]*[0-9]{2}", message = "The password must have one uppercase, many lowercase and exact 2 digits")
    private String password;
}
