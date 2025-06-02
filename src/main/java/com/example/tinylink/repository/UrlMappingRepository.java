package com.example.tinylink.repository;

import com.example.tinylink.entity.UrlMapping;
import com.example.tinylink.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {
    List<UrlMapping> findAllByUser(User user);
    Optional<UrlMapping> findByShortCode(String shortCode);
    UrlMapping findByLongUrl(String longUrl);
    Optional<UrlMapping> findByUserAndLongUrl(User user, String longUrl);


}
