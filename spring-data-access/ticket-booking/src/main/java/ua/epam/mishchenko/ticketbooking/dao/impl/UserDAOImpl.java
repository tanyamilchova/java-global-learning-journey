package ua.epam.mishchenko.ticketbooking.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import ua.epam.mishchenko.ticketbooking.dao.UserDAO;
import ua.epam.mishchenko.ticketbooking.exception.DbException;
import ua.epam.mishchenko.ticketbooking.model.User;
import ua.epam.mishchenko.ticketbooking.model.impl.UserImpl;
import ua.epam.mishchenko.ticketbooking.utils.Util;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDAOImpl implements UserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    private static final Logger LOGGER = LogManager.getLogger(UserDAOImpl.class);


    @Override
    public Optional<User> getById(long id) throws DbException {

        Util.validateId(id);
        try {
            LOGGER.debug("Start retrieving user by id: {}", id);
            User user = entityManager.find(UserImpl.class, id);
            if (user == null) {
                LOGGER.info("No user found with id: {}", id);
                return Optional.empty();
            }
            return Optional.of(user);
        } catch (DbException exception) {
            LOGGER.error("Error while retrieving user by id: {}", id, exception);

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
    public Optional<User> getByEmail(String email) throws DbException {
        try {
            User user = entityManager.createQuery(
                            "SELECT u FROM UserImpl u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return Optional.ofNullable(user);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            LOGGER.error("Error retrieving user by email: {}", email, e);
            throw new DbException("Error retrieving user by email: " + email, e);
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
            return Collections.emptyList();
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
            Optional<User> user = getById(id);
            if (user.isPresent()) {
                entityManager.remove(user.get());
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new DbException("Error deleting user", e);
        }
    }
}
