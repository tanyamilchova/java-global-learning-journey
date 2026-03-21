package ua.epam.mishchenko.ticketbooking.service;

import ua.epam.mishchenko.ticketbooking.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> getUserById(long userId);

    Optional<User> getUserByEmail(String email);

    List<User> getUsersByName(String name, int pageSize, int pageNum);

    List<User> getAllUsers(int pageSize, int pageNum);

    User createUser(User user);

    User updateUser(User user);

    boolean deleteUser(long userId);
}
