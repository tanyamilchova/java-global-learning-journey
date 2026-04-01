package ua.epam.mishchenko.ticketbooking.dao;

import org.springframework.stereotype.Repository;
import ua.epam.mishchenko.ticketbooking.model.Ticket;

import java.util.List;

@Repository
public interface TicketDAO {

    Ticket getById(long id);

    List<Ticket> getAll();

    Ticket insert(Ticket ticket);

    Ticket update(Ticket ticket);

    boolean delete(long ticketId);
}
