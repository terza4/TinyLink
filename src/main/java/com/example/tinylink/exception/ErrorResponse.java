package com.example.tinylink.exception;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {
    private String message;
    private int httpCode;
    private String errorCode;
    private List<String> errors;

}

