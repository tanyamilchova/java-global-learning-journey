package ua.epam.mishchenko.ticketbooking.dao;

import ua.epam.mishchenko.ticketbooking.model.UserAccount;

public interface UserAccountDAO {
    UserAccount getById(long id);

    UserAccount update(UserAccount userAccount);

    UserAccount insert(UserAccount userAccount);

    boolean delete (long userId);

    UserAccount getByUserId(long userId);
}
