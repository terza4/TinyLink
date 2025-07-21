package com.example.tinylink.dto.LinkDTO;

import com.example.tinylink.entity.UrlMapping;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HistoryResponse {
    private String shortCode;
    private long clickCount;

}
