package tests;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.epam.mishchenko.ticketbooking.dao.UserDAO;
import ua.epam.mishchenko.ticketbooking.exception.DbException;
import ua.epam.mishchenko.ticketbooking.model.impl.UserAccount;
import ua.epam.mishchenko.ticketbooking.model.impl.User;
import ua.epam.mishchenko.ticketbooking.model.repository.UserAccountRepository;
import ua.epam.mishchenko.ticketbooking.service.impl.UserAccountServiceImpl;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
public class UserAccountServiceTest {


    @Mock
    private UserDAO userDAO;

    @Mock
    private UserAccountRepository userAccountRepository;

    @InjectMocks
    private UserAccountServiceImpl userAccountService;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createUserAccount_success() {
        long userId = 1L;
        User mockUser = mock(User.class);

        when(userDAO.getById(userId)).thenReturn(Optional.of(mockUser));

        UserAccount result = userAccountService.createUserAccount(userId);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        verify(userAccountRepository, times(1)).save(any(UserAccount.class));
    }

    @Test
    public void createUserAccountShouldThrowExceptionWhenUserNotFound() {
        long userId = 1L;

        when(userDAO.getById(userId)).thenReturn(Optional.empty());

        assertThrows(DbException.class, () -> {
            userAccountService.createUserAccount(userId);
        });

        verify(userAccountRepository, never()).save(any());
    }

    @Test
    public void addFundsShouldIncreaseBalance() {
        long userId = 1L;
        double amount = 50.0;

        UserAccount account = new UserAccount(userId, 100.0);

        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(account));

        UserAccount result = userAccountService.addFunds(userId, amount);

        assertEquals(150.0, result.getBalance(), 0.001);

        verify(userAccountRepository).save(account);
    }

    @Test
    public void addFundsShouldThrowWhenAmountIsNegative() {
        long userId = 1L;
        double amount = -10;

        assertThrows(IllegalArgumentException.class, () -> {
            userAccountService.addFunds(userId, amount);
        });

        verify(userAccountRepository, never()).findById(anyLong());
    }

    @Test
    public void addFundsShouldThrowWhenAccountNotFound() {
        long userId = 1L;
        double amount = 50;

        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(DbException.class, () -> {
            userAccountService.addFunds(userId, amount);
        });

        verify(userAccountRepository, never()).save(any());
    }

    @Test
    public void addFundsShouldThrowWhenRepositoryFails() {
        long userId = 1L;
        double amount = 50;

        UserAccount account = new UserAccount(userId, 100);

        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(account));

        doThrow(new RuntimeException("DB error"))
                .when(userAccountRepository).save(any());

        assertThrows(DbException.class, () -> {
            userAccountService.addFunds(userId, amount);
        });
    }

    @Test
    public void getUserAccountByUserIdShouldReturnAccount() {
        long userId = 1L;

        UserAccount account = new UserAccount(userId, 100);

        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(account));

        UserAccount result = userAccountService.getUserAccountByUserId(userId);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(100, result.getBalance(), 0.001);

        verify(userAccountRepository).findById(userId);
    }

    @Test
    public void getUserAccountByUserIdShouldThrowWhenAccountNotFound() {
        long userId = 1L;

        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(DbException.class, () -> {
            userAccountService.getUserAccountByUserId(userId);
        });

        verify(userAccountRepository).findById(userId);
    }

    @Test
    public void getUserAccountByUserIdShouldThrowWhenIdInvalid() {
        long userId = -1;

        assertThrows(IllegalArgumentException.class, () -> {
            userAccountService.getUserAccountByUserId(userId);
        });

        verify(userAccountRepository, never()).findById(anyLong());
    }

    @Test
    public void updateUserAccountShouldUpdateBalanceSuccessfully() {
        long userId = 1L;
        UserAccount existingAccount = new UserAccount(userId, 100.0);
        UserAccount update = new UserAccount(userId, 200.0);

        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(existingAccount));

        UserAccount result = userAccountService.updateUserAccount(update);

        assertNotNull(result);
        assertEquals(200.0, result.getBalance(), 0.001);

        verify(userAccountRepository).findById(userId);
        verify(userAccountRepository).save(existingAccount);
    }

    @Test
    public void updateUserAccountShouldThrowWhenUserAccountIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            userAccountService.updateUserAccount(null);
        });

        verify(userAccountRepository, never()).findById(anyLong());
        verify(userAccountRepository, never()).save(any());
    }

    @Test
    public void updateUserAccountShouldThrowWhenAccountNotFound() {
        long userId = 1L;
        UserAccount update = new UserAccount(userId, 200.0);

        when(userAccountRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(DbException.class, () -> {
            userAccountService.updateUserAccount(update);
        });

        verify(userAccountRepository).findById(userId);
        verify(userAccountRepository, never()).save(any());
    }

    @Test
    public void updateUserAccountShouldThrowWhenSaveFails() {
        long userId = 1L;
        UserAccount existingAccount = new UserAccount(userId, 100.0);
        UserAccount updateToAccount = new UserAccount(userId, 200.0);

        when(userAccountRepository.findById(userId)).thenReturn(Optional.of(existingAccount));
        doThrow(new RuntimeException("DB error")).when(userAccountRepository).save(existingAccount);

        assertThrows(DbException.class, () -> {
            userAccountService.updateUserAccount(updateToAccount);
        });

        verify(userAccountRepository).findById(userId);
        verify(userAccountRepository).save(existingAccount);
    }

    @Test
    public void deleteUserAccountShouldReturnTrueWhenAccountExists() throws DbException {
        long userId = 1L;
        UserAccount account = new UserAccount(userId, 100.0);

        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(account));

        boolean result = userAccountService.deleteUserAccount(userId);

        assertTrue(result);

        verify(userAccountRepository).findById(userId);
        verify(userAccountRepository).delete(account);
    }

    @Test
    public void deleteUserAccountShouldThrowWhenUserIdInvalid() {
        long invalidUserId = -1;

        assertThrows(IllegalArgumentException.class, () -> {
            userAccountService.deleteUserAccount(invalidUserId);
        });

        verify(userAccountRepository, never()).findById(anyLong());
        verify(userAccountRepository, never()).delete(any());
    }

    @Test
    public void deleteUserAccountShouldThrowWhenAccountNotFound() {
        long userId = 1L;

        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(DbException.class, () -> {
            userAccountService.deleteUserAccount(userId);
        });

        verify(userAccountRepository).findById(userId);
        verify(userAccountRepository, never()).delete(any());
    }
}
