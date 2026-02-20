package com.oceanview.resort.mapper;

import com.oceanview.resort.dto.RoomTypeRequestDTO;
import com.oceanview.resort.dto.RoomTypeResponseDTO;
import com.oceanview.resort.model.RoomType;

/**
 * Mapper for RoomType <-> DTOs.
 */
public class RoomTypeMapper {

    public RoomType toModel(RoomTypeRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        RoomType type = new RoomType();
        type.setTypeName(dto.getTypeName());
        type.setNightlyRate(dto.getNightlyRate());
        type.setMaxOccupancy(dto.getMaxOccupancy());
        type.setDescription(dto.getDescription());
        return type;
    }

    public void updateModel(RoomType type, RoomTypeRequestDTO dto) {
        if (type == null || dto == null) {
            return;
        }
        type.setTypeName(dto.getTypeName());
        type.setNightlyRate(dto.getNightlyRate());
        type.setMaxOccupancy(dto.getMaxOccupancy());
        type.setDescription(dto.getDescription());
    }

    public RoomTypeResponseDTO toResponseDTO(RoomType type) {
        if (type == null) {
            return null;
        }
        RoomTypeResponseDTO dto = new RoomTypeResponseDTO();
        dto.setId(type.getId());
        dto.setTypeName(type.getTypeName());
        dto.setNightlyRate(type.getNightlyRate());
        dto.setMaxOccupancy(type.getMaxOccupancy());
        dto.setDescription(type.getDescription());
        return dto;
    }
}

