package ua.epam.mishchenko.ticketbooking.service;

import ua.epam.mishchenko.ticketbooking.model.impl.EventImpl;

import java.time.LocalDate;
import java.util.List;

public interface EventService {

    EventImpl getEventById(long eventId);

    List<EventImpl> getEventsByTitle(String title, int pageSize, int pageNum);

    List<EventImpl> getEventsForDay(LocalDate day, int pageSize, int pageNum);

    EventImpl createEvent(EventImpl event);

    EventImpl updateEvent(EventImpl event);

    boolean deleteEvent(long eventId);
}
