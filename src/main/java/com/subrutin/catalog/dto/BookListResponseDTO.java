package com.subrutin.catalog.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BookListResponseDTO implements Serializable {

    private static final long serialVersionUID = 7498620951941758793L;

    private String id;

    private String title;

    private String direction;

    private String description;

    private String publisherName;

    private List<String> categoryCodes;

    private List<String> authorName;
}
