package com.example.tinylink.dto.UserDTO;

import com.example.tinylink.entity.User;

public class MapperUser {

    public UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        return dto;
    }
}
