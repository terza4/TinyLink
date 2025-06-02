package com.example.tinylink.service;

import com.example.tinylink.entity.UrlMapping;
import com.example.tinylink.repository.UrlMappingRepository;
import com.example.tinylink.repository.UserRepository;
import com.example.tinylink.service.UserService;
import com.example.tinylink.config.SecurityConfig;
import com.example.tinylink.security.JwtTokenProvider;
import com.example.tinylink.dto.UserDTO.UserDTO;
import com.example.tinylink.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TinyLinkServiceTest {
    @InjectMocks
    private UrlShortenerService service;

    @Mock
    private UrlMappingRepository repo;

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private UserService userService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void testCreateShortUrl() {
        UrlMapping saved = new UrlMapping();
        saved.setLongUrl("test.www");
         User username = new User();
         username.setUsername("test");

        when(userRepo.findByUsername(anyString())).thenReturn(Optional.of(username));
        when(repo.findByShortCode(anyString())).thenReturn(Optional.empty());
        when(repo.save(any(UrlMapping.class))).thenReturn(saved);

        String result = service.shortenUrl(saved.getShortCode(), saved.getLongUrl(), username.getUsername());

        assertNotNull(result);
        verify(repo).save(any(UrlMapping.class));
    }






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

    @Test
    void testGetAllByUsername(){
        User user = new User();
        user.setId(2L);
        user.setUsername("test");

        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setShortCode("EKt5ro");
        urlMapping.setUser(user);

        UrlMapping urlMapping1 = new UrlMapping();
        urlMapping1.setShortCode("h4kNI0");
        urlMapping1.setUser(user);

        List<UrlMapping> urlMappings = new ArrayList<>();
        urlMappings.add(urlMapping);
        urlMappings.add(urlMapping1);

        when(userRepo.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(repo.findAllByUser(user)).thenReturn(urlMappings);

        List<UrlMapping> urlMappingList = service.getAllByUsername(user.getUsername());

        assertNotNull(urlMappingList);
        assertEquals(2, urlMappingList.size());
        verify(repo, times(1)).findAllByUser(user);

    }


    // JUnit tests for UserService
   /* @Test
    void testRegisterUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUsername");
        user.setPassword("testPassword");

        when(userRepo.existsByUsername(user.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("hashedPassword");
        when(jwtTokenProvider.generateToken(user).thenReturn("mockedToken");

        String token = userService.register(user);

        assertNotNull(token);
        assertEquals("mockedToken", token);

        // provjera da li je korisnik sa hesiranom lozinkom sačuvan
        verify(userRepo).save(argThat(user ->
                user.getUsername().equals(user.getUsername()) &&
                        user.getPassword().equals("hashedPassword")
        ));

        verify(jwtTokenProvider).generateToken(user);
    }

    */



    @Test
    void testLoginUser() {
        UserDTO dto = new UserDTO();
        dto.setUsername("testUsername");
        dto.setPassword("testPassword");

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword("hashedPassword");

        when(userRepo.findByUsername(dto.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtTokenProvider.generateToken(user)).thenReturn("mockedToken");

        String token = userService.login(dto);

        assertNotNull(token);
        assertEquals("mockedToken", token);
        verify(jwtTokenProvider).generateToken(user);
    }

}


