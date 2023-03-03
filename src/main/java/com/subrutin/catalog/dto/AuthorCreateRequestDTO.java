package com.subrutin.catalog.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import com.subrutin.catalog.validator.annotation.ValidAuthorName;
import lombok.Data;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class AuthorCreateRequestDTO {

	@ValidAuthorName
	@NotBlank
	private String authorName;
	
	@NotNull
	private Long birthDate;

	@NotEmpty
	private List<AddressCreateRequestDTO> addresses;
}
