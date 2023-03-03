package com.subrutin.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@AllArgsConstructor
@Data
public class BookQueryDTO implements Serializable {

    private static final long serialVersionUID = 6307169583788939793L;

    private Long id; // id internal

    private String bookId; // secureId

    private String bookTitle;

    private String publisherName;

    private String description;
}
