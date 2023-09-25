package com.dcaceresb.ntt_test.phone.dto;

import com.dcaceresb.ntt_test.phone.PhoneEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PhoneMapper {
    PhoneMapper INSTANCE = Mappers.getMapper(PhoneMapper.class);

    PhoneEntity toEntity(PhoneDto dto);
    PhoneDto toDto(PhoneEntity entity);
}
