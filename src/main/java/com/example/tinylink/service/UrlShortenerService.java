package com.example.tinylink.service;

import com.example.tinylink.entity.UrlMapping;
import com.example.tinylink.repository.UserRepository;
import com.example.tinylink.entity.User;
import com.example.tinylink.repository.UrlMappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {

    private final UrlMappingRepository urlMappingRepository;
    private final UserRepository userRepository;

    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int SHORTCODE_LENGTH = 6;
    private final Random random = new Random();

    public String shortenUrl(String longUrl, String username) {
        User user = null;
        if (username != null) {
            user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen."));
        }


        if (longUrl == null || longUrl.isBlank()) {
            throw new IllegalArgumentException("URL ne smije biti prazan.");
        }


        String shortCode;
        do {
            shortCode = generateRandomCode();
        } while (urlMappingRepository.findByShortCode(shortCode).isPresent());

        UrlMapping newMapping = UrlMapping.builder()
                .longUrl(longUrl)
                .shortCode(shortCode)
                .creationDate(LocalDateTime.now())
                .user(user)
                .build();

        urlMappingRepository.save(newMapping);

        return shortCode;
    }

    public String getLongUrl(String shortCode) {
        return urlMappingRepository.findByShortCode(shortCode)
                .map(UrlMapping::getLongUrl)
                .orElseThrow(() -> new RuntimeException("Not found"));
    }

    public String generateRandomCode() {
        StringBuilder code = new StringBuilder(SHORTCODE_LENGTH);
        for (int i = 0; i < SHORTCODE_LENGTH; i++) {
            int index = random.nextInt(ALPHABET.length());
            code.append(ALPHABET.charAt(index));
        }
        return code.toString();
    }

    public List<UrlMapping> getAllByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen."));

        return urlMappingRepository.findAllByUser(user);
    }

    public void deleteByShortCode(String shortCode) {
        UrlMapping urlMapping = urlMappingRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Link not found"));

        urlMappingRepository.delete(urlMapping);
    }
}

