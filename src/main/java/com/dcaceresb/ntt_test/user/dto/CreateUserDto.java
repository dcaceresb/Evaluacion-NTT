package com.dcaceresb.ntt_test.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreateUserDto {
    @JsonProperty("nombre")
    @NotNull
    private String name;

    @JsonProperty("correo")
    @NotNull
    private String email;

    @JsonProperty("contraseña")
    @NotNull
    private String password;

    @JsonProperty("telefonos")
    private List<PhoneDto> phones;
}
