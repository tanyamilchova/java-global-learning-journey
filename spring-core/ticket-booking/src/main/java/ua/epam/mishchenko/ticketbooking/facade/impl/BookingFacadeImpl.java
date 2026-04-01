package ua.epam.mishchenko.ticketbooking.facade.impl;

import jakarta.transaction.Transactional;
import ua.epam.mishchenko.ticketbooking.facade.BookingFacade;
import ua.epam.mishchenko.ticketbooking.model.Event;
import ua.epam.mishchenko.ticketbooking.model.Ticket;
import ua.epam.mishchenko.ticketbooking.model.User;
import ua.epam.mishchenko.ticketbooking.model.UserAccount;
import ua.epam.mishchenko.ticketbooking.service.impl.EventServiceImpl;
import ua.epam.mishchenko.ticketbooking.service.impl.TicketServiceImpl;
import ua.epam.mishchenko.ticketbooking.service.impl.UserAccountServiceImpl;
import ua.epam.mishchenko.ticketbooking.service.impl.UserServiceImpl;

import java.util.Date;
import java.util.List;

public class BookingFacadeImpl implements BookingFacade {

    private final EventServiceImpl eventService;

    private final TicketServiceImpl ticketService;

    private final UserServiceImpl userService;

    private final UserAccountServiceImpl userAccountService;

    public BookingFacadeImpl(EventServiceImpl eventService, TicketServiceImpl ticketService, UserServiceImpl userService, UserAccountServiceImpl userAccountService) {
        this.eventService = eventService;
        this.ticketService = ticketService;
        this.userService = userService;
        this.userAccountService = userAccountService;
    }

    @Override
    public Event getEventById(long eventId) {
        return eventService.getEventById(eventId);
    }

    @Override
    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        return eventService.getEventsByTitle(title, pageSize, pageNum);
    }

    @Override
    public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
        return eventService.getEventsForDay(day, pageSize, pageNum);
    }

    @Override
    public Event createEvent(Event event) {
        return eventService.createEvent(event);
    }

    @Override
    public Event updateEvent(Event event) {
        return eventService.updateEvent(event);
    }

    @Override
    public boolean deleteEvent(long eventId) {
        return eventService.deleteEvent(eventId);
    }

    @Override
    public User getUserById(long userId) {
        return userService.getUserById(userId);
    }

    @Override
    public User getUserByEmail(String email) {
        return userService.getUserByEmail(email);
    }

    @Override
    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        return userService.getUsersByName(name, pageSize, pageNum);
    }

    @Override
    public User createUser(User user) {
        return userService.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        return userService.updateUser(user);
    }

    @Override
    public boolean deleteUser(long userId) {
        return userService.deleteUser(userId);
    }

    @Transactional
    @Override
    public Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category) {
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + userId);
        }

        Event event = eventService.getEventById(eventId);
        if (event == null) {
            throw new IllegalArgumentException("Event not found: " + eventId);
        }

        UserAccount account = userAccountService.getAccountByUserId(userId);
        if (account == null) {
            throw new IllegalArgumentException("User account not found for userId: " + userId);
        }

        if (account.getBalance() < event.getTicketPrice()) {
            throw new IllegalStateException("Not enough money");
        }

        account.setBalance(account.getBalance() - event.getTicketPrice());
        userAccountService.updateAccount(account);
        return ticketService.bookTicket(userId, eventId, place, category);
    }

    @Override
    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
        return ticketService.getBookedTickets(user, pageSize, pageNum);
    }

    @Override
    public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
        return ticketService.getBookedTickets(event, pageSize, pageNum);
    }

    @Transactional
    @Override
    public boolean cancelTicket(long ticketId) {
        Ticket ticket = ticketService.getTicketById(ticketId);
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket not found: " + ticketId);
        }

        Event event = eventService.getEventById(ticket.getEventId());
        if (event == null) {
            throw new IllegalArgumentException("Event not found: " + ticket.getEventId());
        }

        UserAccount account = userAccountService.getAccountByUserId(ticket.getUserId());
        if (account == null) {
            throw new IllegalArgumentException("User account not found for userId: " + ticket.getUserId());
        }

        account.setBalance(account.getBalance() + event.getTicketPrice());
        userAccountService.updateAccount(account);

        return ticketService.cancelTicket(ticketId);
    }

    @Override
    public UserAccount createUserAccount(long userId) {
        return userAccountService.createUserAccount(userId);
    }

    @Override
    public UserAccount getAccountByUserId(long userId) {
        return userAccountService.getAccountByUserId(userId);
    }

    @Override
    public UserAccount updateAccount(UserAccount userAccount) {
        return userAccountService.updateAccount(userAccount);
    }

    @Override
    public boolean deleteUserAccount(long userId) {
        return userAccountService.deleteUserAccount(userId);
    }
}
