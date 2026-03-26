package ua.epam.mishchenko.ticketbooking.service.impl;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.epam.mishchenko.ticketbooking.dao.UserDAO;
import ua.epam.mishchenko.ticketbooking.exception.DbException;
import ua.epam.mishchenko.ticketbooking.model.impl.User;
import ua.epam.mishchenko.ticketbooking.service.UserService;
import ua.epam.mishchenko.ticketbooking.validator.GenericValidator;
import ua.epam.mishchenko.ticketbooking.validator.UserValidator;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service("userService")
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final UserValidator userValidator;
    private final GenericValidator genericValidator;

    @Autowired
    public UserServiceImpl(UserDAO userDAO, UserValidator userValidator, GenericValidator genericValidator) {
        this.userDAO = userDAO;
        this.userValidator = userValidator;
        this.genericValidator = genericValidator;
    }

    @Override
    public Optional<User> getUserById(long userId) {
        genericValidator.validateId(userId, "id");
        log.debug("Finding a user by id: {}", userId);

        try {
            Optional<User> user = userDAO.getById(userId);

            if (user.isPresent()) {
                log.info("User with id {} successfully found", userId);
            } else {
                log.info("No user found with id {}", userId);
            }
            return user;

        } catch (DbException exception) {
            log.warn("Failed to get user by id: {}", userId, exception);
            throw exception;
        }
    }

    @Override
    public List<User> getAllUsers(int pageSize, int pageNum) {
        genericValidator.validatePagination(pageSize, pageNum);
        log.debug("Retrieving all users with pageSize: {} and pageNum: {}", pageSize, pageNum);


        try {
            List<User> users = userDAO.getAll(pageSize, pageNum);

            log.info("Successfully retrieved {} users", users.size());

            return users;
        } catch (DbException e) {
            log.warn("Cannot retrieve users with pageSize: {} and pageNum: {}", pageSize, pageNum, e);
            return List.of();
        }
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        userValidator.validateEmail(email);
        log.debug("Finding a user by email: {}", email);

        try {
            Optional<User> user = userDAO.getByEmail(email);

            log.info("User with email {} successfully found", email);
            return user;

        } catch (DbException exception) {
            log.warn("Failed to get user by email: {}", email, exception);
            throw exception;
        }
    }

    @Override
    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        userValidator.validateString(name, "User name");
        genericValidator.validatePagination(pageSize, pageNum);

        log.debug("Finding all users by name '{}' with page size {} and page number {}", name, pageSize, pageNum);

        try {
            List<User> usersByName = userDAO.getByName(name, pageSize, pageNum);

            log.info("Successfully found {} users by name '{}' with page size {} and page number {}", usersByName.size(), name, pageSize, pageNum);

            return usersByName;
        } catch (DbException exception) {
            log.warn("Failed to find users by name '{}', page size {}, page number {}", name, pageSize, pageNum, exception);

            throw exception;
        }
    }

    @Transactional
    @Override
    public User createUser(User user) {
       userValidator.validate(user);

        log.debug("Start creating a user: {}", user);
        try {
            Optional<User> existingUser = userDAO.getByEmail(user.getEmail());

            if (existingUser.isPresent()) {
                log.info("Attempt to create a user failed: email {} is already registered.", user.getEmail());
                throw new DbException("User already exists with email: " + user.getEmail());
            }

            user = userDAO.insert(user);
            log.info("Successfully created user: {}", user);
            return user;
        } catch (DbException exception) {
            log.warn("Cannot create user: {}", user, exception);
            throw exception;
        }
    }


    @Override
    @Transactional
    public User updateUser(User user) {
        userValidator.validate(user);
        log.debug("Start updating user: {}", user);

        try {
            user = userDAO.update(user);

            log.info("Successfully updated user: {}", user);

            return user;
        } catch (DbException exception) {
            log.warn("Failed to update user: {}", user, exception);
            throw exception;
        }
    }

    @Override
    @Transactional
    public boolean deleteUser(long userId) {

        genericValidator.validateId(userId, "User id");
        log.debug("Start deleting  user with id : {}", userId);

        try {
            boolean isRemoved = userDAO.delete(userId);

            if (isRemoved) {
                log.info("Successfully deleted user with id: {}", userId);
            } else {
                log.warn("No user found to delete with id: {}", userId);
            }

            return isRemoved;
        } catch (DbException e) {
            log.log(Level.WARN, "Can not to delete an user with id: {}", userId, e);
            return false;
        }
    }
}
