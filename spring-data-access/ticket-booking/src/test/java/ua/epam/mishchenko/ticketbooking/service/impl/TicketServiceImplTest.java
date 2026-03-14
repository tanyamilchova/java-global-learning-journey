package ua.epam.mishchenko.ticketbooking.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ua.epam.mishchenko.ticketbooking.dao.EventDAO;
import ua.epam.mishchenko.ticketbooking.dao.UserAccountDAO;
import ua.epam.mishchenko.ticketbooking.dao.impl.TicketDAOImpl;
import ua.epam.mishchenko.ticketbooking.exception.DbException;
import ua.epam.mishchenko.ticketbooking.model.Event;
import ua.epam.mishchenko.ticketbooking.model.Ticket;
import ua.epam.mishchenko.ticketbooking.model.User;
import ua.epam.mishchenko.ticketbooking.model.UserAccount;
import ua.epam.mishchenko.ticketbooking.model.impl.EventImpl;
import ua.epam.mishchenko.ticketbooking.model.impl.TicketImpl;
import ua.epam.mishchenko.ticketbooking.model.impl.UserAccountImpl;
import ua.epam.mishchenko.ticketbooking.model.impl.UserImpl;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TicketServiceImplTest {

    private TicketServiceImpl ticketService;

    @Mock
    private TicketDAOImpl ticketDAO;

    @Mock
    private EventDAO eventDAO;

    @Mock
    private UserAccountDAO userAccountDAO;

    @Before
    public void setUp() {
        ticketService = new TicketServiceImpl();
    }

    @Test
    public void bookTicketWithNotBookedTicketShouldBeOk() {
        long userId = 10L;
        long eventId = 10L;
        int place = 20;
        Ticket.Category category = Ticket.Category.PREMIUM;

        Event mockEvent = new EventImpl(eventId, "Sample Event", 50);
        UserAccount realUserAccount = new UserAccountImpl(userId, 100);
        Ticket expectedTicket = new TicketImpl(userId, eventId, place, category);

        when(eventDAO.getById(eventId)).thenReturn(mockEvent);
        when(userAccountDAO.getByUserId(userId)).thenReturn(realUserAccount);
        when(userAccountDAO.update(any(UserAccount.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(ticketDAO.insert(any(Ticket.class))).thenReturn(expectedTicket);

        Ticket actualTicket = ticketService.bookTicket(userId, eventId, place, category);

        assertEquals(expectedTicket, actualTicket);
        assertEquals(50.0, realUserAccount.getBalance(), 0.001);
    }

    @Test
    public void bookTicketWithExceptionShouldReturnNull() {
        long userId = 4L;
        long eventId = 1L;

        Event mockEvent = new EventImpl(eventId, "Sample Event", 50);
        UserAccount mockUserAccount = new UserAccountImpl(userId, 100);

        when(eventDAO.getById(eventId)).thenReturn(mockEvent);
        when(userAccountDAO.getByUserId(userId)).thenReturn(mockUserAccount);
        when(ticketDAO.insert(any())).thenThrow(DbException.class);

        Ticket actualTicket = ticketService.bookTicket(userId, eventId, 10, Ticket.Category.BAR);

        assertNull(actualTicket);
    }

    @Test
    public void getBookedTicketsWithNotNullUserAndProperPageSizeAndPageNumShouldBeOk() {
        User user = new UserImpl(1, "Alan", "alan@gmail.com");
        List<Ticket> expectedListOfTicketsByUser = Arrays.asList(
                new TicketImpl(1L, 1L, 1L, 10, Ticket.Category.BAR),
                new TicketImpl(4L, 1L, 4L, 20, Ticket.Category.BAR)
        );

        when(ticketDAO.getAllByUser(any(), anyInt(), anyInt())).thenReturn(expectedListOfTicketsByUser);

        List<Ticket> actualListOfTicketsByUser = ticketService.getBookedTickets(user, 2, 1);

        assertEquals(expectedListOfTicketsByUser, actualListOfTicketsByUser);
    }

    @Test
    public void getBookedTicketsByUserWithExceptionShouldReturnNull() {
        when(ticketDAO.getAllByUser(any(), anyInt(), anyInt())).thenThrow(DbException.class);

        List<Ticket> actualListOfTicketsByUser = ticketService.getBookedTickets((User) null, 2, 1);

        assertNull(actualListOfTicketsByUser);
    }

    @Test
    public void getBookedTicketsWithNotNullEventAndProperPageSizeAndPageNumShouldBeOk() throws ParseException {
        Event event = new EventImpl(4, "Fourth event", LocalDate.of(2026, 1, 15));
        List<Ticket> expectedListOfTicketsByEvent = Arrays.asList(
                new TicketImpl(4L, 1L, 4L, 20, Ticket.Category.BAR),
                new TicketImpl(2L, 3L, 4L, 10, Ticket.Category.PREMIUM)
        );

        when(ticketDAO.getAllByEvent(any(), anyInt(), anyInt())).thenReturn(expectedListOfTicketsByEvent);

        List<Ticket> actualListOfTicketsByEvent = ticketService.getBookedTickets(event, 2, 1);

        assertEquals(expectedListOfTicketsByEvent, actualListOfTicketsByEvent);
    }

    @Test
    public void getBookedTicketsByEventWithExceptionShouldReturnNull() {
        when(ticketDAO.getAllByEvent(any(), anyInt(), anyInt())).thenThrow(DbException.class);

        List<Ticket> actualListOfTicketsByEvent = ticketService.getBookedTickets((Event) null, 2, 1);

        assertNull(actualListOfTicketsByEvent);
    }

    @Test
    public void cancelTicketExistsTicketShouldReturnTrue() {
        when(ticketDAO.delete(anyLong())).thenReturn(true);

        boolean actualIsDeleted = ticketService.cancelTicket(6L);

        assertTrue(actualIsDeleted);
    }

    @Test
    public void cancelTicketWithExceptionShouldReturnFalse() {
        when(ticketDAO.delete(anyLong())).thenThrow(DbException.class);

        boolean isRemoved = ticketService.cancelTicket(10L);

        assertFalse(isRemoved);
    }
}