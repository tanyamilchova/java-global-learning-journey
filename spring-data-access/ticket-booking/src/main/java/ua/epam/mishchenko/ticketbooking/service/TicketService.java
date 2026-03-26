package ua.epam.mishchenko.ticketbooking.service;

import ua.epam.mishchenko.ticketbooking.model.impl.Event;
import ua.epam.mishchenko.ticketbooking.model.impl.Ticket;
import ua.epam.mishchenko.ticketbooking.model.impl.User;

import java.util.List;

public interface TicketService {

    Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category);

    List<Ticket> getBookedTickets(User user, int pageSize, int pageNum);

    List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum);

    boolean cancelTicket(long ticketId);

    Ticket createTicket(long userId, long eventId, int place, Ticket.Category category);
}