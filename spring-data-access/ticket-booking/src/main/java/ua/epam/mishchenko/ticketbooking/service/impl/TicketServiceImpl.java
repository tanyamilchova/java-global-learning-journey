package ua.epam.mishchenko.ticketbooking.service.impl;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.epam.mishchenko.ticketbooking.dao.UserDAO;
import ua.epam.mishchenko.ticketbooking.exception.DbException;
import ua.epam.mishchenko.ticketbooking.model.impl.EventImpl;
import ua.epam.mishchenko.ticketbooking.model.impl.TicketImpl;
import ua.epam.mishchenko.ticketbooking.model.impl.UserAccountImpl;
import ua.epam.mishchenko.ticketbooking.model.impl.UserImpl;
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

@Service
public class TicketServiceImpl implements TicketService {

    private static final Logger LOGGER = LogManager.getLogger(TicketServiceImpl.class);

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
    public TicketImpl bookTicket(long userId, long eventId, int place, TicketImpl.Category category) {
        genericValidator.validateId(userId, "User id");
        genericValidator.validateId(eventId, "Event id");


        if (place <= 0) {
            LOGGER.warn("Invalid place provided: {}", place);
            throw new IllegalArgumentException("Place must be positive: " + place);
        }
        Util.validateNotNull(category, "Ticket.Category");

        LOGGER.debug("Start booking a ticket for user with id {}, event with id {}, place {}, category {}",
                userId, eventId, place, category);

        EventImpl event = eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    LOGGER.warn("Event {} not found", eventId);
                    return new DbException("Event not found: " + eventId);
                });

        UserAccountImpl userAccount = userAccountRepository.findById(userId)
                .orElseThrow(() -> {
                    LOGGER.warn("User account {} not found", userId);
                    return new DbException("User account not found: " + userId);
                });

        Util.updateBalance(userAccount, - event.getTicketPrice());

        userAccountRepository.save((UserAccountImpl) userAccount);

        TicketImpl ticket = createNewTicket(userId, eventId, place, category);
        TicketImpl bookedTicket = ticketRepository.save((TicketImpl) ticket);

        LOGGER.info("Successfully booked ticket: {}", ticket);
        return bookedTicket;
    }


    private TicketImpl createNewTicket(long userId, long eventId, int place, TicketImpl.Category category) {
        return new TicketImpl(userId, eventId, place, category);
    }

    @Override
    public List<TicketImpl> getBookedTickets(UserImpl user, int pageSize, int pageNum) {
        userValidator.validate(user);
        genericValidator.validatePagination(pageSize, pageNum);
        LOGGER.debug("Finding all booked tickets by user {} with page size {} and number of page {}", user, pageSize, pageNum);

        try {
           Pageable pageable = PageRequest.of(pageNum - 1, pageSize);

            List<TicketImpl> ticketsByUser = ticketRepository.getAllByUserId(user.getId(), pageable).getContent();

            LOGGER.info("All booked tickets successfully found by user {} with page size {} and number of page {}", user, pageSize, pageNum);

            return new ArrayList<>(ticketsByUser);
        } catch (DbException e) {
            LOGGER.warn("Cannot find a list of booked tickets by user '{}'", user, e);
            return null;
        }

    }


    @Override
    public List<TicketImpl> getBookedTickets(EventImpl event, int pageSize, int pageNum) {
        Util.validateNotNull(event, "Event");
        genericValidator.validatePagination(pageSize, pageNum);
        LOGGER.debug("Finding all booked tickets by event {} with page size {} and number of page {}", event, pageSize, pageNum);


        try {
            Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
            List<TicketImpl> ticketsByUser = ticketRepository.getAllByEventId(event.getId(), pageable).getContent();

            LOGGER.info("All booked tickets successfully found by event {} with page size {} and number of page {}", event, pageSize, pageNum);

            return new ArrayList<>(ticketsByUser);
        } catch (DbException e) {
            LOGGER.warn("Cannot find a list of booked tickets by event '{}'", event, e);
            return null;
        }
    }

    @Transactional
    @Override
    public boolean cancelTicket(long ticketId) {
        genericValidator.validateLong(ticketId, "Ticket id");
        LOGGER.debug("Start canceling a ticket with id: {}", ticketId);

        try {
            TicketImpl ticket = ticketRepository.findById(ticketId).orElseThrow(() ->new IllegalArgumentException("Ticket not found"));
            UserImpl user = userDAO.getById(ticket.getUserId()).orElseThrow(() ->new IllegalArgumentException("User not found"));
            long userAccountId = user.getId();
            UserAccountImpl userAccount = userAccountRepository.findById(userAccountId).orElseThrow(() ->new IllegalArgumentException("User account not found"));
            EventImpl event = eventRepository.findById(ticket.getEventId()).orElseThrow(() ->new IllegalArgumentException("User account not found"));

            userAccountValidator.updateBalance(userAccount, event.getTicketPrice());
            ticketRepository.deleteById(ticketId);

            LOGGER.info("Successfully canceled the ticket with id: {}", ticketId);

            return true;
        } catch (DbException e) {
            LOGGER.warn("Cannot cancel a ticket with id: {}", ticketId, e);
            return false;
        }
    }
}
