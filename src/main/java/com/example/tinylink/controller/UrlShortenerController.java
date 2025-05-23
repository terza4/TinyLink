package com.example.tinylink.controller;

import com.example.tinylink.dto.LinkDTO.ShortenRequest;
import com.example.tinylink.dto.LinkDTO.ShortenResponse;
import com.example.tinylink.entity.UrlMapping;
import com.example.tinylink.service.UrlShortenerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UrlShortenerController {

    private final UrlShortenerService urlShortenerService;

    @PostMapping("/shorten")
    public ResponseEntity<ShortenResponse> shorten(@RequestBody ShortenRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = null;
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            username = authentication.getName();
            System.out.println("Ulogovani korisnik: " + username);
        } else {
            System.out.println("Anonimni korisnik kreira link");
        }

        String shortCode = urlShortenerService.shortenUrl(request.getUrl(), username);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ShortenResponse("http://localhost:8080/api/" + shortCode));
    }

        @GetMapping("/{shortCode}")
        public RedirectView redirectToLongUrl(@PathVariable String shortCode) {
            String longUrl = urlShortenerService.getLongUrl(shortCode);
            return new RedirectView(longUrl);
        }

    @GetMapping("/history")
    public ResponseEntity<List<ShortenResponse>> getUserHistory() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = authentication.getName();
        List<UrlMapping> mappings = urlShortenerService.getAllByUsername(username);

        List<ShortenResponse> responses = mappings.stream()
                .map(mapping -> new ShortenResponse("http://localhost:8080/api/" + mapping.getShortCode()))
                .toList();

        return ResponseEntity.ok(responses);
    }



}
