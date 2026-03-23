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
import ua.epam.mishchenko.ticketbooking.model.impl.EventImpl;
import ua.epam.mishchenko.ticketbooking.model.impl.TicketImpl;
import ua.epam.mishchenko.ticketbooking.model.impl.UserAccountImpl;
import ua.epam.mishchenko.ticketbooking.model.impl.UserImpl;

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

        UserImpl user = new UserImpl();
        user.setName("Test User");
        user.setEmail("testuser@example.com");
        user = bookingFacade.createUser(user);

        UserAccountImpl userAccount = bookingFacade.createUserAccount(user.getId());
        bookingFacade.addFunds(user.getId(),2000);

        EventImpl event = new EventImpl("Test Event", LocalDate.now());
        event.setTicketPrice(100.0);
        event = bookingFacade.createEvent(event);

        TicketImpl ticket = bookingFacade.bookTicket(user.getId(), event.getId(), 2, TicketImpl.Category.STANDARD);

        assertNotNull(ticket);
        assertEquals(user.getId(), ticket.getUserId());
        assertEquals(event.getId(), ticket.getEventId());


        UserAccountImpl updatedAccount = bookingFacade.getUserAccountByUserId(user.getId());
        assertEquals(1900.0, updatedAccount.getBalance(), 0.01);
    }

    @Test
    public void cancelTicket_shouldUpdateUserAccountAndDeleteTicket() {

        UserImpl user = new UserImpl();
        user.setName("Test User");
        user.setEmail("testuser1@example.com");
        user = bookingFacade.createUser(user);

        UserAccountImpl userAccount = bookingFacade.createUserAccount(user.getId());
        bookingFacade.addFunds(user.getId(),2000);

        EventImpl event = new EventImpl("Test Event", LocalDate.now());
        event.setTicketPrice(100.0);
        event = bookingFacade.createEvent(event);

        TicketImpl ticket = bookingFacade.bookTicket(user.getId(), event.getId(), 2, TicketImpl.Category.STANDARD);

        boolean isDeleted = bookingFacade.cancelTicket(ticket.getId());
        assertTrue(isDeleted);

        UserAccountImpl updatedAccount = bookingFacade.getUserAccountByUserId(user.getId());
        assertEquals(2000, updatedAccount.getBalance(), 0.001);

    }

}
