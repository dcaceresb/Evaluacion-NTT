package com.dcaceresb.ntt_test.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class CreateUserDto {
    @JsonProperty("nombre")
    private String name;

    @JsonProperty("correo")
    private String email;

    @JsonProperty("contraseña")
    private String password;

    @JsonProperty("telefonos")
    private List<PhoneDto> phones;
}
