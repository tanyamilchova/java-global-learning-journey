package ua.epam.mishchenko.ticketbooking.facade.impl;

import org.springframework.stereotype.Component;
import ua.epam.mishchenko.ticketbooking.facade.BookingFacade;
import ua.epam.mishchenko.ticketbooking.model.impl.Event;
import ua.epam.mishchenko.ticketbooking.model.impl.Ticket;
import ua.epam.mishchenko.ticketbooking.model.impl.User;
import ua.epam.mishchenko.ticketbooking.model.impl.UserAccount;
import ua.epam.mishchenko.ticketbooking.service.EventService;
import ua.epam.mishchenko.ticketbooking.service.TicketService;
import ua.epam.mishchenko.ticketbooking.service.UserAccountService;
import ua.epam.mishchenko.ticketbooking.service.UserService;

import javax.transaction.Transactional;
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
    public Event getEventById(long eventId) {
        return eventService.getEventById(eventId);
    }

    @Override
    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        return eventService.getEventsByTitle(title, pageSize, pageNum);
    }

    @Override
    public List<Event> getEventsForDay(LocalDate day, int pageSize, int pageNum) {
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
    public Optional<User> getUserById(long userId) {
        return userService.getUserById(userId);
    }

    @Override
    public List<User> getAllUsers(int pageSize, int pageNum) {

            return userService.getAllUsers(pageSize, pageNum);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
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

    @Override
    @Transactional
    public Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category) {

        userService.getUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        Event event = eventService.getEventById(eventId);
        UserAccount account = userAccountService.getUserAccountByUserId(userId);

        if (account.getBalance() < event.getTicketPrice()) {
            throw new IllegalStateException("Not enough money");
        }
        account.setBalance(account.getBalance() - event.getTicketPrice());
        userAccountService.updateUserAccount(account);

        return ticketService.createTicket(userId, eventId, place, category);
    }

    @Override
    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
        return ticketService.getBookedTickets(user, pageSize, pageNum);
    }

    @Override
    public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
        return ticketService.getBookedTickets(event, pageSize, pageNum);
    }

    @Override
    public boolean cancelTicket(long ticketId) {
        return ticketService.cancelTicket(ticketId);
    }

    @Override
    public UserAccount createUserAccount(long userId) {
        return userAccountService.createUserAccount(userId);
    }

    @Override
    public UserAccount getUserAccountByUserId(long userId) {
        return userAccountService.getUserAccountByUserId(userId);
    }

    @Override
    public UserAccount updateUserAccount(UserAccount userAccount) {
        return userAccountService.updateUserAccount(userAccount);
    }

    @Override
    public boolean deleteUserAccount(long userId) {
        return userAccountService.deleteUserAccount(userId);
    }

    @Override
    public UserAccount addFunds(long userId, double amount) {
        return userAccountService.addFunds(userId, amount);
    }
}
