package com.example.tinylink.service;

import com.example.tinylink.dto.StatsDTO.StatsDTO;
import com.example.tinylink.dto.StatsDTO.StatsMapper;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TinyLinkServiceTest {
    @InjectMocks
    private UrlShortenerService service;

    @InjectMocks
    private AdminService adminService;

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

    // JUnit tests for  UrlShortenerService
    @Test
    void testCreateShortUrl() {
        UrlMapping saved = new UrlMapping();
        saved.setLongUrl("test.www");
         User username = new User();
         username.setUsername("test");

        when(userRepo.findByUsername(anyString())).thenReturn(Optional.of(username));
        when(repo.findByShortCode(anyString())).thenReturn(Optional.empty());
        when(repo.save(any(UrlMapping.class))).thenReturn(saved);

        String result = service.shortenUrl(saved.getShortCode(), saved.getLongUrl(), saved.getExpiryDate(), username.getUsername());

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

    @Test
    public void testStats() {
        // Arrange
        String shortCode = "abc123";
        UrlMapping mapping = new UrlMapping();
        mapping.setShortCode(shortCode);
        mapping.setLongUrl("https://example.com");
        mapping.setClickCount(5);



        when(repo.findByShortCode(shortCode)).thenReturn(Optional.of(mapping));

        // Act
        StatsDTO result = service.Stats(shortCode);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testDelete() {
        // Arrange
        String shortCode = "abc123";
        UrlMapping mapping = new UrlMapping();
        mapping.setShortCode(shortCode);

        when(repo.findByShortCode(shortCode)).thenReturn(Optional.of(mapping));

        // Act
        service.deleteByShortCode(shortCode);

        // Assert
        verify(repo).delete(mapping);
    }



    // JUnit tests for UserService
    @Test
    void testRegisterUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setPassword("testpassword");

        when(userRepo.existsByUsername("testuser")).thenReturn(false);
        when(passwordEncoder.encode("testpassword")).thenReturn("encodedPass");
        when(jwtTokenProvider.generateToken(any(User.class))).thenReturn("mocked-jwt-token");


        String token = userService.register(userDTO);


        assertEquals("mocked-jwt-token", token);
        verify(userRepo).save(any(User.class));
    }





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

    // JUnit test for AdminService

    @Test
    public void testFindAllUsers() {
        List<User> users = List.of(new User(), new User());
        when(userRepo.findAll()).thenReturn(users);

        List<User> result = adminService.findAllUsers();

        assertEquals(2, result.size());
        verify(userRepo).findAll();
    }

    @Test
    public void testFindAllUrls() {
        List<UrlMapping> urls = List.of(new UrlMapping(), new UrlMapping());
        when(repo.findAll()).thenReturn(urls);

        List<UrlMapping> result = adminService.findAllUrls();

        assertEquals(2, result.size());
        verify(repo).findAll();
    }

    @Test
    public void testFindUser() {
        Long id = 1L;
        User user = new User();
        when(userRepo.findById(id)).thenReturn(Optional.of(user));

        User result = adminService.findUser(id);

        assertEquals(user, result);
        verify(userRepo).findById(id);
    }

    @Test
    public void testFindUrl() {
        Long id = 1L;
        UrlMapping url = new UrlMapping();
        when(repo.findById(id)).thenReturn(Optional.of(url));

        UrlMapping result = adminService.findUrl(id);

        assertEquals(url, result);
        verify(repo).findById(id);
    }

    @Test
    public void testDeleteUser() {
        Long id = 1L;
        User user = new User();
        when(userRepo.findById(id)).thenReturn(Optional.of(user));

        User result = adminService.deleteUser(id);

        assertEquals(user, result);
        verify(userRepo).delete(user);
    }

    @Test
    public void testDeleteUrl() {
        Long id = 1L;
        UrlMapping url = new UrlMapping();
        when(repo.findById(id)).thenReturn(Optional.of(url));

        UrlMapping result = adminService.deleteUrl(id);

        assertEquals(url, result);
        verify(repo).delete(url);
    }

}


