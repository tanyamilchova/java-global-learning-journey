package ua.epam.mishchenko.ticketbooking.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.epam.mishchenko.ticketbooking.dao.UserAccountDAO;
import ua.epam.mishchenko.ticketbooking.exception.DbException;
import ua.epam.mishchenko.ticketbooking.model.UserAccount;
import ua.epam.mishchenko.ticketbooking.model.impl.UserAccountImpl;
import ua.epam.mishchenko.ticketbooking.service.UserAccountService;

public class UserAccountServiceImpl implements UserAccountService {
    private static final Logger logger = LoggerFactory.getLogger(UserAccountServiceImpl.class);
    private  UserAccountDAO userAccountDAO;

    public UserAccountServiceImpl() {
    }

    public UserAccountServiceImpl(UserAccountDAO userAccountDAO) {
        this.userAccountDAO = userAccountDAO;
    }

    @Override
    public UserAccount createUserAccount(long userId) {
        logger.debug("Start creating a user account with userId: {}", userId);

        try{
            UserAccount userAccount = userAccountDAO.insert(createNewUserAccount(userId));
            logger.debug("Successfully creating userAccount for userId: {}", userId);
            return userAccount;
        } catch (DbException exception){
            logger.warn("Can not to create a user account for user with id {}", userId, exception);
            return null;
        }
    }

    private UserAccount createNewUserAccount(long userId) {
        return new UserAccountImpl(userId);
    }

    @Override
    public UserAccount getAccountByUserId(long userId) {
        logger.debug("Start getting a user account with userId: {}", userId);

        try {
            UserAccount userAccount = userAccountDAO.getByUserId(userId);
            logger.debug("Successfully getting userAccount for userId: {}", userId);
            return userAccount;
        } catch (DbException exception){
            logger.warn("Can not to get a user account for user with id {}", userId, exception);
            return null;
        }
    }

    @Override
    public UserAccount updateAccount(UserAccount userAccount) {
        logger.debug("Start updating a user account with userId: {}", userAccount.getUserId());

        try {
            UserAccount userAccountUpdated =  userAccountDAO.update(userAccount);
            logger.debug("Successfully updating userAccount for userId: {}", userAccountUpdated.getUserId());
            return userAccount;
        } catch (DbException exception){
            logger.warn("Can not to update a user account for user with id {}", userAccount.getUserId(), exception);
            return null;
        }
    }

    @Override
    public boolean deleteUserAccount(long userId) {
        logger.debug("Start deleting a user account with userId: {}", userId);

        try {
            boolean isRemoved = userAccountDAO.delete(userId);
            logger.debug("Successfully deleting userAccount for userId: {}", userId);
            return isRemoved;
        } catch (DbException exception){
            logger.warn("Can not to delete a user account for user with id {}", userId, exception);
            return false;
        }
    }


    public void setUserAccountDAO(UserAccountDAO userAccountDAO) {
        this.userAccountDAO = userAccountDAO;
    }
}