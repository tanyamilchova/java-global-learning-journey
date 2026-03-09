package ua.epam.mishchenko.ticketbooking.service;

import ua.epam.mishchenko.ticketbooking.model.UserAccount;

public interface UserAccountService {
    UserAccount createUserAccount(long userId);

    UserAccount getUserAccountByUserId(long userId);

    UserAccount updateUserAccount(UserAccount userAccount);

    boolean deleteUserAccount(long userId);
}
