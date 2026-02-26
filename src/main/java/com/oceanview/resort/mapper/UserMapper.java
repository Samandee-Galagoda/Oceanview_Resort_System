package com.oceanview.resort.mapper;

import com.oceanview.resort.dto.UserDTO;
import com.oceanview.resort.model.User;

/**
 * Mapper for User <-> UserDTO.
 * Note: does not expose password or passwordHash to the DTO.
 */
public class UserMapper {

    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFullName(user.getFullName());
        dto.setRole(user.getRole());
        return dto;
    }

    public User toModel(UserDTO dto) {
        if (dto == null) {
            return null;
        }
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setFullName(dto.getFullName());
        user.setRole(dto.getRole());
        return user;
    }
}

