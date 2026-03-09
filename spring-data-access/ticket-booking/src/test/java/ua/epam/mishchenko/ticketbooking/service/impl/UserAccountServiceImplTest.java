package ua.epam.mishchenko.ticketbooking.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ua.epam.mishchenko.ticketbooking.dao.UserAccountDAO;
import ua.epam.mishchenko.ticketbooking.exception.DbException;
import ua.epam.mishchenko.ticketbooking.model.UserAccount;
import ua.epam.mishchenko.ticketbooking.model.impl.UserAccountImpl;

import java.text.ParseException;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserAccountServiceImplTest {
    private UserAccountServiceImpl userAccountService;

    @Mock
    private UserAccountDAO userAccountDAO;


    @Before
    public void setUp() {
        userAccountService = new UserAccountServiceImpl();
        userAccountService.setUserAccountDAO(userAccountDAO);
    }

    @Test
    public void getUserAccountByUserIdWithExistsIdShouldBeOk() {
        long userId = 3L;
        UserAccount expectedUserAccount = new UserAccountImpl(userId);

        when(userAccountDAO.getByUserId(userId)).thenReturn(expectedUserAccount);

        UserAccount actualUserAccount = userAccountService.getUserAccountByUserId(userId);

        assertEquals(expectedUserAccount, actualUserAccount);
    }

    @Test
    public void getUserAccountByIdWithExceptionShouldReturnNull() {
        when(userAccountDAO.getByUserId(anyLong())).thenThrow(DbException.class);

        UserAccount actualUserAccount = userAccountService.getUserAccountByUserId(10L);

        assertNull(actualUserAccount);
    }

    @Test
    public void createUserAccountWithExceptionShouldReturnNull() {
        when(userAccountDAO.insert(any())).thenThrow(DbException.class);

        UserAccount actualUserAccount = userAccountService.createUserAccount(-1);

        assertNull(actualUserAccount);
    }

    @Test
    public void createUserAccountWithExistsUserIdShouldReturnNull() throws ParseException {
        long userId = 3L;

        when(userAccountDAO.insert(any(UserAccount.class)))
                .thenThrow(new DbException("Account exists"));

        UserAccount real = userAccountService.createUserAccount(userId);

        assertNull(real);
    }

    @Test
    public void createUserAccountWithExistsUserIdShouldReturnUserAccount() throws ParseException {
        long userId = 3L;
        UserAccount expected = new UserAccountImpl(userId);
        when(userAccountDAO.insert(expected)).thenReturn(expected);

        UserAccount real = userAccountService.createUserAccount(expected.getUserId());
        assertEquals(expected, real);
    }

    @Test
    public void updateEventWithExistsEventShouldBeOk() throws ParseException {
        UserAccount expectedUserAccount = new UserAccountImpl(1L);

        when(userAccountDAO.update(any())).thenReturn(expectedUserAccount);

        UserAccount actualUserAccount = userAccountDAO.update(expectedUserAccount);

        assertEquals(expectedUserAccount, actualUserAccount);
    }

    @Test
    public void updateUserAccountWithExceptionShouldReturnNull() {
        when(userAccountDAO.update(any())).thenThrow(DbException.class);

        UserAccount actualUserAccount = userAccountService.updateUserAccount(new UserAccountImpl());

        assertNull(actualUserAccount);
    }

    @Test
    public void deleteUserAccountExistsShouldReturnTrue() {
        when(userAccountDAO.delete(anyLong())).thenReturn(true);

        boolean actualIsDeleted = userAccountService.deleteUserAccount(6L);

        assertTrue(actualIsDeleted);
    }

    @Test
    public void deleteEventWithExceptionShouldReturnFalse() {
        when(userAccountDAO.delete(anyLong())).thenThrow(DbException.class);

        boolean actualIsDeleted = userAccountService.deleteUserAccount(10L);

        assertFalse(actualIsDeleted);
    }
}
