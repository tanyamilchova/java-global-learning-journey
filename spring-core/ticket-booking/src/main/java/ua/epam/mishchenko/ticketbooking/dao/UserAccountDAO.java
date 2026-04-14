package ua.epam.mishchenko.ticketbooking.dao;

import org.springframework.stereotype.Repository;
import ua.epam.mishchenko.ticketbooking.model.UserAccount;

@Repository
public interface UserAccountDAO {
    UserAccount getById(long id);

    UserAccount update(UserAccount userAccount);

    UserAccount insert(UserAccount userAccount);

    boolean delete (long userId);

    UserAccount getByUserId(long userId);
}
