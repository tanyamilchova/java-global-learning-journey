package tests;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.epam.mishchenko.ticketbooking.exception.DbException;
import ua.epam.mishchenko.ticketbooking.model.impl.EventImpl;
import ua.epam.mishchenko.ticketbooking.model.impl.TicketImpl;
import ua.epam.mishchenko.ticketbooking.model.impl.UserAccountImpl;
import ua.epam.mishchenko.ticketbooking.model.repository.EventRepository;
import ua.epam.mishchenko.ticketbooking.model.repository.TicketRepository;
import ua.epam.mishchenko.ticketbooking.model.repository.UserAccountRepository;
import ua.epam.mishchenko.ticketbooking.service.impl.TicketServiceImpl;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TicketServiceTest {


    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserAccountRepository userAccountRepository;

    @InjectMocks
    private TicketServiceImpl ticketService;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void bookTicketShouldSucceed() {

        long userId = 1L;
        long eventId = 100L;
        int place = 5;
        TicketImpl.Category category = TicketImpl.Category.PREMIUM;

        EventImpl event = new EventImpl();
        event.setId(eventId);
        event.setTicketPrice(50.0);

        UserAccountImpl account = new UserAccountImpl(userId, 100.0);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(userAccountRepository.findById(userId)).thenReturn(Optional.of(account));
        when(ticketRepository.save(any(TicketImpl.class))).thenAnswer(i -> i.getArgument(0));

        TicketImpl ticket = ticketService.bookTicket(userId, eventId, place, category);

        assertNotNull(ticket);
        assertEquals(userId, ticket.getUserId());
        assertEquals(eventId, ticket.getEventId());
        assertEquals(place, ticket.getPlace());
        assertEquals(category, ticket.getCategory());

        assertEquals(50.0, account.getBalance(), 0.001);

        verify(eventRepository).findById(eventId);
        verify(userAccountRepository).findById(userId);
        verify(userAccountRepository).save(account);
        verify(ticketRepository).save(any(TicketImpl.class));

    }

    @Test
    public void bookTicketShouldThrowWhenPlaceInvalid() {

        assertThrows(IllegalArgumentException.class, () ->
                ticketService.bookTicket(1L, 100L, 0, TicketImpl.Category.STANDARD)
        );

        verifyNoInteractions(eventRepository, userAccountRepository, ticketRepository);
    }


    @Test
    public void bookTicketShouldThrowWhenEventNotFound() {

        long userId = 1L;
        long eventId = 100L;

        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        assertThrows(DbException.class, () ->
                ticketService.bookTicket(userId, eventId, 5, TicketImpl.Category.STANDARD)
        );

        verify(eventRepository).findById(eventId);
        verifyNoInteractions(userAccountRepository, ticketRepository);
    }

    @Test
    public void bookTicketShouldThrowWhenUserAccountNotFound() {

        long userId = 1L;
        long eventId = 100L;

        EventImpl event = new EventImpl();
        event.setId(eventId);
        event.setTicketPrice(50.0);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(userAccountRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(DbException.class, () ->
                ticketService.bookTicket(userId, eventId, 5, TicketImpl.Category.STANDARD)
        );

        verify(eventRepository).findById(eventId);
        verify(userAccountRepository).findById(userId);
        verifyNoInteractions(ticketRepository);
    }

    @Test
    public void bookTicketShouldThrowWhenInsufficientFunds() {

        long userId = 1L;
        long eventId = 100L;

        EventImpl event = new EventImpl();
        event.setId(eventId);
        event.setTicketPrice(100.0);

        UserAccountImpl account = new UserAccountImpl(userId, 50.0);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(userAccountRepository.findById(userId)).thenReturn(Optional.of(account));

        assertThrows(DbException.class, () ->
                ticketService.bookTicket(userId, eventId, 5, TicketImpl.Category.STANDARD)
        );

        assertEquals(50.0, account.getBalance(), 0.001);
        verify(ticketRepository, never()).save(any());
    }
}
