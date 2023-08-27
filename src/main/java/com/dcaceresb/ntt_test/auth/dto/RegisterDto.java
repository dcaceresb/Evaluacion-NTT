package com.dcaceresb.ntt_test.auth.dto;

import com.dcaceresb.ntt_test.phone.dto.CreatePhoneDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterDto {
    @Email(message = "Must be a valid email")
    @NotNull(message = "Email must provided")
    private String email;


    @Pattern(regexp = "[A-Z)][a-z]*[0-9]{2}", message = "The password must have one uppercase, many lowercases and exact 2 digits")
    @NotNull(message = "Password must provided")
    private String password;

    private CreatePhoneDto[] phones;
}
