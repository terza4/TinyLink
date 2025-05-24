package com.example.tinylink.dto.StatsDTO;

import com.example.tinylink.entity.UrlMapping;

public class StatsMapper {
    public static StatsDTO statsDto(UrlMapping urlMapping){
        StatsDTO dto = new StatsDTO();
        dto.setLongUrl(urlMapping.getLongUrl());
        dto.setShortUrl(urlMapping.getShortCode());
        dto.setCreationDate(urlMapping.getCreationDate());
        dto.setClickCount(urlMapping.getClickCount());
        dto.setLastAccessed(urlMapping.getLastAccessed());
        return dto;
    }
}
