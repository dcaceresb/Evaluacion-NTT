package com.dcaceresb.ntt_test.common.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponseDto {
    private String message;
    private Integer status;
}
