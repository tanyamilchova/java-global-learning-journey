package ua.epam.mishchenko.ticketbooking.dao;

import org.springframework.stereotype.Repository;
import ua.epam.mishchenko.ticketbooking.model.User;

import java.util.List;

@Repository
public interface UserDAO {

    User getById(long id);

    List<User> getAll();

    User insert(User user);

    User update(User user);

    boolean delete(long userId);
}
