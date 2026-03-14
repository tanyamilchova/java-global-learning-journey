package ua.epam.mishchenko.ticketbooking.service.impl;


import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.epam.mishchenko.ticketbooking.exception.DbException;
import ua.epam.mishchenko.ticketbooking.model.Event;
import ua.epam.mishchenko.ticketbooking.model.Ticket;
import ua.epam.mishchenko.ticketbooking.model.User;
import ua.epam.mishchenko.ticketbooking.model.UserAccount;
import ua.epam.mishchenko.ticketbooking.model.impl.TicketImpl;
import ua.epam.mishchenko.ticketbooking.model.impl.UserAccountImpl;
import ua.epam.mishchenko.ticketbooking.model.repository.EventRepository;
import ua.epam.mishchenko.ticketbooking.model.repository.TicketRepository;
import ua.epam.mishchenko.ticketbooking.model.repository.UserAccountRepository;
import ua.epam.mishchenko.ticketbooking.service.TicketService;
import ua.epam.mishchenko.ticketbooking.utils.Util;

import java.util.ArrayList;
import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    UserAccountRepository userAccountRepository;

    private static final Logger LOGGER = LogManager.getLogger(TicketServiceImpl.class);

    @Transactional
    @Override
    public Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category) {
        Util.validateId(userId);
        Util.validateLong(eventId);
        if (place <= 0) {
            LOGGER.warn("Invalid place provided: {}", place);
            throw new IllegalArgumentException("Place must be positive: " + place);
        }
        Util.validateNotNull(category, "Ticket.Category");

        LOGGER.log(Level.DEBUG,
                "Start booking a ticket for user with id {}, event with id {}, place {}, category {}",
                userId, eventId, place, category);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    LOGGER.warn("Event {} not found", eventId);
                    return new DbException("Event not found: " + eventId);
                });

        UserAccount userAccount = userAccountRepository.findById(userId)
                .orElseThrow(() -> {
                    LOGGER.warn("User account {} not found", userId);
                    return new DbException("User account not found: " + userId);
                });

        if (userAccount.getBalance() < event.getTicketPrice()) {
            LOGGER.warn("Insufficient funds for user {} to book ticket for event {}", userId, eventId);
            throw new DbException("Insufficient funds for user " + userId);
        }

        userAccount.setBalance(userAccount.getBalance() - event.getTicketPrice());
        userAccountRepository.save((UserAccountImpl) userAccount);

        Ticket ticket = createNewTicket(userId, eventId, place, category);
        ticket = ticketRepository.save((TicketImpl) ticket);

        LOGGER.log(Level.DEBUG, "Successfully booked ticket: {}", ticket);
        return ticket;
    }


    private Ticket createNewTicket(long userId, long eventId, int place, Ticket.Category category) {
        return new TicketImpl(userId, eventId, place, category);
    }

    @Override
    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
        Util.validateUser(user);
        Util.validatePagination(pageSize, pageNum);
        LOGGER.log(Level.DEBUG,
                "Finding all booked tickets by user {} with page size {} and number of page {}",
                user, pageSize, pageNum);

        try {
           Pageable pageable = PageRequest.of(pageNum, pageSize);

            List<TicketImpl> ticketsByUser = ticketRepository.getAllByUserId(user.getId(), pageable).getContent();

            LOGGER.log(Level.DEBUG,
                    "All booked tickets successfully found by user {} with page size {} and number of page {}",
                    user, pageSize, pageNum);

            return new ArrayList<>(ticketsByUser);
        } catch (DbException e) {
            LOGGER.log(Level.WARN, "Can not to find a list of booked tickets by user '{}'", user, e);
            return null;
        }

    }

    @Override
    public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
        Util.validateNotNull(event, "Event");
        Util.validatePagination(pageSize, pageNum);
        LOGGER.log(Level.DEBUG,
                "Finding all booked tickets by event {} with page size {} and number of page {}",
                event, pageSize, pageNum);

        try {
            Pageable pageable = PageRequest.of(pageNum, pageSize);
            List<TicketImpl> ticketsByUser = ticketRepository.getAllByEventId(event.getId(), pageable).getContent();

            LOGGER.log(Level.DEBUG,
                    "All booked tickets successfully found by event {} with page size {} and number of page {}",
                    event, pageSize, pageNum);

            return new ArrayList<>(ticketsByUser);
        } catch (DbException e) {
            LOGGER.log(Level.WARN, "Can not to find a list of booked tickets by event '{}'", event, e);
            return null;
        }
    }

    @Override
    public boolean cancelTicket(long ticketId) {
        Util.validateLong(ticketId);
        LOGGER.log(Level.DEBUG, "Start canceling a ticket with id: {}", ticketId);

        try {
            ticketRepository.deleteById(ticketId);

            LOGGER.log(Level.DEBUG, "Successfully canceling of the ticket with id: {}", ticketId);

            return true;
        } catch (DbException e) {
            LOGGER.log(Level.WARN, "Can not to cancel a ticket with id: {}", ticketId, e);
            return false;
        }
    }
}
