package com.example.tinylink.service;


import com.example.tinylink.entity.UrlMapping;
import com.example.tinylink.entity.User;
import com.example.tinylink.repository.UrlMappingRepository;
import com.example.tinylink.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepo;
    private final UrlMappingRepository urlRepo;

    public List<User> findAllUsers(){
        return userRepo.findAll();
    }

    public List<UrlMapping> findAllUrls(){
        return urlRepo.findAll();
    }

    public User findUser(Long id){
        Optional<User> u = userRepo.findById(id);
        User user = u.orElseThrow(() -> new RuntimeException("Not found"));
        return user;
    }

    public UrlMapping findUrl(Long id){
        Optional<UrlMapping> u = urlRepo.findById(id);
        UrlMapping url = u.orElseThrow(() -> new RuntimeException("Not found"));
        return url;
    }

    public User deleteUser(Long id){
        Optional<User> u = userRepo.findById(id);
        User user = u.orElseThrow(() -> new RuntimeException("Not found"));
        userRepo.delete(user);
        return user;
    }

    public UrlMapping deleteUrl(Long id){
        Optional<UrlMapping> u = urlRepo.findById(id);
        UrlMapping url = u.orElseThrow(() -> new RuntimeException("Not found"));
        urlRepo.delete(url);
        return url;
    }
}
