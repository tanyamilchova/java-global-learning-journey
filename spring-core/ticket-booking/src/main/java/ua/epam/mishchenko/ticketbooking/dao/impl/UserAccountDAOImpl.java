package ua.epam.mishchenko.ticketbooking.dao.impl;

import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.epam.mishchenko.ticketbooking.dao.UserAccountDAO;
import ua.epam.mishchenko.ticketbooking.db.Storage;
import ua.epam.mishchenko.ticketbooking.exception.DbException;
import ua.epam.mishchenko.ticketbooking.model.UserAccount;
import ua.epam.mishchenko.ticketbooking.model.impl.UserAccountImpl;

import java.util.Map;

@Setter
public class UserAccountDAOImpl implements UserAccountDAO {

    private static final Logger logger = LoggerFactory.getLogger(UserAccountDAOImpl.class);

    private static final String NAMESPACE = "user_account:";

    private Storage storage;

    @Override
    public UserAccount getById(long id) {
        logger.debug("Finding a user_account by id: {}", id);

        String stringUserAccount = storage.getInMemoryStorage().get(NAMESPACE + id);
        if (stringUserAccount == null) {
            logger.warn("Can not to find a user_account by id: {}", id);
            throw new DbException("Can not to find a user_account by id: " + id);
        }

        UserAccount userAccount = parseFromStringToUserAccount(stringUserAccount);

        logger.debug("The user_account with id {} successfully found ", id);
        return userAccount;
    }

    private UserAccount createUserAccountFromStringFields(String[] stringFields) {
        int index = 0;
        UserAccount userAccount = new UserAccountImpl();
        userAccount.setUserId(
                Long.parseLong(getFieldValueFromFields(stringFields, index++))
        );
        userAccount.setBalance(
                Double.parseDouble(getFieldValueFromFields(stringFields, index))
        );

        return userAccount;
    }

    private String getFieldValueFromFields(String[] stringFields, int index) {
        final String delimiterBetweenKeyAndValue = "=";
        return stringFields[index]
                .split(delimiterBetweenKeyAndValue)[1]
                .trim();
    }

    @Override
    public UserAccount update(UserAccount userAccount) {
        logger.debug("Start updating of the user account: {}", userAccount);

        if (userAccount == null) {
            throw new DbException("The user account can not equal null");
        }

        if (!isUserAccountExists(userAccount.getUserId())) {
            throw new DbException("The user account with user id "
                    + userAccount.getUserId() + " does not exist");
        }

        storage.getInMemoryStorage().replace(
                NAMESPACE + userAccount.getUserId(),
                userAccount.toString()
        );

        logger.debug("Successfully updated user account: {}", userAccount);

        return userAccount;
    }

    private boolean isUserAccountExists(long userId) {
        return storage.getInMemoryStorage()
                .containsKey(NAMESPACE + userId);
    }

    @Override
    public UserAccount insert(UserAccount userAccount) {
        logger.debug("Start inserting of the user account: {}", userAccount);

        if (userAccount == null) {
            throw new DbException("The user account cannot be null");
        }

        if (isUserAccountExists(userAccount.getUserId())) {
            throw new DbException("User account for user id "
                    + userAccount.getUserId() + " already exists");
        }

        storage.getInMemoryStorage().put(
                NAMESPACE + userAccount.getUserId(),
                userAccount.toString()
        );

        logger.debug("Successfully inserted user account: {}", userAccount);

        return userAccount;
    }

    @Override
    public boolean delete(long userId) {
        logger.debug("Start deleting of the user account with user id: {}", userId);

        if (!isUserAccountExists(userId)) {
            throw new DbException("The user account with user id " + userId + " does not exist");
        }

        String removedAccount = storage.getInMemoryStorage().remove(NAMESPACE + userId);

        if (removedAccount == null) {
            throw new DbException("The user account with user id " + userId + " was not deleted");
        }

        logger.debug("Successfully deleted user account with user id: {}", userId);

        return true;
    }

    @Override
    public UserAccount getByUserId(long userId) {
        logger.debug("Start retrieving  user account for user id: {}", userId);
        if (!isUserAccountExists(userId)) {
            throw new DbException("The user account with user id " + userId + " does not exist");
        }
        Map<String, String> getStorage = storage.getInMemoryStorage();

        return parseFromStringToUserAccount(getStorage.get(NAMESPACE + userId));
    }

    private UserAccount parseFromStringToUserAccount(String stringUserAccount) {
        final String delimiterBetweenFields = ", ";
        stringUserAccount = removeBrackets(stringUserAccount);
        String[] stringFields = stringUserAccount.split(delimiterBetweenFields);
        return createUserAccountFromStringFields(stringFields);
    }

    private String removeBrackets(String text) {
        text = text.replace("{", "");
        return text.replace("}", "");
    }
}