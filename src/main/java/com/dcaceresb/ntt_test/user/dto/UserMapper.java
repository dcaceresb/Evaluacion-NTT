package com.dcaceresb.ntt_test.user.dto;

import com.dcaceresb.ntt_test.auth.dto.RegisterDto;
import com.dcaceresb.ntt_test.phone.dto.PhoneMapper;
import com.dcaceresb.ntt_test.user.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {
                PhoneMapper.class
        }
)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserDto toDto(UserEntity user);


    UserEntity toEntity(UserDto entity);


    UserEntity registerToEntity(RegisterDto dto);
}
