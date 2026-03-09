package ua.epam.mishchenko.ticketbooking.service.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.mishchenko.ticketbooking.dao.UserAccountDAO;
import ua.epam.mishchenko.ticketbooking.exception.DbException;
import ua.epam.mishchenko.ticketbooking.model.UserAccount;
import ua.epam.mishchenko.ticketbooking.model.impl.UserAccountImpl;
import ua.epam.mishchenko.ticketbooking.service.UserAccountService;

public class UserAccountServiceImpl implements UserAccountService {
    private static final Logger LOGGER = LogManager.getLogger(UserAccountServiceImpl.class);
    private  UserAccountDAO userAccountDAO;


    public UserAccountServiceImpl() {
    }

    public UserAccountServiceImpl(UserAccountDAO userAccountDAO) {
        this.userAccountDAO = userAccountDAO;
    }

    @Override
    public UserAccount createUserAccount(long userId) {
        LOGGER.log(Level.DEBUG, "Start creating a user account with userId: {}", userId);

        try{
            UserAccount userAccount = userAccountDAO.insert(createNewUserAccount(userId));
            LOGGER.log(Level.DEBUG, "Successfully creating userAccount for userId: {}", userId);
            return userAccount;
        } catch (DbException exception){
            LOGGER.log(Level.WARN,
                    "Can not to create a user account for user with id {}", userId, exception);
            return null;
        }
    }


    private UserAccount createNewUserAccount(long userId) {
        return new UserAccountImpl(userId);
    }


    @Override
    public UserAccount getUserAccountByUserId(long userId) {
        LOGGER.log(Level.DEBUG, "Start getting a user account with userId: {}", userId);

        try {
            UserAccount userAccount = userAccountDAO.getByUserId(userId);
            LOGGER.log(Level.DEBUG, "Successfully getting userAccount for userId: {}", userId);
            return userAccount;
        } catch (DbException exception){
            LOGGER.log(Level.WARN,
                    "Can not to get a user account for user with id {}", userId, exception);
            return null;
        }
    }

    @Override
    public UserAccount updateUserAccount(UserAccount userAccount) {
        LOGGER.log(Level.DEBUG, "Start updating a user account with userId: {}", userAccount.getUserId());

        try {
            UserAccount userAccountUpdated =  userAccountDAO.update(userAccount);
            LOGGER.log(Level.DEBUG, "Successfully updating userAccount for userId: {}", userAccountUpdated.getUserId());
            return userAccount;
        } catch (DbException exception){
            LOGGER.log(Level.WARN,
                    "Can not to update a user account for user with id {}", userAccount.getUserId(), exception);
            return null;
        }
    }

    @Override
    public boolean deleteUserAccount(long userId) {
        LOGGER.log(Level.DEBUG, "Start deleting a user account with userId: {}", userId);

        try {
            boolean isRemoved = userAccountDAO.delete(userId);
            LOGGER.log(Level.DEBUG, "Successfully deleting userAccount for userId: {}", userId);
            return isRemoved;
        } catch (DbException exception){
            LOGGER.log(Level.WARN,
                    "Can not to delete a user account for user with id {}", userId, exception);
            return false;
        }
    }

    public UserAccountDAO getUserAccountDAO() {
        return userAccountDAO;
    }

    public void setUserAccountDAO(UserAccountDAO userAccountDAO) {
        this.userAccountDAO = userAccountDAO;
    }
}
