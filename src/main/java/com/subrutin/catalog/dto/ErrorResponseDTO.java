package com.subrutin.catalog.dto;

import com.subrutin.catalog.enums.ErrorCode;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class ErrorResponseDTO implements Serializable {

    private static final long serialVersionUID= -1225569517281267486L;

    private Date timestamps;

    private String message;

    private ErrorCode errorCode;

    private List<String> details;

    private HttpStatus status;

    public static ErrorResponseDTO of(final String message, ErrorCode errorCode, List<String> details, HttpStatus status) {
        return new ErrorResponseDTO(message, errorCode, details, status);
    }

    private ErrorResponseDTO(String message, ErrorCode errorCode, List<String> details, HttpStatus status) {
        this.message = message;
        this.errorCode = errorCode;
        this.details = details;
        this.status = status;
        this.timestamps = new Date();
    }
}
