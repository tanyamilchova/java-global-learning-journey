package ua.epam.mishchenko.ticketbooking.dao.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import ua.epam.mishchenko.ticketbooking.dao.UserDAO;
import ua.epam.mishchenko.ticketbooking.exception.DbException;
import ua.epam.mishchenko.ticketbooking.model.User;
import ua.epam.mishchenko.ticketbooking.model.impl.UserImpl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    private static final Logger LOGGER = LogManager.getLogger(UserDAOImpl.class);

    @Override
    public User getById(long id) throws DbException {
        if(id <= 0){
            throw new IllegalArgumentException("User id must be positive");
        }
        try {
            LOGGER.log(Level.DEBUG, "Start retrieving  user by id: {}", id);
            return entityManager.find(UserImpl.class, id);
        } catch (DbException exception) {
            LOGGER.log(Level.DEBUG, "Error while retrieving  user by id: {}", id);

            throw new DbException(exception.getMessage());
        }
    }

    @Override
    public List<User> getAll(int pageSize, int pageNum) throws DbException {
        try {
            return entityManager.createQuery(
                    "SELECT u FROM UserImpl u", User.class)
                    .setFirstResult((pageNum - 1) * pageSize)
                    .setMaxResults(pageSize).getResultList();
        } catch (Exception e) {
            throw new DbException("Error retrieving all users", e);
        }
    }

    @Override
    public User getByEmail(String email) throws DbException {
        try {
            return entityManager.createQuery(
                            "SELECT u FROM UserImpl u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (Exception e) {
            throw new DbException("Error finding user by email", e);
        }
    }

    @Override
    public List<User> getByName(String name, int pageSize, int pageNum) throws DbException {
        try {
            return entityManager.createQuery(
                            "SELECT u FROM UserImpl u WHERE u.name LIKE :name", User.class)
                    .setParameter("name", "%" + name + "%")
                    .setFirstResult((pageNum - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();
        } catch (Exception e) {
            throw new DbException("Error finding users by name", e);
        }
    }

    @Override
    public User insert(User user) throws DbException {
        try {
            entityManager.persist(user);
            return user;
        } catch (Exception e) {
            throw new DbException("Error inserting user", e);

        }
    }

    @Override
    public User update(User user) throws DbException {
        try {
            return entityManager.merge(user);
        } catch (Exception e) {
            throw new DbException("Error updating user", e);
        }
    }

    @Override
    public boolean delete(long id) throws DbException {
        try {
            User user = getById(id);
            if (user != null) {
                entityManager.remove(user);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new DbException("Error deleting user", e);
        }
    }
}
