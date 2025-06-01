package com.example.tinylink.controller;

import com.example.tinylink.dto.LinkDTO.ShortenRequest;
import com.example.tinylink.dto.LinkDTO.ShortenResponse;
import com.example.tinylink.dto.LinkDTO.HistoryResponse;
import com.example.tinylink.entity.UrlMapping;
import com.example.tinylink.service.UrlShortenerService;
import com.example.tinylink.dto.StatsDTO.StatsDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${tinylink.base-url}")
    private String baseUrl;

    @PostMapping("/shorten")
    public ResponseEntity<ShortenResponse> shorten(@RequestBody @Valid ShortenRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = null;
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            username = authentication.getName();
        }

        String shortCode = urlShortenerService.shortenUrl(request.getShortCodee(), request.getUrl(), username);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ShortenResponse(baseUrl + shortCode));
    }

        @GetMapping("/{shortCode}")
        public RedirectView redirectToLongUrl(@PathVariable String shortCode) {
            String longUrl = urlShortenerService.getLongUrl(shortCode);
            return new RedirectView(longUrl);
        }

    @GetMapping("/history")
    public ResponseEntity<List<HistoryResponse>> getUserHistory() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = authentication.getName();
        List<UrlMapping> mappings = urlShortenerService.getAllByUsername(username);

        List<HistoryResponse> responses = mappings.stream()
                .map(mapping -> new HistoryResponse(baseUrl + mapping.getShortCode(), mapping.getClickCount()))
                .toList();



        return ResponseEntity.ok(responses);
    }

    @GetMapping("/stats/{shortCode}")
    public ResponseEntity<StatsDTO> getStats(@PathVariable String shortCode){
          StatsDTO stats = urlShortenerService.Stats(shortCode);
          return ResponseEntity.ok(stats);
    }

    @DeleteMapping("/delete/{shortCode}")
    public ResponseEntity<Void> deleteShortCode(@PathVariable("shortCode") String shortCode) {
        urlShortenerService.deleteByShortCode(shortCode);
        return ResponseEntity.noContent().build(); // 204 No Content
    }



}
