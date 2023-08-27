package com.dcaceresb.ntt_test.phone.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
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
