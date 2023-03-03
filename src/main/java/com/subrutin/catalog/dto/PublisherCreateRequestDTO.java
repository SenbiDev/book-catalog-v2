package com.subrutin.catalog.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import com.subrutin.catalog.annotation.LogThisArg;
import lombok.Data;

@LogThisArg
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PublisherCreateRequestDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6264675048939616940L;

	@NotBlank(message = "publisher.must.not.blank")
	private String publisherName;
	
	@NotBlank(message = "company_name.must.not.blank")
	private String companyName;
	
	private String address;

}
