package com.oceanview.resort.dao;

import com.oceanview.resort.model.User;

public interface UserDao {
    User findByUsername(String username);

    void create(User user);

    boolean existsAny();

    java.util.List<User> findAll();

    User findById(long id);

    void update(User user);

    boolean deleteById(long id);
}
