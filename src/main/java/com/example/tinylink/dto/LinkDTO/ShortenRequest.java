package com.example.tinylink.dto.LinkDTO;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
public class ShortenRequest {

    @URL(message = "Bad URL!")
    private String url;
}