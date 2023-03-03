package com.subrutin.catalog.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
public class UserDetailResponseDTO implements Serializable {

    private static final long serialVersionUID = 8840788153639346501L;

    private String username;
}
