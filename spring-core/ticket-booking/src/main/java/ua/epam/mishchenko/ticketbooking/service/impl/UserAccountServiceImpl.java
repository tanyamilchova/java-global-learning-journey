package ua.epam.mishchenko.ticketbooking.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.epam.mishchenko.ticketbooking.dao.UserDAO;
import ua.epam.mishchenko.ticketbooking.exception.DbException;
import ua.epam.mishchenko.ticketbooking.model.impl.User;
import ua.epam.mishchenko.ticketbooking.model.impl.UserAccount;
import ua.epam.mishchenko.ticketbooking.model.repository.UserAccountRepository;
import ua.epam.mishchenko.ticketbooking.service.UserAccountService;
import ua.epam.mishchenko.ticketbooking.validator.GenericValidator;
import ua.epam.mishchenko.ticketbooking.validator.UserAccountValidator;

import java.util.Optional;

@Log4j2
@Service
public class UserAccountServiceImpl implements UserAccountService {

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
    public UserAccount createUserAccount(long userId) {
        genericValidator.validateId(userId, "User id");
        log.debug("Start creating a user account with userId: {}", userId);

        Optional<User> userOpt = userDAO.getById(userId);
        if (userOpt.isEmpty()) {
            log.warn("User with id {} not found", userId);
            throw new DbException("User not found: " + userId);
        }

        try{
            UserAccount userAccount = createNewUserAccount(userId);
            userAccountRepository.save((UserAccount)userAccount);

            log.info("Successfully created userAccount for userId: {}", userId);
            return userAccount;

        } catch (Exception e) {
            log.error("Cannot create user account for userId {}", userId, e);
            throw new DbException("Cannot create user account for userId " + userId, e);
        }
    }

    private UserAccount createNewUserAccount(long userId) {
        return new UserAccount(userId);
    }

    @Override
    public UserAccount addFunds(long userId, double amount) {

        genericValidator.validateId(userId, "User id");
        genericValidator.validateFunds(amount);
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        log.debug("Start adding funds: {} to userId: {}", amount, userId);

        try {
            Optional<UserAccount> optionalAccount = userAccountRepository.findById(userId);

            UserAccount userAccount = optionalAccount.orElseThrow(() -> {
                log.warn("User account not found for userId: {}", userId);
                return new DbException("User account not found for userId: " + userId);
            });

            double newBalance = userAccountValidator.updateBalance(userAccount, amount);

            userAccountRepository.save( userAccount);
            log.info("Successfully added {} to userId: {}. New balance: {}", amount, userId, newBalance);

            return userAccount;
        } catch (DbException exception) {
            throw exception;
        } catch (Exception exception) {
            log.error("Cannot add funds to userId: {}", userId, exception);
            throw new DbException("Cannot add funds to userId " + userId, exception);
        }
    }


    @Override
    public UserAccount getUserAccountByUserId(long userId) {
        genericValidator.validateId(userId, "User id");
        log.debug("Start getting a user account with userId: {}", userId);

            UserAccount userAccount = userAccountRepository.findById(userId)
                    .orElseThrow(() -> {
                        log.warn("User account not found for userId {}", userId);
                        return new DbException("User account not found for userId: " + userId);
                    });
        log.info("Successfully retrieved userAccount for userId: {}", userId);

            return userAccount;
    }


    @Override
    public UserAccount updateUserAccount(UserAccount userAccount) {
        userAccountValidator.validate(userAccount);
        log.debug("Start updating user account with userId: {}", userAccount.getUserId());

        try {
            UserAccount userAccountToUpdate = userAccountRepository
                    .findById(userAccount.getUserId())
                    .orElseThrow(() -> {

                        log.warn("User account to update not found for userId {}", userAccount.getUserId());
                        return new DbException("User account to update not found for userId: " + userAccount.getUserId());
                    });

            userAccountToUpdate.setBalance(userAccount.getBalance());
            userAccountRepository.save(userAccountToUpdate);

            log.info("Successfully updated userAccount for userId: {}", userAccountToUpdate.getUserId());

            return userAccountToUpdate;
        } catch (Exception exception) {

            log.error("Cannot update user account for userId {}", userAccount.getUserId(), exception);
            throw new DbException("Error while updating user account", exception);
        }
    }

    @Override
    public boolean deleteUserAccount(long userId) {

        genericValidator.validateId(userId, "User id");
        if (userId <= 0) {
            log.warn("Attempted to delete user account with invalid userId: {}", userId);
            throw new IllegalArgumentException("UserId must be positive");
        }

        log.debug("Start deleting user account for userId {}", userId);

        UserAccount account = userAccountRepository
                .findById(userId)
                .orElseThrow(() -> {
                    log.warn("No user account exists for userId: {}. Deletion aborted.", userId);
                    return new DbException("User account not found for userId: " + userId);
                });

        userAccountRepository.delete(account);

        log.info("Successfully deleted user account for userId {}", userId);

        return true;
    }
}
