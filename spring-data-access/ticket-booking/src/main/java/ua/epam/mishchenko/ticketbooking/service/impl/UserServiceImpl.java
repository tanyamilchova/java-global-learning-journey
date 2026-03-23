package ua.epam.mishchenko.ticketbooking.service.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.epam.mishchenko.ticketbooking.dao.UserDAO;
import ua.epam.mishchenko.ticketbooking.exception.DbException;
import ua.epam.mishchenko.ticketbooking.model.impl.UserImpl;
import ua.epam.mishchenko.ticketbooking.service.UserService;
import ua.epam.mishchenko.ticketbooking.validator.GenericValidator;
import ua.epam.mishchenko.ticketbooking.validator.UserValidator;

import java.util.List;
import java.util.Optional;

@Service("userService")
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);

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
    public Optional<UserImpl> getUserById(long userId) {
        genericValidator.validateId(userId, "id");
        LOGGER.debug("Finding a user by id: {}", userId);

        try {
            Optional<UserImpl> user = userDAO.getById(userId);

            if (user.isPresent()) {
                LOGGER.info("User with id {} successfully found", userId);
            } else {
                LOGGER.info("No user found with id {}", userId);
            }
            return user;

        } catch (DbException exception) {
            LOGGER.warn("Failed to get user by id: {}", userId, exception);
            throw exception;
        }
    }

    @Override
    public List<UserImpl> getAllUsers(int pageSize, int pageNum) {
        genericValidator.validatePagination(pageSize, pageNum);
        LOGGER.debug("Retrieving all users with pageSize: {} and pageNum: {}", pageSize, pageNum);


        try {
            List<UserImpl> users = userDAO.getAll(pageSize, pageNum);

            LOGGER.info("Successfully retrieved {} users", users.size());

            return users;
        } catch (DbException e) {
            LOGGER.warn("Cannot retrieve users with pageSize: {} and pageNum: {}", pageSize, pageNum, e);
            return List.of();
        }
    }

    @Override
    public Optional<UserImpl> getUserByEmail(String email) {
        userValidator.validateEmail(email);
        LOGGER.debug("Finding a user by email: {}", email);

        try {
            Optional<UserImpl> user = userDAO.getByEmail(email);

            LOGGER.info("User with email {} successfully found", email);
            return user;

        } catch (DbException exception) {
            LOGGER.warn("Failed to get user by email: {}", email, exception);
            throw exception;
        }
    }

    @Override
    public List<UserImpl> getUsersByName(String name, int pageSize, int pageNum) {
        userValidator.validateString(name, "User name");
        genericValidator.validatePagination(pageSize, pageNum);

        LOGGER.debug("Finding all users by name '{}' with page size {} and page number {}", name, pageSize, pageNum);

        try {
            List<UserImpl> usersByName = userDAO.getByName(name, pageSize, pageNum);

            LOGGER.info("Successfully found {} users by name '{}' with page size {} and page number {}", usersByName.size(), name, pageSize, pageNum);

            return usersByName;
        } catch (DbException exception) {
            LOGGER.warn("Failed to find users by name '{}', page size {}, page number {}", name, pageSize, pageNum, exception);

            throw exception;
        }
    }

    @Transactional
    @Override
    public UserImpl createUser(UserImpl user) {
       userValidator.validate(user);

        LOGGER.debug("Start creating a user: {}", user);
        try {
            Optional<UserImpl> existingUser = userDAO.getByEmail(user.getEmail());

            if (existingUser.isPresent()) {
                LOGGER.info("Attempt to create a user failed: email {} is already registered.", user.getEmail());
                throw new DbException("User already exists with email: " + user.getEmail());
            }

            user = userDAO.insert(user);
            LOGGER.info("Successfully created user: {}", user);
            return user;
        } catch (DbException exception) {
            LOGGER.warn("Cannot create user: {}", user, exception);
            throw exception;
        }
    }


    @Override
    @Transactional
    public UserImpl updateUser(UserImpl user) {
        userValidator.validate(user);
        LOGGER.debug("Start updating user: {}", user);

        try {
            user = userDAO.update(user);

            LOGGER.info("Successfully updated user: {}", user);

            return user;
        } catch (DbException exception) {
            LOGGER.warn("Failed to update user: {}", user, exception);
            throw exception;
        }
    }

    @Override
    @Transactional
    public boolean deleteUser(long userId) {

        genericValidator.validateId(userId, "User id");
        LOGGER.debug("Start deleting  user with id : {}", userId);

        try {
            boolean isRemoved = userDAO.delete(userId);

            if (isRemoved) {
                LOGGER.info("Successfully deleted user with id: {}", userId);
            } else {
                LOGGER.warn("No user found to delete with id: {}", userId);
            }

            return isRemoved;
        } catch (DbException e) {
            LOGGER.log(Level.WARN, "Can not to delete an user with id: {}", userId, e);
            return false;
        }
    }
}
