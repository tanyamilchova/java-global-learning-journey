package ua.epam.mishchenko.ticketbooking.service.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.epam.mishchenko.ticketbooking.dao.impl.UserDAOImpl;
import ua.epam.mishchenko.ticketbooking.exception.DbException;
import ua.epam.mishchenko.ticketbooking.model.User;
import ua.epam.mishchenko.ticketbooking.service.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private UserDAOImpl userDAO;

    @Override
    public User getUserById(long userId) {
        logger.debug("Finding a user by id: {}", userId);

        try {
            User user = userDAO.getById(userId);

            logger.debug("The user with id {} successfully found", userId);

            return user;
        } catch (DbException e) {
            logger.warn("Can not to get a user by id: {}", userId, e);
            return null;
        }
    }

    @Override
    public User getUserByEmail(String email) {
        logger.debug("Finding a user by email: {}", email);

        try {
            User user = userDAO.getByEmail(email);

            logger.debug("The user with email {} successfully found", email);

            return user;
        } catch (DbException e) {
            logger.warn("Can not to get a user by email: {}", email, e);
            return null;
        }
    }

    @Override
    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        logger.debug("Finding all users by name {} with page size {} and number of page {}", name, pageSize, pageNum);

        try {
            List<User> usersByName = userDAO.getByName(name, pageSize, pageNum);
            logger.debug("All users successfully found by name {} with page size {} and number of page {}", name, pageSize, pageNum);

            return usersByName;
        } catch (DbException e) {
            logger.warn("Can not to find a list of users by name '{}'", name, e);
            return null;
        }
    }

    @Override
    public User createUser(User user) {
        logger.debug("Start creating a user: {}", user);

        try {
            user = userDAO.insert(user);

            logger.debug("Successfully created the user: {}", user);

            return user;
        } catch (DbException e) {
            logger.warn("Can not to create a user: {}", user, e);
            return null;
        }
    }

    @Override
    public User updateUser(User user) {
        logger.debug("Start updating a user: {}", user);

        try {
            user = userDAO.update(user);

            logger.debug("Successfully updated the user: {}", user);

            return user;
        } catch (DbException e) {
            logger.warn("Can not to update a user: {}", user, e);
            return null;
        }
    }

    @Override
    public boolean deleteUser(long userId) {
        logger.debug("Start deleting a user with id: {}", userId);

        try {
            boolean isRemoved = userDAO.delete(userId);

            logger.debug("Successfully deleted the user with id: {}", userId);

            return isRemoved;
        } catch (DbException e) {
            logger.warn("Can not to delete a user with id: {}", userId, e);
            return false;
        }
    }

    public void setUserDAO(UserDAOImpl userDAO) {
        this.userDAO = userDAO;
    }
}
