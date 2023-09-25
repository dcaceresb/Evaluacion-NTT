package com.dcaceresb.ntt_test.user.dto;

import com.dcaceresb.ntt_test.phone.dto.PhoneDto;
import com.dcaceresb.ntt_test.user.UserEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
public class UserDto implements Serializable {
    @JsonProperty("usuario")
    private UserEntity user;

    @JsonProperty("id")
    private String id;

    @JsonProperty("created")
    private Date createdAt;

    @JsonProperty("modified")
    private Date updatedAt;

    @JsonProperty("last_login")
    private Date lastLogin;

    @JsonProperty("token")
    private String token;

    @JsonProperty("isactive")
    private Boolean isActive;

    @JsonProperty("phones")
    private PhoneDto[] phones;
}
