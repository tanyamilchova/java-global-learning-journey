package ua.epam.mishchenko.ticketbooking.validator;

import org.springframework.stereotype.Component;
import ua.epam.mishchenko.ticketbooking.model.impl.UserAccountImpl;

@Component
public class BalanceValidator {

    public double updateBalance(UserAccountImpl userAccount, double delta) {
        double newBalance = userAccount.getBalance() + delta;
        if (newBalance < 0) {
            throw new IllegalArgumentException("Insufficient funds: resulting balance would be negative");
        }
        userAccount.setBalance(newBalance);
        return newBalance;
    }
}

