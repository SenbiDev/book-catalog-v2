package com.subrutin.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@AllArgsConstructor
@Data
public class CategoryQueryDTO implements Serializable {
    private static final long serialVersionUID = -3088780791059647317L;

    private Long bookId;

    private String categoryCode;
}
