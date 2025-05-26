package com.example.tinylink.dto.LinkDTO;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
public class ShortenRequest {

    @URL(message = "Bad URL!")
    private String url;

    @Pattern(regexp = "^[a-zA-Z0-9]{4,10}$", message = "Short code mora imati 4-10 slova ili brojeva")
    private String shortCodee;
}