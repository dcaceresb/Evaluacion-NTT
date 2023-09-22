package com.dcaceresb.ntt_test.user.dto;

import com.dcaceresb.ntt_test.phone.dto.CreatePhoneDto;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class CreateUserDto {
    private String email;
    private String token;
    private String password;
    private Date lastLogin;
    private List<CreatePhoneDto> phones;
}
