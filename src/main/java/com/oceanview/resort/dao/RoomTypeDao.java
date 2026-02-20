package com.oceanview.resort.dao;

import com.oceanview.resort.model.RoomType;

import java.util.List;

public interface RoomTypeDao {
    List<RoomType> findAll();

    RoomType findById(long id);

    RoomType findByName(String typeName);

    void create(RoomType roomType);

    void update(RoomType roomType);

    boolean deleteById(long id);
}

