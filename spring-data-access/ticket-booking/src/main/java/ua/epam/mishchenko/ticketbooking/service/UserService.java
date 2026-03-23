package ua.epam.mishchenko.ticketbooking.service;

import ua.epam.mishchenko.ticketbooking.model.impl.UserImpl;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<UserImpl> getUserById(long userId);

    Optional<UserImpl> getUserByEmail(String email);

    List<UserImpl> getUsersByName(String name, int pageSize, int pageNum);

    List<UserImpl> getAllUsers(int pageSize, int pageNum);

    UserImpl createUser(UserImpl user);

    UserImpl updateUser(UserImpl user);

    boolean deleteUser(long userId);
}
