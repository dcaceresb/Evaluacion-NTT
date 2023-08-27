package com.dcaceresb.ntt_test.phone.dto;


import com.dcaceresb.ntt_test.phone.PhoneEntity;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring"
)
public interface PhoneMapper {
    PhoneEntity toEntity(PhoneDto dto);
    PhoneDto toDto(PhoneEntity entity);
}
