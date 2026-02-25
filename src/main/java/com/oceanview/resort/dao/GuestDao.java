package com.oceanview.resort.dao;

import com.oceanview.resort.model.Guest;

public interface GuestDao {
    long create(Guest guest);

    Guest findById(long id);
}
