package ua.epam.mishchenko.ticketbooking.facade.impl;

import org.springframework.stereotype.Component;
import ua.epam.mishchenko.ticketbooking.facade.BookingFacade;
import ua.epam.mishchenko.ticketbooking.model.impl.EventImpl;
import ua.epam.mishchenko.ticketbooking.model.impl.TicketImpl;
import ua.epam.mishchenko.ticketbooking.model.impl.UserAccountImpl;
import ua.epam.mishchenko.ticketbooking.model.impl.UserImpl;
import ua.epam.mishchenko.ticketbooking.service.EventService;
import ua.epam.mishchenko.ticketbooking.service.TicketService;
import ua.epam.mishchenko.ticketbooking.service.UserAccountService;
import ua.epam.mishchenko.ticketbooking.service.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class BookingFacadeImpl implements BookingFacade {

    private final EventService eventService;

    private final TicketService ticketService;

    private final UserService userService;

    private final UserAccountService userAccountService;

    public BookingFacadeImpl(EventService eventService, TicketService ticketService, UserService userService, UserAccountService userAccountService) {
        this.eventService = eventService;
        this.ticketService = ticketService;
        this.userService = userService;
        this.userAccountService = userAccountService;
    }

    @Override
    public EventImpl getEventById(long eventId) {
        return eventService.getEventById(eventId);
    }

    @Override
    public List<EventImpl> getEventsByTitle(String title, int pageSize, int pageNum) {
        return eventService.getEventsByTitle(title, pageSize, pageNum);
    }

    @Override
    public List<EventImpl> getEventsForDay(LocalDate day, int pageSize, int pageNum) {
        return eventService.getEventsForDay(day, pageSize, pageNum);
    }

    @Override
    public EventImpl createEvent(EventImpl event) {
        return eventService.createEvent(event);
    }

    @Override
    public EventImpl updateEvent(EventImpl event) {
        return eventService.updateEvent(event);
    }

    @Override
    public boolean deleteEvent(long eventId) {
        return eventService.deleteEvent(eventId);
    }

    @Override
    public Optional<UserImpl> getUserById(long userId) {
        return userService.getUserById(userId);
    }

    @Override
    public List<UserImpl> getAllUsers(int pageSize, int pageNum) {

            return userService.getAllUsers(pageSize, pageNum);
    }

    @Override
    public Optional<UserImpl> getUserByEmail(String email) {
        return userService.getUserByEmail(email);
    }

    @Override
    public List<UserImpl> getUsersByName(String name, int pageSize, int pageNum) {
        return userService.getUsersByName(name, pageSize, pageNum);
    }

    @Override
    public UserImpl createUser(UserImpl user) {
        return userService.createUser(user);
    }

    @Override
    public UserImpl updateUser(UserImpl user) {
        return userService.updateUser(user);
    }

    @Override
    public boolean deleteUser(long userId) {
        return userService.deleteUser(userId);
    }

    @Override
    public TicketImpl bookTicket(long userId, long eventId, int place, TicketImpl.Category category) {
        return ticketService.bookTicket(userId, eventId, place, category);
    }

    @Override
    public List<TicketImpl> getBookedTickets(UserImpl user, int pageSize, int pageNum) {
        return ticketService.getBookedTickets(user, pageSize, pageNum);
    }

    @Override
    public List<TicketImpl> getBookedTickets(EventImpl event, int pageSize, int pageNum) {
        return ticketService.getBookedTickets(event, pageSize, pageNum);
    }

    @Override
    public boolean cancelTicket(long ticketId) {
        return ticketService.cancelTicket(ticketId);
    }

    @Override
    public UserAccountImpl createUserAccount(long userId) {
        return userAccountService.createUserAccount(userId);
    }

    @Override
    public UserAccountImpl getUserAccountByUserId(long userId) {
        return userAccountService.getUserAccountByUserId(userId);
    }

    @Override
    public UserAccountImpl updateUserAccount(UserAccountImpl userAccount) {
        return userAccountService.updateUserAccount(userAccount);
    }

    @Override
    public boolean deleteUserAccount(long userId) {
        return userAccountService.deleteUserAccount(userId);
    }

    @Override
    public UserAccountImpl addFunds(long userId, double amount) {
        return userAccountService.addFunds(userId, amount);
    }
}
