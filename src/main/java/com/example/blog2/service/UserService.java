package com.example.blog2.service;

import com.example.blog2.po.User;

import java.util.List;

public interface UserService {
    User checkUser(String username, String password);

    User findUserById(Long id);

    User save(User user);

    User updateUser(Long id, User admin);

    List<User> listUser();

    Boolean deleteUserById(Long id);

    Boolean resetPassword(String newPassword,String email);

    Boolean resetPasswordAdmin(Long userId, String newPassword);
}
