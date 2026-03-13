package ua.epam.mishchenko.ticketbooking.dao;

import ua.epam.mishchenko.ticketbooking.exception.DbException;
import ua.epam.mishchenko.ticketbooking.model.User;

import java.util.List;

public interface UserDAO {

    User getById(long id);

    List<User> getAll(int size, int num);

    User getByEmail(String email) throws DbException;

    List<User> getByName(String name, int pageSize, int pageNum) throws DbException;

    User insert(User user);

    User update(User user);

    boolean delete(long userId);
}
