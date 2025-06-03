package com.example.tinylink.service;

import com.example.tinylink.dto.LinkDTO.HistoryResponse;
import com.example.tinylink.entity.UrlMapping;
import com.example.tinylink.repository.UserRepository;
import com.example.tinylink.dto.StatsDTO.StatsMapper;
import com.example.tinylink.dto.StatsDTO.StatsDTO;
import com.example.tinylink.entity.User;
import com.example.tinylink.repository.UrlMappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {

    private final UrlMappingRepository urlMappingRepository;
    private final UserRepository userRepository;

    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int SHORTCODE_LENGTH = 6;
    private final Random random = new Random();

    public String shortenUrl(String shortCodee, String longUrl, LocalDateTime expiryDate, String username) {
        User user = null;
        if (username != null) {
            user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen."));

            // Provjera da li već postoji aktivni URL
            Optional<UrlMapping> existing = urlMappingRepository.findByUserAndLongUrl(user, longUrl);
            if (existing.isPresent()) {
                UrlMapping existingUrl = existing.get();
                if (existingUrl.getExpiryDate() == null || existingUrl.getExpiryDate().isAfter(LocalDateTime.now())) {
                    return existingUrl.getShortCode(); // još nije istekao
                } else {
                    urlMappingRepository.delete(existingUrl); // istekao -> obriši
                }
            }

        } else {
            Optional<UrlMapping> u = urlMappingRepository.findByUserAndLongUrl(null, longUrl);
            if (u.isPresent()) {
                UrlMapping url = u.get();
                if ((url.getExpiryDate() == null || url.getExpiryDate().isAfter(LocalDateTime.now())) && longUrl != null) {
                    return url.getShortCode();
                } else {
                    urlMappingRepository.delete(url); // obriši ako je istekao
                }
            }
        }

        if (longUrl == null || longUrl.isBlank()) {
            throw new IllegalArgumentException("URL ne smije biti prazan.");
        }

        String shortCode = null;

        // Ako korisnik želi da unese custom shortCode
        if (shortCodee != null && !shortCodee.isBlank()) {
            if (!shortCodee.matches("^[a-zA-Z0-9]{4,10}$")) {
                throw new IllegalArgumentException("Short code mora imati 4-10 slova ili brojeva");
            }

            Optional<UrlMapping> shortCode2 = urlMappingRepository.findByShortCode(shortCodee);
            if (shortCode2.isPresent()) {
                throw new IllegalArgumentException("Short code već postoji!");
            } else {
                shortCode = shortCodee;
            }
        }

        // Generiši random short code ako nije unesen
        if (shortCode == null) {
            do {
                shortCode = generateRandomCode();
            } while (urlMappingRepository.findByShortCode(shortCode).isPresent());
        }

        UrlMapping newMapping = UrlMapping.builder()
                .longUrl(longUrl)
                .shortCode(shortCode)
                .creationDate(LocalDateTime.now())
                .expiryDate(expiryDate)
                .user(user)
                .build();

        urlMappingRepository.save(newMapping);
        return shortCode;
    }

    public String getLongUrl(String shortCode) {
        Optional<UrlMapping> mapping = urlMappingRepository.findByShortCode(shortCode);

        UrlMapping urlMapping = mapping.orElseThrow(() -> new RuntimeException("Not found"));

        // ➕ Update tracking podaci
        urlMapping.setClickCount(urlMapping.getClickCount() + 1);
        urlMapping.setLastAccessed(LocalDateTime.now());

        // ➕ Sačuvaj promjene
        urlMappingRepository.save(urlMapping);

        return
                urlMapping.getLongUrl();


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

    public StatsDTO Stats(String shortCode){
        Optional<UrlMapping> mapping = urlMappingRepository.findByShortCode(shortCode);
        if(mapping.isPresent()){
            StatsDTO dto = StatsMapper.statsDto(mapping.get());
            return dto;
        }else{
            throw new RuntimeException("Not found");
        }

    }

    public void deleteByShortCode(String shortCode) {
        UrlMapping urlMapping = urlMappingRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Link not found"));

        urlMappingRepository.delete(urlMapping);
    }
}

