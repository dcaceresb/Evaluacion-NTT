package com.dcaceresb.ntt_test.auth.dto;

import com.dcaceresb.ntt_test.phone.dto.CreatePhoneDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RegisterDto {
    @NotNull(message = "Email must provided")
    private String email;

    @NotNull(message = "Password must provided")
    private String password;

    private List<CreatePhoneDto> phones;
}
