package integrationTests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import ua.epam.mishchenko.ticketbooking.config.AppConfig;
import ua.epam.mishchenko.ticketbooking.exception.DbException;
import ua.epam.mishchenko.ticketbooking.model.impl.User;
import ua.epam.mishchenko.ticketbooking.model.impl.UserAccount;
import ua.epam.mishchenko.ticketbooking.model.repository.UserAccountRepository;
import ua.epam.mishchenko.ticketbooking.service.UserAccountService;
import ua.epam.mishchenko.ticketbooking.service.UserService;

import java.util.Optional;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@Transactional
public class UserAccountServiceIntegrationTest {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    UserService userService;

    @Autowired
    UserAccountService userAccountService;

    @Test
    public void findByIdShouldReturnAccount() {
        UserAccount account = new UserAccount(5L);
        account.setBalance(200);

        userAccountRepository.save(account);
        Optional<UserAccount> result = userAccountRepository.findById(5L);

        assertTrue(result.isPresent());
        assertEquals(200.0, result.get().getBalance(), 0.001);
    }

    @Test
    public void getUserAccountByUserIdShouldThrowWithInvalidUserId() {
        long invalidUserId = 0L;
        assertThrows(IllegalArgumentException.class, () ->
                userAccountService.getUserAccountByUserId(invalidUserId)
        );
    }

    @Test
    public void createUserAccount_ShouldPersist_WhenUserExists() {
        User user = userService.createUser(new User("Peter", "peer1@gmail.com"));

        UserAccount account = userAccountService.createUserAccount(user.getId());

        assertNotNull(account);
        UserAccount fromDb = userAccountRepository.findById(account.getUserId()).orElse(null);
        assertNotNull(fromDb);
        assertEquals(user.getId(), fromDb.getUserId());
    }

    @Test
    public void createUserAccount_ShouldFail_WhenUserDoesNotExist() {
        long nonExistentUserId = 9999L;

        DbException exception = assertThrows(DbException.class, () ->
                userAccountService.createUserAccount(nonExistentUserId)
        );

        assertTrue(exception.getMessage().contains("User not found: " + nonExistentUserId));
    }

    @Test
    public void addFunds_ShouldIncreaseBalance_WhenAmountIsPositive() {
        User user = userService.createUser(new User("Poly", "poli@gmail.com"));
        UserAccount account = userAccountService.createUserAccount(user.getId());

        double initialBalance = account.getBalance();
        double amountToAdd = 100.0;

        UserAccount updatedAccount = userAccountService.addFunds(user.getId(), amountToAdd);

        assertNotNull(updatedAccount);
        assertEquals(initialBalance + amountToAdd, updatedAccount.getBalance(), 0.001);

        UserAccount fromDb = userAccountRepository.findById(user.getId()).orElse(null);
        assertNotNull(fromDb);
        assertEquals(initialBalance + amountToAdd, fromDb.getBalance(), 0.001);
    }

    @Test
    public void addFunds_ShouldThrowException_WhenAmountIsZeroOrNegative() {
        User user = userService.createUser(new User("Anna", "anna@gmail.com"));
        userAccountService.createUserAccount(user.getId());

        double negativeAmount = -50.0;
        double zeroAmount = 0.0;

        assertThrows(IllegalArgumentException.class, () ->
                userAccountService.addFunds(user.getId(), negativeAmount));

        assertThrows(IllegalArgumentException.class, () ->
                userAccountService.addFunds(user.getId(), zeroAmount));
    }

    @Test
    public void addFunds_ShouldThrowDbException_WhenUserAccountDoesNotExist() {
        long nonExistingUserId = 10000L;
        double amount = 50.0;

        DbException exception = assertThrows(DbException.class, () ->
                userAccountService.addFunds(nonExistingUserId, amount));

        assertTrue(exception.getMessage().contains("User account not found"));
    }

    @Test
    public void updateUserAccountSuccessfulWhenUserAccountExists() {
        User user = userService.createUser(new User("John", "john@gmail.com"));
        UserAccount account = userAccountService.createUserAccount(user.getId());

        double newBalance = account.getBalance() + 150.0;
        account.setBalance(newBalance);

        UserAccount updatedAccount = userAccountService.updateUserAccount(account);

        assertNotNull(updatedAccount);
        assertEquals(newBalance, updatedAccount.getBalance(), 0.001);

        UserAccount fromDb = userAccountRepository.findById(account.getUserId()).orElse(null);
        assertNotNull(fromDb);
        assertEquals(newBalance, fromDb.getBalance(), 0.001);
    }

    @Test
    public void updateUserAccount_ShouldThrowIllegalArgumentException_WhenInputIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                userAccountService.updateUserAccount(null)
        );
    }

    @Test
    public void updateUserAccount_ShouldHandleNegativeBalanceProperly() {
        User user = userService.createUser(new User("Lara", "lara@gmail.com"));
        UserAccount account = userAccountService.createUserAccount(user.getId());

        account.setBalance(-50.0);

        UserAccount updatedAccount = userAccountService.updateUserAccount(account);

        assertEquals(-50.0, updatedAccount.getBalance(), 0.001);

    }


    @Test
    public void deleteUserAccount_ShouldRemoveAccount_WhenUserExists() {
        User user = userService.createUser(new User("Ana", "ana@gmail.com"));
        UserAccount account = userAccountService.createUserAccount(user.getId());

        boolean deleted = userAccountService.deleteUserAccount(user.getId());

        assertTrue("deleteUserAccount should return true", deleted);

        Optional<UserAccount> fromDb = userAccountRepository.findById(user.getId());
        assertFalse("User account should be deleted from DB", fromDb.isPresent());
    }

    @Test
    public void deleteUserAccount_ShouldThrowDbException_WhenUserDoesNotExist() {
        long nonExistentUserId = 10000L;

        DbException exception = assertThrows(DbException.class, () ->
                userAccountService.deleteUserAccount(nonExistentUserId)
        );

        assertTrue(exception.getMessage().contains("User account not found for userId: " + nonExistentUserId));
    }

    @Test
    public void deleteUserAccount_ShouldThrowIllegalArgumentException_WhenUserIdIsInvalid() {
        long invalidUserId1 = -5L;
        long invalidUserId2 = 0L;

        assertThrows(IllegalArgumentException.class, () ->
                userAccountService.deleteUserAccount(invalidUserId1)
        );

        assertThrows(IllegalArgumentException.class, () ->
                userAccountService.deleteUserAccount(invalidUserId2)
        );
    }
}
