package com.example.hiveinform.service;

import com.example.hiveinform.dto.UserFindAllDto;
import com.example.hiveinform.entity.User;

import java.util.List;

public interface UserService {
    User getByUsername(String username);

    User save(User user);

    User getUserById(long id);

    List<User> getUserByEmail(String email);

    User updateUser(User user);

    List<User> getAll (UserFindAllDto userFindAllDto);
}
