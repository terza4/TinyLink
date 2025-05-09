package com.example.tinylink.service;

import com.example.tinylink.entity.UrlMapping;
import com.example.tinylink.repository.UrlMappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {

    private final UrlMappingRepository urlMappingRepository;

    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int SHORTCODE_LENGTH = 6;
    private final Random random = new Random();

    public String shortenUrl(String longUrl) {
        if (longUrl == null || longUrl.isBlank()) {
            throw new IllegalArgumentException("URL ne smije biti prazan.");
        }

        Optional<UrlMapping> existing = urlMappingRepository.findByLongUrl(longUrl);
        if (existing.isPresent()) {
            return existing.get().getShortCode();
        }

        String shortCode;
        do {
            shortCode = generateRandomCode();
        } while (urlMappingRepository.findByShortCode(shortCode).isPresent());

        UrlMapping newMapping = UrlMapping.builder()
                .longUrl(longUrl)
                .shortCode(shortCode)
                .creationDate(LocalDateTime.now())
                .build();

        urlMappingRepository.save(newMapping);

        return shortCode;
    }

    public String getLongUrl(String shortCode) {
        return urlMappingRepository.findByShortCode(shortCode)
                .map(UrlMapping::getLongUrl)
                .orElseThrow(() -> new RuntimeException("Not found"));
    }

    private String generateRandomCode() {
        StringBuilder code = new StringBuilder(SHORTCODE_LENGTH);
        for (int i = 0; i < SHORTCODE_LENGTH; i++) {
            int index = random.nextInt(ALPHABET.length());
            code.append(ALPHABET.charAt(index));
        }
        return code.toString();
    }
}

