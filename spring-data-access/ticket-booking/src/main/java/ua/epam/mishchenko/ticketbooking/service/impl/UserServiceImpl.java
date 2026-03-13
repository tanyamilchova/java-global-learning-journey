package ua.epam.mishchenko.ticketbooking.service.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.epam.mishchenko.ticketbooking.dao.UserDAO;
import ua.epam.mishchenko.ticketbooking.exception.DbException;
import ua.epam.mishchenko.ticketbooking.model.User;
import ua.epam.mishchenko.ticketbooking.service.UserService;
import ua.epam.mishchenko.ticketbooking.utils.Util;

import java.util.List;
@Service("userService")
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);

    private final UserDAO userDAO;

    @Autowired
    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public User getUserById(long userId) {
        Util.validateId(userId);
        LOGGER.log(Level.DEBUG, "Finding a user by id: {}", userId);

        try {
            User user = userDAO.getById(userId);

            LOGGER.log(Level.DEBUG, "The user with id {} successfully found ", userId);

            return user;
        } catch (DbException exception) {
            LOGGER.log(Level.WARN, "Can not to get an user by id: " + userId);
            throw exception;
        }
    }

    @Override
    public List<User> getAllUsers(int pageSize, int pageNum) {
        Util.validatePagination(pageSize, pageNum);
        LOGGER.log(Level.DEBUG, "Retrieving all users");

        try {
            List<User> users = userDAO.getAll(pageSize, pageNum);

            LOGGER.log(Level.DEBUG, "Successfully retrieved {} users", users.size());

            return users;

        } catch (DbException e) {
            LOGGER.log(Level.WARN, "Cannot retrieve users", e);
            return List.of();
        }
    }

    @Override
    public User getUserByEmail(String email) {
        Util.validateEmail(email);
        LOGGER.log(Level.DEBUG, "Finding a user by email: {}", email);

        try {
            User user = userDAO.getByEmail(email);

            LOGGER.log(Level.DEBUG, "The user with email {} successfully found ", email);

            return user;
        } catch (DbException exception) {
            LOGGER.log(Level.WARN, "Can not to get an user by email: " + email);
            throw exception;
        }
    }

    @Override
    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        Util.validateString(name);
        Util.validatePagination(pageSize, pageNum);
        LOGGER.log(Level.DEBUG,
                "Finding all users by name {} with page size {} and number of page {}", name, pageSize, pageNum);

        try {
            List<User> usersByName = userDAO.getByName(name, pageSize, pageNum);

            LOGGER.log(Level.DEBUG,
                    "All users successfully found by name {} with page size {} and number of page {}", name, pageSize, pageNum);

            return usersByName;
        } catch (DbException exception) {
            LOGGER.log(Level.WARN, "Can not to find a list of users by name '{}'", name, exception);
            throw exception;
        }
    }

    @Transactional
    @Override
    public User createUser(User user) {
       Util.validateUser(user);
        LOGGER.log(Level.DEBUG, "Start creating an user: {}", user);
        try {
            User existingUser = userDAO.getByEmail(user.getEmail());

            if (existingUser != null) {
                LOGGER.log(Level.DEBUG, "Wrong credentials.");
                throw new DbException("User already exists with email: " + user.getEmail());
            }
            user = userDAO.insert(user);
            LOGGER.log(Level.DEBUG, "Successfully creation of the user: {}", user);
            return user;
        } catch (DbException exception) {
            LOGGER.log(Level.WARN, "Cannot create user: {}", user, exception);
            throw exception;
        }
    }


    @Override
    @Transactional
    public User updateUser(User user) {
        Util.validateUser(user);
        LOGGER.log(Level.DEBUG, "Start updating an user: {}", user);

        try {
            user = userDAO.update(user);

            LOGGER.log(Level.DEBUG, "Successfully updating of the user: {}", user);

            return user;
        } catch (DbException exception) {
            LOGGER.log(Level.WARN, "Can not to update an user: {}", user, exception);
            throw exception;
        }
    }

    @Override
    @Transactional
    public boolean deleteUser(long userId) {
        Util.validateId(userId);
        LOGGER.log(Level.DEBUG, "Start deleting an user with id: {}", userId);

        try {
            boolean isRemoved = userDAO.delete(userId);

            LOGGER.log(Level.DEBUG, "Successfully deletion of the user with id: {}", userId);

            return isRemoved;
        } catch (DbException e) {
            LOGGER.log(Level.WARN, "Can not to delete an user with id: {}", userId, e);
            return false;
        }
    }
}
