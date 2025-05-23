package com.example.tinylink.service;

import com.example.tinylink.entity.UrlMapping;
import com.example.tinylink.repository.UrlMappingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TinyLinkServiceTest {
    @InjectMocks
    private UrlShortenerService service;

    @Mock
    private UrlMappingRepository repo;

   /* @Test
    void testCreateShortUrl() {
        UrlMapping saved = new UrlMapping();
        saved.setLongUrl("test.www");

        when(repo.findByLongUrl("test.www")).thenReturn(Optional.empty());
        when(repo.findByShortCode(anyString())).thenReturn(Optional.empty());
        when(repo.save(any(UrlMapping.class))).thenReturn(saved);

        String result = service.shortenUrl("test.www", username);

        assertNotNull(result);
        verify(repo).save(any(UrlMapping.class));
    }

    */


    @Test
    void testRedirectToLongUrl() {
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setLongUrl("test.www");
        urlMapping.setShortCode("eTJ247");

        when(repo.findByShortCode("eTJ247")).thenReturn(Optional.of(urlMapping));

        String result = service.getLongUrl("eTJ247");

        assertEquals("test.www", result);
        verify(repo).findByShortCode("eTJ247");
    }

    @Test
    void testGenerateRandomCode() {
        String result = service.generateRandomCode();

        assertNotNull(result);
        assertEquals(6, result.length());
        assertTrue(result.matches("^[a-zA-Z0-9]+$"));
    }

}
