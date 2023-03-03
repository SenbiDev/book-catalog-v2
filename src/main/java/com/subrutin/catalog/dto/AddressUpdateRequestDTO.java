package com.subrutin.catalog.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AddressUpdateRequestDTO implements Serializable {

    private static final long serialVersionUID = 7029737959226398691L;

    private Long addressId;

    private String streetName;

    private String cityName;

    private String zipCode;
}
