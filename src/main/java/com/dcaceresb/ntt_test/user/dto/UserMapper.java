package com.dcaceresb.ntt_test.user.dto;

import com.dcaceresb.ntt_test.auth.dto.RegisterDto;
import com.dcaceresb.ntt_test.phone.dto.PhoneMapper;
import com.dcaceresb.ntt_test.user.UserEntity;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        uses = {
                PhoneMapper.class
        }
)
public interface UserMapper {

    UserDto toDto(UserEntity user);


    UserEntity toEntity(UserDto entity);


    UserEntity registerToEntity(RegisterDto dto);
}
