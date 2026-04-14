package ua.epam.mishchenko.ticketbooking.dao;

import org.springframework.stereotype.Repository;
import ua.epam.mishchenko.ticketbooking.model.Event;

import java.util.List;

@Repository
public interface EventDAO {

    Event getById(long id);

    List<Event> getAll();

    Event insert(Event event);

    Event update(Event event);

    boolean delete(long eventId);
}
