package ua.epam.mishchenko.ticketbooking.dao;

import ua.epam.mishchenko.ticketbooking.exception.DbException;
import ua.epam.mishchenko.ticketbooking.model.impl.UserImpl;

import java.util.List;
import java.util.Optional;

public interface UserDAO {

    Optional<UserImpl> getById(long id);

    List<UserImpl> getAll(int size, int num);

    Optional<UserImpl> getByEmail(String email) throws DbException;

    List<UserImpl> getByName(String name, int pageSize, int pageNum) throws DbException;

    UserImpl insert(UserImpl user);

    UserImpl update(UserImpl user);

    boolean delete(long userId);
}
