package ua.epam.mishchenko.ticketbooking.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.epam.mishchenko.ticketbooking.dao.UserDAO;
import ua.epam.mishchenko.ticketbooking.exception.DbException;
import ua.epam.mishchenko.ticketbooking.model.impl.UserAccountImpl;
import ua.epam.mishchenko.ticketbooking.model.impl.UserImpl;
import ua.epam.mishchenko.ticketbooking.model.repository.UserAccountRepository;
import ua.epam.mishchenko.ticketbooking.service.UserAccountService;
import ua.epam.mishchenko.ticketbooking.validator.GenericValidator;
import ua.epam.mishchenko.ticketbooking.validator.UserAccountValidator;

import java.util.Optional;

@Service
public class UserAccountServiceImpl implements UserAccountService {
    private static final Logger LOGGER = LogManager.getLogger(UserAccountServiceImpl.class);

    private final UserDAO userDAO;
    private final UserAccountRepository userAccountRepository;
    private final UserAccountValidator userAccountValidator;
    private final GenericValidator genericValidator;

    @Autowired
    public UserAccountServiceImpl(
            UserDAO userDAO,
            UserAccountRepository userAccountRepository,
            UserAccountValidator userAccountValidator,
            GenericValidator genericValidator
    ) {
        this.userDAO = userDAO;
        this.userAccountRepository = userAccountRepository;
        this.userAccountValidator = userAccountValidator;
        this.genericValidator = genericValidator;
    }

    @Override
    public UserAccountImpl createUserAccount(long userId) {
        genericValidator.validateId(userId, "User id");
        LOGGER.debug("Start creating a user account with userId: {}", userId);

        Optional<UserImpl> userOpt = userDAO.getById(userId);
        if (userOpt.isEmpty()) {
            LOGGER.warn("User with id {} not found", userId);
            throw new DbException("User not found: " + userId);
        }

        try{
            UserAccountImpl userAccount = createNewUserAccount(userId);
            userAccountRepository.save((UserAccountImpl)userAccount);

            LOGGER.info("Successfully created userAccount for userId: {}", userId);
            return userAccount;

        } catch (Exception e) {
            LOGGER.error("Cannot create user account for userId {}", userId, e);
            throw new DbException("Cannot create user account for userId " + userId, e);
        }
    }

    private UserAccountImpl createNewUserAccount(long userId) {
        return new UserAccountImpl(userId);
    }

    @Override
    public UserAccountImpl addFunds(long userId, double amount) {

        genericValidator.validateId(userId, "User id");
        genericValidator.validateFunds(amount);
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        LOGGER.debug("Start adding funds: {} to userId: {}", amount, userId);

        try {
            Optional<UserAccountImpl> optionalAccount = userAccountRepository.findById(userId);

            UserAccountImpl userAccount = optionalAccount.orElseThrow(() -> {
                LOGGER.warn("User account not found for userId: {}", userId);
                return new DbException("User account not found for userId: " + userId);
            });

            double newBalance = userAccountValidator.updateBalance(userAccount, amount);

            userAccountRepository.save( userAccount);
            LOGGER.info("Successfully added {} to userId: {}. New balance: {}", amount, userId, newBalance);

            return userAccount;
        } catch (DbException exception) {
            throw exception;
        } catch (Exception exception) {
            LOGGER.error("Cannot add funds to userId: {}", userId, exception);
            throw new DbException("Cannot add funds to userId " + userId, exception);
        }
    }


    @Override
    public UserAccountImpl getUserAccountByUserId(long userId) {
        genericValidator.validateId(userId, "User id");
        LOGGER.debug("Start getting a user account with userId: {}", userId);

            UserAccountImpl userAccount = userAccountRepository.findById(userId)
                    .orElseThrow(() -> {
                        LOGGER.warn("User account not found for userId {}", userId);
                        return new DbException("User account not found for userId: " + userId);
                    });
            LOGGER.info("Successfully retrieved userAccount for userId: {}", userId);

            return userAccount;
    }


    @Override
    public UserAccountImpl updateUserAccount(UserAccountImpl userAccount) {
        userAccountValidator.validate(userAccount);
        LOGGER.debug("Start updating user account with userId: {}", userAccount.getUserId());

        try {
            UserAccountImpl userAccountToUpdate = userAccountRepository
                    .findById(userAccount.getUserId())
                    .orElseThrow(() -> {

                        LOGGER.warn("User account to update not found for userId {}", userAccount.getUserId());
                        return new DbException("User account to update not found for userId: " + userAccount.getUserId());
                    });

            userAccountToUpdate.setBalance(userAccount.getBalance());
            userAccountRepository.save(userAccountToUpdate);

            LOGGER.info("Successfully updated userAccount for userId: {}", userAccountToUpdate.getUserId());

            return userAccountToUpdate;
        } catch (Exception exception) {

            LOGGER.error("Cannot update user account for userId {}", userAccount.getUserId(), exception);
            throw new DbException("Error while updating user account", exception);
        }
    }

    @Override
    public boolean deleteUserAccount(long userId) {

        genericValidator.validateId(userId, "User id");
        if (userId <= 0) {
            LOGGER.warn("Attempted to delete user account with invalid userId: {}", userId);
            throw new IllegalArgumentException("UserId must be positive");
        }

        LOGGER.debug("Start deleting user account for userId {}", userId);

        UserAccountImpl account = userAccountRepository
                .findById(userId)
                .orElseThrow(() -> {
                    LOGGER.warn("No user account exists for userId: {}. Deletion aborted.", userId);
                    return new DbException("User account not found for userId: " + userId);
                });

        userAccountRepository.delete(account);

        LOGGER.info("Successfully deleted user account for userId {}", userId);

        return true;
    }
}
