package ua.epam.mishchenko.ticketbooking.validator;

import org.springframework.stereotype.Component;
import ua.epam.mishchenko.ticketbooking.model.impl.UserAccount;

@Component
public class UserAccountValidator {

    public void validate(UserAccount userAccount) {
        if (userAccount == null) {
            throw new IllegalArgumentException("UserAccount cannot be null");
        }
        if (userAccount.getUserId() <= 0) {
            throw new IllegalArgumentException("UserAccount userId must be positive. Provided: " + userAccount.getUserId());
        }
    }

    public  double updateBalance(UserAccount userAccount, double delta) {
        double newBalance = userAccount.getBalance() + delta;
        if (newBalance < 0) {
            throw new IllegalArgumentException("Insufficient funds: resulting balance would be negative");
        }
        userAccount.setBalance(newBalance);
        return newBalance;
    }
}

