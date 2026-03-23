package ua.epam.mishchenko.ticketbooking.validator;

import ua.epam.mishchenko.ticketbooking.model.impl.UserAccountImpl;

public class Util {


    public static <T> void validateNotNull(T resource, String resourceName) {
        if (resource == null) {
            throw new IllegalArgumentException(resourceName + " cannot be null");
        }
    }


    public static double updateBalance(UserAccountImpl userAccount, double delta) {
        double newBalance = userAccount.getBalance() + delta;
        if (newBalance < 0) {
            throw new IllegalArgumentException("Insufficient funds: resulting balance would be negative");
        }
        userAccount.setBalance(newBalance);
        return newBalance;
    }
}
