package ua.epam.mishchenko.ticketbooking.service;

import ua.epam.mishchenko.ticketbooking.model.impl.EventImpl;
import ua.epam.mishchenko.ticketbooking.model.impl.TicketImpl;
import ua.epam.mishchenko.ticketbooking.model.impl.UserImpl;

import java.util.List;

public interface TicketService {

    TicketImpl bookTicket(long userId, long eventId, int place, TicketImpl.Category category);

    List<TicketImpl> getBookedTickets(UserImpl user, int pageSize, int pageNum);

    List<TicketImpl> getBookedTickets(EventImpl event, int pageSize, int pageNum);

    boolean cancelTicket(long ticketId);
}