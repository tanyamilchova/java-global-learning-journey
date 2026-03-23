package ua.epam.mishchenko.ticketbooking.service;

import ua.epam.mishchenko.ticketbooking.model.impl.UserAccountImpl;

public interface UserAccountService {
    UserAccountImpl createUserAccount(long userId);

    UserAccountImpl getUserAccountByUserId(long userId);

    UserAccountImpl updateUserAccount(UserAccountImpl userAccount);

    boolean deleteUserAccount(long userId);

    UserAccountImpl addFunds(long userId, double funds);
}
