package integrationTests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import ua.epam.mishchenko.ticketbooking.config.AppConfig;
import ua.epam.mishchenko.ticketbooking.facade.BookingFacade;
import ua.epam.mishchenko.ticketbooking.model.impl.Event;
import ua.epam.mishchenko.ticketbooking.model.impl.Ticket;
import ua.epam.mishchenko.ticketbooking.model.impl.User;
import ua.epam.mishchenko.ticketbooking.model.impl.UserAccount;

import java.time.LocalDate;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@Transactional
@Rollback
public class BookingFacadeIntegrationTest {

    @Autowired
    private BookingFacade bookingFacade;

    @Test
    public void bookTicket_shouldUpdateUserAccountAndCreateTicket() {

        User user = new User();
        user.setName("Test User");
        user.setEmail("testuser@example.com");
        user = bookingFacade.createUser(user);

        bookingFacade.createUserAccount(user.getId());
        bookingFacade.addFunds(user.getId(),2000);

        Event event = new Event("Test Event", LocalDate.now());
        event.setTicketPrice(100.0);
        event = bookingFacade.createEvent(event);

        Ticket ticket = bookingFacade.bookTicket(user.getId(), event.getId(), 2, Ticket.Category.STANDARD);

        assertNotNull(ticket);
        assertEquals(user.getId(), ticket.getUserId());
        assertEquals(event.getId(), ticket.getEventId());


        UserAccount updatedAccount = bookingFacade.getUserAccountByUserId(user.getId());
        assertEquals(1900.0, updatedAccount.getBalance(), 0.01);
    }

    @Test
    public void cancelTicket_shouldUpdateUserAccountAndDeleteTicket() {

        User user = new User();
        user.setName("Test User");
        user.setEmail("testuser1@example.com");
        user = bookingFacade.createUser(user);

        bookingFacade.createUserAccount(user.getId());
        bookingFacade.addFunds(user.getId(),2000);

        Event event = new Event("Test Event", LocalDate.now());
        event.setTicketPrice(100.0);
        event = bookingFacade.createEvent(event);

        Ticket ticket = bookingFacade.bookTicket(user.getId(), event.getId(), 2, Ticket.Category.STANDARD);

        boolean isDeleted = bookingFacade.cancelTicket(ticket.getId());
        assertTrue(isDeleted);

        UserAccount updatedAccount = bookingFacade.getUserAccountByUserId(user.getId());
        assertEquals(2000, updatedAccount.getBalance(), 0.001);

    }

}
