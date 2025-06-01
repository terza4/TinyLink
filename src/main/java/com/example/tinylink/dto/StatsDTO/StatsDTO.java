package com.example.tinylink.dto.StatsDTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
public class StatsDTO {
    private String longUrl;
    private String shortCode;
    private LocalDateTime creationDate;
    private long clickCount;
    private LocalDateTime lastAccessed;

}
