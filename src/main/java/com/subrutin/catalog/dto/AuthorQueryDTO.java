package com.subrutin.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@AllArgsConstructor
@Data
public class AuthorQueryDTO implements Serializable {

    private static final long serialVersionUID = -8490152208980968835L;

    private Long bookId;

    private String authorName;
}
