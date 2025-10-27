package com.example.blog2.service.impl;

import com.example.blog2.dao.UserRepository;
import com.example.blog2.po.User;
import com.example.blog2.service.UserService;
import com.example.blog2.util.BcryptUtils;
import com.example.blog2.util.MyBeanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = Objects.requireNonNull(userRepository, "userRepository must not be null");
    }

    @Override
    public User checkUser(String username, String password) {
        Objects.requireNonNull(username, "username must not be null");
        Objects.requireNonNull(password, "password must not be null");

        try {
            User user = userRepository.findByUsername(username);
            if (user == null) {
                return null;
            }
            String hashedPassword = user.getPassword();
            if (BcryptUtils.verify(password, hashedPassword)) {
                return user;
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Failed to check user", e);
        }
    }

    @Transactional
    @Override
    public User findUserById(Long id) {
        Objects.requireNonNull(id, "id must not be null");

        try {
            return userRepository.getOne(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to find user by id: " + id, e);
        }
    }

    @Override
    public User save(User user) {
        Objects.requireNonNull(user, "user must not be null");

        try {
            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());
            if (user.getAvatar() == null || user.getAvatar().isEmpty()) {
                user.setAvatar("");
            }
            return userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save user", e);
        }
    }

    @Transactional
    @Override
    public User updateUser(Long id, User admin) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(admin, "admin must not be null");

        try {
            User u = userRepository.getOne(id);
            Objects.requireNonNull(u, "User not found with id: " + id);

            BeanUtils.copyProperties(admin, u, MyBeanUtils.getNullPropertyNames(admin));
            u.setUpdateTime(new Date()); // 确保更新时间被刷新
            return userRepository.save(u);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update user with id: " + id, e);
        }
    }

    @Override
    public List<User> listUser() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Failed to list users", e);
        }
    }

    @Override
    public Boolean deleteUserById(Long id) {
        Objects.requireNonNull(id, "id must not be null");

        try {
            userRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete user with id: " + id, e);
        }
    }

    @Override
    @Transactional
    public Boolean resetPassword(String newPassword, String email) {
        Objects.requireNonNull(newPassword, "newPassword must not be null");
        Objects.requireNonNull(email, "email must not be null");

        try {
            User user = userRepository.findByEmail(email);
            Objects.requireNonNull(user, "User not found with email: " + email);

            String hashedPassword = BcryptUtils.encrypt(newPassword);
            int affectedRows = userRepository.resetPassword(user.getId(), hashedPassword);
            return affectedRows > 0;
        } catch (Exception e) {
            throw new RuntimeException("Failed to reset password for email: " + email, e);
        }
    }

    @Override
    @Transactional
    public Boolean resetPasswordAdmin(Long userId, String newPassword) {
        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(newPassword, "newPassword must not be null");

        try {
            String hashedPassword = BcryptUtils.encrypt(newPassword);
            int affectedRows = userRepository.resetPassword(userId, hashedPassword);
            return affectedRows > 0;
        } catch (Exception e) {
            throw new RuntimeException("Failed to reset password for user id: " + userId, e);
        }
    }
}