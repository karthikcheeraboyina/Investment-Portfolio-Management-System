package com.example.ipmsBackend.service;

import com.example.ipmsBackend.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User registerUser(User user);
    Optional<User> loginUser(String username, String password);
    User updateUserProfile(Long userId, User updatedUser);

    List<User> getAllUsers();
    Optional<User> getUserById(Long userId);
    List<User> getUsersByRole(User.UserRole role);
    List<User> searchUsers(String keyword);

    void deleteUser(Long userId);
    long getUsersCountByRole(User.UserRole role);
}
