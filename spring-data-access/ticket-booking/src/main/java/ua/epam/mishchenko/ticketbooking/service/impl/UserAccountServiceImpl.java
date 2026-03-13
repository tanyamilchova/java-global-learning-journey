package ua.epam.mishchenko.ticketbooking.service.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.epam.mishchenko.ticketbooking.dao.UserAccountDAO;
import ua.epam.mishchenko.ticketbooking.dao.UserDAO;
import ua.epam.mishchenko.ticketbooking.exception.DbException;
import ua.epam.mishchenko.ticketbooking.model.User;
import ua.epam.mishchenko.ticketbooking.model.UserAccount;
import ua.epam.mishchenko.ticketbooking.model.impl.UserAccountImpl;
import ua.epam.mishchenko.ticketbooking.model.repository.UserAccountRepository;
import ua.epam.mishchenko.ticketbooking.service.UserAccountService;
import ua.epam.mishchenko.ticketbooking.utils.Util;

import java.util.Optional;

@Service
public class UserAccountServiceImpl implements UserAccountService {
    private static final Logger LOGGER = LogManager.getLogger(UserAccountServiceImpl.class);
    private  UserAccountDAO userAccountDAO;
    private UserDAO userDAO;

    @Autowired
    public UserAccountServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Autowired
    UserAccountRepository userAccountRepository;

    public UserAccountServiceImpl() {
    }

    public UserAccountServiceImpl(UserAccountDAO userAccountDAO) {
        this.userAccountDAO = userAccountDAO;
    }

    @Override
    public UserAccount createUserAccount(long userId) {
        Util.validateId(userId);
        LOGGER.log(Level.DEBUG, "Start creating a user account with userId: {}", userId);

        User user = userDAO.getById(userId);
        if (user == null) {
            LOGGER.log(Level.WARN, "User with id {} not found", userId);
            throw new DbException("User not found: " + userId);
        }

        try{
            UserAccount userAccount = createNewUserAccount(userId);
            userAccountRepository.save((UserAccountImpl)userAccount);

            LOGGER.log(Level.DEBUG, "Successfully creating userAccount for userId: {}", userId);
            return userAccount;

        } catch (Exception e) {
            LOGGER.log(Level.ERROR, "Cannot create user account for userId {}", userId, e);
            throw new DbException("Cannot create user account for userId " + userId, e);
        }
    }

    private UserAccount createNewUserAccount(long userId) {
        return new UserAccountImpl(userId);
    }

    @Override
    public UserAccount addFunds(long userId, double amount) {
        Util.validateId(userId);
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        LOGGER.log(Level.DEBUG, "Start adding funds: {} to userId: {}", amount, userId);

        try {
            Optional<UserAccountImpl> optionalAccount = userAccountRepository.findByUserId(userId);

            UserAccount userAccount = optionalAccount.orElseThrow(() -> {
                LOGGER.log(Level.WARN, "User account not found for userId: {}", userId);
                return new DbException("User account not found for userId: " + userId);
            });

            double newBalance = userAccount.getBalance() + amount;
            userAccount.setBalance(newBalance);

            userAccountRepository.save((UserAccountImpl) userAccount);
            LOGGER.log(Level.DEBUG, "Successfully added {} to userId: {}. New balance: {}", amount, userId, newBalance);
            return userAccount;
        } catch (DbException exception) {
            throw exception;
        } catch (Exception exception) {
            LOGGER.log(Level.ERROR, "Cannot add funds to userId: {}", userId, exception);
            throw new DbException("Cannot add funds to userId " + userId, exception);
        }
    }


    @Override
    public UserAccount getUserAccountByUserId(long userId) {
        Util.validateId(userId);
        LOGGER.log(Level.DEBUG, "Start getting a user account with userId: {}", userId);

            UserAccountImpl userAccount = userAccountRepository.findByUserId(userId)
                    .orElseThrow(() -> {
                        LOGGER.log(Level.WARN, "User account not found for userId {}", userId);
                        return new DbException("User account not found for userId: " + userId);
                    });

               LOGGER.log(Level.DEBUG, "Successfully getting userAccount for userId: {}", userId);
               return userAccount;
    }


    @Override
    public UserAccount updateUserAccount(UserAccount userAccount) {
        Util.validateNotNull(userAccount, "UserAccount");
        LOGGER.log(Level.DEBUG,
                "Start updating user account with userId: {}", userAccount.getUserId());

        try {
            UserAccountImpl userAccountToUpdate = userAccountRepository
                    .findByUserId(userAccount.getUserId())
                    .orElseThrow(() -> {
                        LOGGER.log(Level.WARN,
                                "User account to update not found for userId {}", userAccount.getUserId());
                        return new DbException("User account to update not found for userId: " + userAccount.getUserId());
                    });

            userAccountToUpdate.setBalance(userAccount.getBalance());
            userAccountRepository.save(userAccountToUpdate);

            LOGGER.log(Level.DEBUG,
                    "Successfully updated userAccount for userId: {}", userAccountToUpdate.getUserId());

            return userAccountToUpdate;
        } catch (Exception exception) {

            LOGGER.log(Level.WARN,
                    "Cannot update user account for userId {}", userAccount.getUserId(), exception);

            throw new DbException("Error while updating user account", exception);
        }
    }

    @Override
    public boolean deleteUserAccount(long userId) {
        Util.validateId(userId);
        if (userId <= 0) {
            throw new IllegalArgumentException("UserId must be positive");
        }

        LOGGER.debug("Start deleting user account for userId {}", userId);

        UserAccountImpl account = userAccountRepository
                .findByUserId(userId)
                .orElseThrow(() -> new DbException("User account not found for userId: " + userId));

        userAccountRepository.delete(account);

        LOGGER.debug("Successfully deleted user account for userId {}", userId);

        return true;
    }
}
