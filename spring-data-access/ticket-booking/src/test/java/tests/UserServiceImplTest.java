package tests;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.epam.mishchenko.ticketbooking.dao.UserDAO;
import ua.epam.mishchenko.ticketbooking.exception.DbException;
import ua.epam.mishchenko.ticketbooking.model.User;
import ua.epam.mishchenko.ticketbooking.model.impl.UserImpl;
import ua.epam.mishchenko.ticketbooking.service.impl.UserServiceImpl;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {
    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private UserServiceImpl userService;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createUserShouldInsertUserWhenEmailNotExists() throws DbException {
        User newUser = new UserImpl();
        newUser.setName("Vala");
        newUser.setEmail("test@example.com");

        when(userDAO.getByEmail(newUser.getEmail())).thenReturn(null);
        when(userDAO.insert(newUser)).thenReturn(newUser);

        User created = userService.createUser(newUser);
        verify(userDAO, times(1)).getByEmail(newUser.getEmail());
        verify(userDAO, times(1)).insert(newUser);

        assertNotNull(created);
    }

    @Test
    public void createUserShouldReturnNullWhenEmailExists() throws DbException {
        User existingUser = new UserImpl();
        existingUser.setName("Vala");
        existingUser.setEmail("test5@example.com");

        when(userDAO.getByEmail(existingUser.getEmail())).thenReturn(null);
        User result = userService.createUser(existingUser);

        assertNull(result);
    }

    @Test
    public void createUserShouldThrowWhenUserInvalid() {

        assertThrows(IllegalArgumentException.class,
                () -> userService.createUser(null));
    }


    @Test
    public void getUserByIdShouldReturnUserWhenExists() throws DbException {
        User user = new UserImpl();
        user.setId(1L);
        user.setName("Alice");
        user.setEmail("alice@example.com");

        when(userDAO.getById(1L)).thenReturn(user);

        User resultUser = userService.getUserById(1L);

        assertEquals(user, resultUser);
        verify(userDAO, times(1)).getById(1L);

    }

    @Test
    public void getUserByIdShouldThrowWhenIdNotExists() throws DbException {
        when(userDAO.getById(1L)).thenThrow(DbException.class);

        assertThrows(DbException.class, () -> {
            userService.getUserById(1L);
        });
        verify(userDAO, times(1)).getById(1L);
    }

    @Test
    public void getAllUsersShouldReturnListWhenUsersExist() throws DbException {
        List<User> users = List.of(
                new UserImpl(1L, "Alice", "alice@example.com"),
                new UserImpl(2L, "Bob", "bob@example.com")
        );
        when(userDAO.getAll(10, 1)).thenReturn(users);

        List<User> result = userService.getAllUsers(10, 1);

        assertEquals(2, result.size());
        assertEquals(users, result);

        verify(userDAO, times(1)).getAll(10, 1);
    }

    @Test
    public void getAllUsersShouldThrowWhenSizeIncorrect() throws DbException {

        when(userDAO.getAll(0, 1)).thenThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class, () -> {
            userService.getAllUsers(0, 1);
        });
    }

    @Test
    public void getAllUsersShouldThrowWhenNumberIncorrect() throws DbException {

        when(userDAO.getAll(0, 1)).thenThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class, () -> {
            userService.getAllUsers(3, -1);
        });
    }


    @Test
    public void getUserByEmailShouldReturnUserWhenExists() throws DbException {
        User user = new UserImpl();
        user.setId(1L);
        user.setName("Alice");
        user.setEmail("alice@example.com");

        when(userDAO.getByEmail(user.getEmail())).thenReturn(user);

        User resultUser = userService.getUserByEmail("alice@example.com");

        assertNotNull(resultUser);
        assertEquals(user.getId(), resultUser.getId());
    }

    @Test
    public void getUserByEmailShouldThrowWhenEmailNotExists() throws DbException {
        String email = "alice@example.com";

        when(userDAO.getByEmail(email)).thenReturn(null);

        User result = userService.getUserByEmail(email);

        assertNull(result);
    }

    @Test
    public void getUserByEmailShouldThrowWhenInvalidEmail() throws DbException {
        when(userDAO.getByEmail(null)).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> {
            userService.getUserByEmail(null);
        });
    }

    @Test
    public void getUsersByNameShouldReturnUsersWhenExist() throws DbException {
        String name = "Alice";
        List<User> users = List.of(
                new UserImpl(1L, "Alice", "alice@mail.com"),
                new UserImpl(2L, "Alice", "alice2@mail.com")
        );

        when(userDAO.getByName(name, 10, 1)).thenReturn(users);

        List<User> resultUsers = userService.getUsersByName(name, 10, 1);

        assertNotNull(resultUsers);
        assertEquals(2, resultUsers.size());
        assertEquals(users, resultUsers);

        verify(userDAO, times(1)).getByName(name, 10, 1);

    }

    @Test
    public void getUsersByNameShouldReturnNullWhenDaoThrows() throws DbException {
        when(userDAO.getByName("Alice", 10, 1))
                .thenThrow(new DbException("DB error"));
        assertThrows(DbException.class, () -> {
            userService.getUsersByName("Alice", 10, 1);
        });
    }


    @Test
    public void getUsersByNameShouldThrowWhenNotValidPageSize() throws DbException {
        String name = "Alice";

        when(userDAO.getByName(name, 0, 1)).thenThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class, () -> {
            userService.getUsersByName(name, 0, 1);
        });
    }

    @Test
    public void getUsersByNameShouldThrowWhenNotValidNumber() throws DbException {
        String name = "Alice";

        when(userDAO.getByName(name, 10, -1)).thenThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class, () -> {
            userService.getUsersByName(name, 10, -1);
        });
    }


    @Test
    public void updateUserShouldReturnUpdatedUser() throws DbException {
        User user = new UserImpl();
        user.setId(1L);
        user.setName("Alice");
        user.setEmail("alice@example.com");

        when(userDAO.update(user)).thenReturn(user);

        User result = userService.updateUser(user);

        assertNotNull(result);
        assertEquals(user, result);

        verify(userDAO, times(1)).update(user);
    }

    @Test
    public void updateUserShouldThrowWhenUserNotExist() throws DbException {
        User user = new UserImpl();
        user.setId(1L);
        user.setName("Alice");
        user.setEmail("alice@example.com");

        when(userDAO.update(user)).thenThrow(DbException.class);

        assertThrows(DbException.class, () -> {
            userService.updateUser(user);
        });
    }

    @Test
    public void updateUserShouldThrowWhenUserInvalid() {

        assertThrows(IllegalArgumentException.class, () ->
                userService.updateUser(null));
    }


    @Test
    public void deleteUserShouldReturnTrueWhenUserDeleted() throws DbException {
        when(userDAO.delete(1L)).thenReturn(true);

        boolean result = userService.deleteUser(1L);

        assertTrue(result);
        verify(userDAO).delete(1L);
    }

    @Test
    public void deleteUserShouldReturnFalseWhenUserNotExists() throws DbException {
        when(userDAO.delete(1L)).thenReturn(false);

        boolean result = userService.deleteUser(1L);

        assertFalse(result);
        verify(userDAO).delete(1L);
    }
}
