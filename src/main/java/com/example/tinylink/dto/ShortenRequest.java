package com.example.tinylink.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShortenRequest {

    @NotBlank(message = "URL ne smije biti prazan.")
    private String url;
}