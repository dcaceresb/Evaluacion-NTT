package com.dcaceresb.ntt_test.phone.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PhoneDto {
    private Integer id;

    @NotNull(message = "number is required")
    private String number;

    @JsonProperty("citycode")
    @NotNull(message = "citycode is required")
    private String cityCode;

    @JsonProperty("contrycode")
    @NotNull(message = "contrycode is required")
    private String countryCode;
}
