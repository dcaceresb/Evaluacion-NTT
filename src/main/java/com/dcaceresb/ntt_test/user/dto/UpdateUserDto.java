package com.dcaceresb.ntt_test.user.dto;

import com.dcaceresb.ntt_test.phone.dto.PhoneDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class UpdateUserDto {
    @Email(message = "Must be a valid email")
    private String email;
    @Pattern(regexp = "[A-Z)][a-z]*[0-9]{2}", message = "The password must have one uppercase, many lowercases and exact 2 digits")
    private String password;

    private List<@Valid PhoneDto> phones;
}
