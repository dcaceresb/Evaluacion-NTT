package com.dcaceresb.ntt_test.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class PhoneDto implements Serializable {
    @JsonProperty("numero")
    private String number;

    @JsonProperty("codigoCiudad")
    private String cityCode;

    @JsonProperty("codigoPais")
    private String countryCode;

    @Override
    public String toString() {
        return "PhoneDto{" +
                "number='" + number + "'" +
                ", cityCode='" + cityCode + "'" +
                ", countryCode='"+ countryCode+"'"+
                '}';
    }
}
