package com.subrutin.catalog.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AddressCreateRequestDTO implements Serializable {

    private static final long serialVersionUID = 8122060456683784892L;

    private String streetName;

    private String cityName;

    private String zipCode;
}
