package ua.epam.mishchenko.ticketbooking.service.impl;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.epam.mishchenko.ticketbooking.dao.UserDAO;
import ua.epam.mishchenko.ticketbooking.exception.DbException;
import ua.epam.mishchenko.ticketbooking.model.impl.Event;
import ua.epam.mishchenko.ticketbooking.model.impl.Ticket;
import ua.epam.mishchenko.ticketbooking.model.impl.User;
import ua.epam.mishchenko.ticketbooking.model.impl.UserAccount;
import ua.epam.mishchenko.ticketbooking.model.repository.EventRepository;
import ua.epam.mishchenko.ticketbooking.model.repository.TicketRepository;
import ua.epam.mishchenko.ticketbooking.model.repository.UserAccountRepository;
import ua.epam.mishchenko.ticketbooking.service.TicketService;
import ua.epam.mishchenko.ticketbooking.validator.GenericValidator;
import ua.epam.mishchenko.ticketbooking.validator.UserAccountValidator;
import ua.epam.mishchenko.ticketbooking.validator.UserValidator;
import ua.epam.mishchenko.ticketbooking.validator.Util;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final UserAccountRepository userAccountRepository;
    private final UserValidator userValidator;
    private final GenericValidator genericValidator;
    private final UserAccountValidator userAccountValidator;
    private final UserDAO userDAO;

    @Autowired
    public TicketServiceImpl(
            TicketRepository ticketRepository,
            EventRepository eventRepository,
            UserAccountRepository userAccountRepository,
            UserValidator userValidator,
            GenericValidator genericValidator,
            UserAccountValidator userAccountValidator,
            UserDAO userDAO
    ) {
        this.ticketRepository = ticketRepository;
        this.eventRepository = eventRepository;
        this.userAccountRepository = userAccountRepository;
        this.userValidator = userValidator;
        this.genericValidator = genericValidator;
        this.userAccountValidator = userAccountValidator;
        this.userDAO = userDAO;
    }

    @Transactional
    @Override
    public Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category) {
        genericValidator.validateId(userId, "User id");
        genericValidator.validateId(eventId, "Event id");


        if (place <= 0) {
            log.warn("Invalid place provided: {}", place);
            throw new IllegalArgumentException("Place must be positive: " + place);
        }
        Util.validateNotNull(category, "Ticket.Category");

        log.debug("Start booking a ticket for user with id {}, event with id {}, place {}, category {}",
                userId, eventId, place, category);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    log.warn("Event {} not found", eventId);
                    return new DbException("Event not found: " + eventId);
                });

        UserAccount userAccount = userAccountRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User account {} not found", userId);
                    return new DbException("User account not found: " + userId);
                });

        Util.updateBalance(userAccount, - event.getTicketPrice());

        userAccountRepository.save( userAccount);

        Ticket ticket = createNewTicket(userId, eventId, place, category);
        Ticket bookedTicket = ticketRepository.save(ticket);

        log.info("Successfully booked ticket: {}", ticket);
        return bookedTicket;
    }


    private Ticket createNewTicket(long userId, long eventId, int place, Ticket.Category category) {
        return new Ticket(userId, eventId, place, category);
    }

    @Override
    public Ticket createTicket(long userId, long eventId, int place, Ticket.Category category) {

        genericValidator.validateId(userId, "User id");
        genericValidator.validateId(eventId, "Event id");

        if (place <= 0) {
            log.warn("Invalid place provided: {}", place);
            throw new IllegalArgumentException("Place must be positive: " + place);
        }

        Util.validateNotNull(category, "Ticket.Category");

        log.debug("Creating ticket for user {}, event {}, place {}, category {}",
                userId, eventId, place, category);

        Ticket ticket = new Ticket(userId, eventId, place, category);

        Ticket savedTicket = ticketRepository.save(ticket);

        log.info("Ticket successfully created: {}", savedTicket);

        return savedTicket;
    }

    @Override
    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {

        userValidator.validate(user);
        genericValidator.validatePagination(pageSize, pageNum);

        log.debug("Finding tickets for user {} pageSize {} pageNum {}",
                user, pageSize, pageNum);

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);

        List<Ticket> tickets =
                ticketRepository.getAllByUserId(user.getId(), pageable).getContent();

        return new ArrayList<>(tickets);
    }

    @Override
    public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {

        Util.validateNotNull(event, "Event");
        genericValidator.validatePagination(pageSize, pageNum);

        log.debug("Finding tickets for event {} pageSize {} pageNum {}",
                event, pageSize, pageNum);

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);

        List<Ticket> tickets =
                ticketRepository.getAllByEventId(event.getId(), pageable).getContent();

        return new ArrayList<>(tickets);
    }

    @Override
    public boolean cancelTicket(long ticketId) {

        genericValidator.validateLong(ticketId, "Ticket id");

        log.debug("Deleting ticket with id {}", ticketId);

        if (!ticketRepository.existsById(ticketId)) {
            throw new IllegalArgumentException("Ticket not found: " + ticketId);
        }

        ticketRepository.deleteById(ticketId);

        log.info("Ticket deleted successfully: {}", ticketId);

        return true;
    }
}
