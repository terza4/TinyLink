package com.example.tinylink.controller;

import com.example.tinylink.dto.ShortenRequest;
import com.example.tinylink.dto.ShortenResponse;
import com.example.tinylink.service.UrlShortenerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UrlShortenerController {

    private final UrlShortenerService urlShortenerService;

    @PostMapping("/shorten")
    public ResponseEntity<ShortenResponse> shortenUrl(@Valid @RequestBody ShortenRequest request) {
        String shortCode = urlShortenerService.shortenUrl(request.getUrl());
        String shortUrl = "http://localhost:8080/" + shortCode;
        return new ResponseEntity<>(new ShortenResponse(shortUrl), HttpStatus.CREATED);
    }

    @GetMapping("/{shortCode}")
    public RedirectView redirectToLongUrl(@PathVariable String shortCode) {
        String longUrl = urlShortenerService.getLongUrl(shortCode);
        return new RedirectView(longUrl);
    }
}
