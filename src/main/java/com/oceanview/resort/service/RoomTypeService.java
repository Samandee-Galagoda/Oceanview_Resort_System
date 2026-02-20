package com.oceanview.resort.service;

import com.oceanview.resort.dao.RoomTypeDao;
import com.oceanview.resort.model.RoomType;
import com.oceanview.resort.util.ValidationUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RoomTypeService {
    private final RoomTypeDao roomTypeDao;

    public RoomTypeService(RoomTypeDao roomTypeDao) {
        this.roomTypeDao = roomTypeDao;
    }

    public List<RoomType> list() {
        return roomTypeDao.findAll();
    }

    public void create(String name, String nightlyRate, String maxOccupancy, String description) {
        List<String> errors = new ArrayList<>();
        ValidationUtil.requireNonBlank(name, "Room type name", errors);
        ValidationUtil.requireNonBlank(nightlyRate, "Nightly rate", errors);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("; ", errors));
        }
        if (roomTypeDao.findByName(name.trim()) != null) {
            throw new IllegalArgumentException("Room type already exists");
        }
        BigDecimal rate;
        try {
            rate = new BigDecimal(nightlyRate.trim());
        } catch (Exception ex) {
            throw new IllegalArgumentException("Nightly rate must be a number");
        }
        int maxOcc = 2;
        if (maxOccupancy != null && !maxOccupancy.trim().isEmpty()) {
            try {
                maxOcc = Integer.parseInt(maxOccupancy.trim());
            } catch (Exception ex) {
                throw new IllegalArgumentException("Max occupancy must be an integer");
            }
        }
        if (rate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Nightly rate must be greater than 0");
        }
        if (maxOcc <= 0) {
            throw new IllegalArgumentException("Max occupancy must be greater than 0");
        }
        RoomType type = new RoomType();
        type.setTypeName(name.trim());
        type.setNightlyRate(rate);
        type.setMaxOccupancy(maxOcc);
        type.setDescription(description);
        roomTypeDao.create(type);
    }

    public void update(String id, String name, String nightlyRate, String maxOccupancy, String description) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Room type id is required");
        }
        long rid;
        try {
            rid = Long.parseLong(id.trim());
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid room type id");
        }
        RoomType existing = roomTypeDao.findById(rid);
        if (existing == null) {
            throw new IllegalArgumentException("Room type not found");
        }
        if (name != null && !name.trim().isEmpty()) {
            RoomType byName = roomTypeDao.findByName(name.trim());
            if (byName != null && byName.getId() != existing.getId()) {
                throw new IllegalArgumentException("Room type name already exists");
            }
            existing.setTypeName(name.trim());
        }
        if (nightlyRate != null && !nightlyRate.trim().isEmpty()) {
            try {
                existing.setNightlyRate(new BigDecimal(nightlyRate.trim()));
            } catch (Exception ex) {
                throw new IllegalArgumentException("Nightly rate must be a number");
            }
        }
        if (maxOccupancy != null && !maxOccupancy.trim().isEmpty()) {
            try {
                existing.setMaxOccupancy(Integer.parseInt(maxOccupancy.trim()));
            } catch (Exception ex) {
                throw new IllegalArgumentException("Max occupancy must be an integer");
            }
        }
        existing.setDescription(description);
        roomTypeDao.update(existing);
    }

    public void delete(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Room type id is required");
        }
        long rid;
        try {
            rid = Long.parseLong(id.trim());
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid room type id");
        }
        boolean deleted = roomTypeDao.deleteById(rid);
        if (!deleted) {
            throw new IllegalStateException("Delete failed");
        }
    }
}

