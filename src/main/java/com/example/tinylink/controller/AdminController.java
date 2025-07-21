package com.example.tinylink.controller;


import com.example.tinylink.entity.UrlMapping;
import com.example.tinylink.entity.User;
import com.example.tinylink.repository.UserRepository;
import com.example.tinylink.repository.UrlMappingRepository;
import com.example.tinylink.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final UrlMappingRepository urlMappingRepository;
    private final UserRepository userRepository;
    private final AdminService adminService;

    public AdminController(UrlMappingRepository urlMappingRepository, UserRepository userRepository, AdminService adminService) {
        this.urlMappingRepository = urlMappingRepository;
        this.userRepository = userRepository;
        this.adminService = adminService;
    }



    @GetMapping("/urls")
    public ResponseEntity<List<UrlMapping>> getAllUrls() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            List<UrlMapping> urls = adminService.findAllUrls();
        return ResponseEntity.ok(urls);

       }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            List<User> users = adminService.findAllUsers();
            return ResponseEntity.ok(users);

        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            User user = adminService.findUser(id);
            return ResponseEntity.ok(user);

        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/urls/{id}")
    public ResponseEntity<UrlMapping> getUrl(@PathVariable Long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            UrlMapping urlMapping = adminService.findUrl(id);
            return ResponseEntity.ok(urlMapping);

        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {

                User user = adminService.deleteUser(id);
                return ResponseEntity.ok(user);

        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @DeleteMapping("/urls/{id}")
    public ResponseEntity<UrlMapping> deleteUrl(@PathVariable Long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                UrlMapping urlMapping = adminService.deleteUrl(id);
                return ResponseEntity.ok(urlMapping);

        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }




}
