package com.example.tinylink.service;

import com.example.tinylink.dto.UserDTO.UserDTO;
import com.example.tinylink.entity.User;
import com.example.tinylink.repository.UserRepository;
import com.example.tinylink.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    public String register(UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new RuntimeException("Korisničko ime je zauzeto.");
        }
        if(userDTO.getUsername().length() < 4){
            throw new RuntimeException("Korisnicko ime mora imati najmanje 4 karaktera!");
        }
        if(userDTO.getPassword().length() < 6){
            throw new RuntimeException("Lozinka mora imati najmanje 6 karaktera!");
        }
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(user);
        String tokenString = jwtTokenProvider.generateToken(user.getUsername());

        return tokenString;
    }


    public String login(UserDTO userDTO) {
        User user = userRepository.findByUsername(userDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("Korisnik ne postoji."));

        if (!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Pogrešna lozinka.");
        }

        String tokenString = jwtTokenProvider.generateToken(user.getUsername());


        return tokenString;
    }
}

