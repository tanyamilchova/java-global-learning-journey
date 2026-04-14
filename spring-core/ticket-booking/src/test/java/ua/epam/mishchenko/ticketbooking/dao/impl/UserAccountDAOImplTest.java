package ua.epam.mishchenko.ticketbooking.dao.impl;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ua.epam.mishchenko.ticketbooking.db.Storage;
import ua.epam.mishchenko.ticketbooking.exception.DbException;
import ua.epam.mishchenko.ticketbooking.model.UserAccount;
import ua.epam.mishchenko.ticketbooking.model.impl.UserAccountImpl;
import ua.epam.mishchenko.ticketbooking.postprocessor.FileReader;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@ContextConfiguration({"classpath:/test-applicationContext.xml"})
public class UserAccountDAOImplTest {

    private UserAccountDAOImpl userAccountDAO;

    @Mock
    private Storage storage;

    @Autowired
    private FileReader fileReader;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        Map<String, String> inMemoryStorage = fileReader.readPreparedDataFromFile();
        storage.setInMemoryStorage(inMemoryStorage);
        userAccountDAO = new UserAccountDAOImpl();
        userAccountDAO.setStorage(storage);

        when(storage.getInMemoryStorage()).thenReturn(inMemoryStorage);
    }

    @Test
    public void getByIdWithExistsIdShouldBeOk() {
        UserAccount userAccount = new UserAccountImpl(3L, 5000000);
        userAccountDAO.insert(userAccount);
        UserAccount actualUserAccount = userAccountDAO.getById(3L);

        assertEquals(userAccount, actualUserAccount);
        ;
    }

    @Test
    public void getByIdWithNotExistsIdShouldThrowException() {
        DbException dbException = Assert.assertThrows(DbException.class, () -> userAccountDAO.getByUserId(3L));

        verify(storage, times(1)).getInMemoryStorage();
        assertEquals("The user account with user id 3 does not exist", dbException.getMessage());
    }

    @Test
    public void insertWithUserIdShouldBeOk() {
        UserAccount expectedUserAccount = new UserAccountImpl(5L);
        UserAccount actualUserAccount = userAccountDAO.insert(expectedUserAccount);
        expectedUserAccount.setUserId(actualUserAccount.getUserId());

        assertEquals(expectedUserAccount, actualUserAccount);
    }

    @Test
    public void insertWithNullUserAccountShouldThrowException() {
        DbException dbException = Assert.assertThrows(DbException.class,
                () -> userAccountDAO.insert(null));

        verify(storage, times(0)).getInMemoryStorage();
        assertEquals("The user account cannot be null", dbException.getMessage());
    }

    @Test
    public void updateWithExistsUserAccountShouldBeOk() {
        long expectedBalance = 100;
        userAccountDAO.insert(new UserAccountImpl(1L));
        UserAccount expectedUserAccount = userAccountDAO.getById(1L);
        expectedUserAccount.setBalance(expectedBalance);
        UserAccount actualUserAccount = userAccountDAO.update(expectedUserAccount);

        assertEquals(expectedUserAccount, actualUserAccount);
    }

    @Test
    public void updateWithNullUserAccountShouldThrowException() {
        DbException dbException = Assert.assertThrows(DbException.class,
                () -> userAccountDAO.insert(null));

        verify(storage, times(0)).getInMemoryStorage();
        assertEquals("The user account cannot be null", dbException.getMessage());
    }

    @Test
    public void updateEventWhichNotExistsShouldThrowException() {
        DbException dbException = Assert.assertThrows(DbException.class,
                () -> userAccountDAO.update(new UserAccountImpl(1L)));

        verify(storage, times(1)).getInMemoryStorage();
        assertEquals("The user account with user id 1 does not exist", dbException.getMessage());
    }

    @Test
    public void deleteExistsUserAccountShouldBeOk() {
        userAccountDAO.insert(new UserAccountImpl(6L));
        boolean actualIsDeleted = userAccountDAO.delete(6L);

        Assert.assertTrue(actualIsDeleted);
    }

    @Test
    public void deleteUserAccountWhichNotExistsShouldThrowException() {
        DbException dbException = Assert.assertThrows(DbException.class,
                () -> userAccountDAO.delete(10L));

        verify(storage, times(1)).getInMemoryStorage();
        assertEquals("The user account with user id 10 does not exist", dbException.getMessage());
    }

    @Test
    public void balanceWithExistsIdShouldBeOk() {
        UserAccount userAccount = new UserAccountImpl(3L, 200);
        userAccountDAO.insert(userAccount);
        UserAccount actualUserAccount = userAccountDAO.getById(3L);

        assertEquals(userAccount, actualUserAccount);
        assertEquals(200.0, actualUserAccount.getBalance(), 0.001);
    }

    @Test
    public void getZeroBalanceShouldReturnZero() {
        UserAccount userAccount = new UserAccountImpl(3L);
        userAccountDAO.insert(userAccount);
        UserAccount actualUserAccount = userAccountDAO.getById(3L);

        assertEquals(0.0, actualUserAccount.getBalance(), 0.001);
    }
}
