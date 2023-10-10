package com.dcaceresb.ntt_test.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
public class UserDto implements Serializable {

    @JsonProperty("id")
    private String id;

    @JsonProperty("creado")
    private Date createdAt;

    @JsonProperty("modificado")
    private Date updatedAt;

    @JsonProperty("ultimoLogin")
    private Date lastLogin;

    @JsonProperty("token")
    private String token;

    @JsonProperty("activo")
    private Boolean isActive;
}
