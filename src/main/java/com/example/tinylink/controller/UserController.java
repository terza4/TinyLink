package com.example.tinylink.controller;

import com.example.tinylink.dto.UserDTO.UserDTO;
import com.example.tinylink.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000") // dozvoljava React frontendu pristup
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        String token = userService.register(userDTO);
        return ResponseEntity.ok().body("Bearer " + token);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        String token = userService.login(userDTO);
        return ResponseEntity.ok().body("Bearer " + token);
    }

}
