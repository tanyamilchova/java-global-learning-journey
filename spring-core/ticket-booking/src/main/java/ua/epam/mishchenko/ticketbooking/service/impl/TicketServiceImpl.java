package ua.epam.mishchenko.ticketbooking.service.impl;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.epam.mishchenko.ticketbooking.dao.EventDAO;
import ua.epam.mishchenko.ticketbooking.dao.UserAccountDAO;
import ua.epam.mishchenko.ticketbooking.dao.impl.TicketDAOImpl;
import ua.epam.mishchenko.ticketbooking.exception.DbException;
import ua.epam.mishchenko.ticketbooking.model.Event;
import ua.epam.mishchenko.ticketbooking.model.Ticket;
import ua.epam.mishchenko.ticketbooking.model.User;
import ua.epam.mishchenko.ticketbooking.model.UserAccount;
import ua.epam.mishchenko.ticketbooking.model.impl.TicketImpl;
import ua.epam.mishchenko.ticketbooking.service.TicketService;

import java.util.List;

public class TicketServiceImpl implements TicketService {

    private static final Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);

    private TicketDAOImpl ticketDAO;

    private EventDAO eventDAO;

    private UserAccountDAO userAccountDAO;

    @Transactional
    @Override
    public Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category) {
        logger.debug(
                "Start booking a ticket for user with id {}, event with id {}, place {}, category {}",
                userId, eventId, place, category);

        Event event = eventDAO.getById(eventId);
        UserAccount userAccount = userAccountDAO.getByUserId(userId);
        if (event == null) {
            logger.warn("Event {} not found", eventId);
            return null;
        }

        if (userAccount == null) {
            logger.warn("User account {} not found", userId);
            return null;
        }

        if(userAccount.getBalance() < event.getTicketPrice()){
            logger.warn("Insufficient funds for user {} to book ticket for event {}", userId, eventId);
            return null;
        }

        try {
            userAccount.setBalance(userAccount.getBalance() - event.getTicketPrice());
            userAccountDAO.update(userAccount);
            Ticket ticket = ticketDAO.insert(createNewTicket(userId, eventId, place, category));

            logger.debug("Successfully booking of the ticket: {}", ticket);

            return ticket;
        } catch (DbException e) {
            logger.warn(
                    "Can not to book a ticket for user with id {}, event with id {}, place {}, category {}",
                    userId, eventId, place, category, e);
            return null;
        }
    }

    private Ticket createNewTicket(long userId, long eventId, int place, Ticket.Category category) {
        return new TicketImpl(userId, eventId, place, category);
    }

    @Override
    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
        logger.debug(
                "Finding all booked tickets by user {} with page size {} and number of page {}",
                user, pageSize, pageNum);

        try {
            List<Ticket> ticketsByUser = ticketDAO.getAllByUser(user, pageSize, pageNum);

            logger.debug(
                    "All booked tickets successfully found by user {} with page size {} and number of page {}",
                    user, pageSize, pageNum);

            return ticketsByUser;
        } catch (DbException e) {
            logger.warn("Can not to find a list of booked tickets by user '{}'", user, e);
            return null;
        }
    }

    @Override
    public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
        logger.debug(
                "Finding all booked tickets by event {} with page size {} and number of page {}",
                event, pageSize, pageNum);

        try {
            List<Ticket> ticketsByUser = ticketDAO.getAllByEvent(event, pageSize, pageNum);

            logger.debug(
                    "All booked tickets successfully found by event {} with page size {} and number of page {}",
                    event, pageSize, pageNum);

            return ticketsByUser;
        } catch (DbException e) {
            logger.warn("Can not to find a list of booked tickets by event '{}'", event, e);
            return null;
        }
    }


    @Override
    public boolean cancelTicket(long ticketId) {
        logger.debug("Start canceling a ticket with id: {}", ticketId);

        try {
            boolean isRemoved = ticketDAO.delete(ticketId);

            logger.debug("Successfully canceling of the ticket with id: {}", ticketId);

            return isRemoved;
        } catch (DbException e) {
            logger.warn("Can not to cancel a ticket with id: {}", ticketId, e);
            return false;
        }
    }

    @Override
    public Ticket getTicketById(long ticketId) {
        try {
            return ticketDAO.getById(ticketId);
        } catch (DbException e) {
            logger.warn("Cannot find ticket with id: {}", ticketId, e);
            return null;
        }
    }

    public void setTicketDAO(TicketDAOImpl ticketDAO) {
        this.ticketDAO = ticketDAO;
    }

    public void setEventDAO(EventDAO eventDAO) {
        this.eventDAO = eventDAO;
    }

    public void setUserAccountDAO(UserAccountDAO userAccountDAO) {
        this.userAccountDAO = userAccountDAO;
    }
}