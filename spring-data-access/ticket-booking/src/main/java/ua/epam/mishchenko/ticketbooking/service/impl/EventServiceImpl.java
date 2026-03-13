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
import ua.epam.mishchenko.ticketbooking.model.impl.EventImpl;
import ua.epam.mishchenko.ticketbooking.model.repository.EventRepository;
import ua.epam.mishchenko.ticketbooking.service.EventService;
import ua.epam.mishchenko.ticketbooking.utils.Util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    private static final Logger LOGGER = LogManager.getLogger(EventServiceImpl.class);

    @Override
    public Event getEventById(long eventId) {
        Util.validateLong(eventId);
        LOGGER.log(Level.DEBUG, "Finding an event by id: {}", eventId);

        if (eventId <= 0) {
            LOGGER.log(Level.WARN, "Invalid eventId provided: {}", eventId);
            throw new IllegalArgumentException("EventId must be positive: " + eventId);
        }

        return eventRepository.findById(eventId)
                .map(event -> {
                    LOGGER.log(Level.DEBUG, "Event with id {} successfully found", eventId);
                    return event;
                })
                .orElseThrow(() -> {
                    LOGGER.log(Level.WARN, "Event with id {} not found", eventId);
                    return new DbException("Event not found with id: " + eventId);
                });
    }

    @Override
    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        Util.validateString(title);
        Util.validatePagination(pageSize, pageNum);
        LOGGER.log(Level.DEBUG,
                "Finding all events by title '{}' with page size {} and number of page {}",
                title, pageSize, pageNum);

        if (title == null || title.isBlank()) {
            LOGGER.log(Level.WARN, "Invalid title provided: '{}'", title);
            throw new IllegalArgumentException("Title must not be null or empty");
        }
        if (pageSize <= 0 || pageNum < 0) {
            LOGGER.log(Level.WARN, "Invalid pagination parameters: pageSize={}, pageNum={}", pageSize, pageNum);
            throw new IllegalArgumentException("Page size must be > 0 and page number >= 0");
        }

        Pageable pageable = PageRequest.of(pageNum, pageSize);
        List<Event> eventsByTitle = eventRepository.findByTitleContainingIgnoreCase(title, pageable).getContent();

        LOGGER.log(Level.DEBUG,
                "Found {} events by title '{}' on page {} with page size {}",
                eventsByTitle.size(), title, pageNum, pageSize);

        return eventsByTitle;
    }


    public List<Event> getEventsForDay(LocalDate day, int pageSize, int pageNum) {
        Util.validateNotNull(day, "LocalDate");
        Util.validatePagination(pageSize, pageNum);

        try {
            Pageable pageable = PageRequest.of(pageNum, pageSize);
            List<EventImpl> eventsImpl = eventRepository.findByDate(day, pageable).getContent();
            return new ArrayList<>(eventsImpl);
        } catch (Exception exception) {
            LOGGER.log(Level.WARN, "Cannot find events for day {}", day, exception);
            throw new DbException("Error fetching events for day " + day, exception);
        }
    }


    @Override
    @Transactional
    public Event createEvent(Event event) {
        Util.validateNotNull(event, "Event");
        LOGGER.log(Level.DEBUG, "Start creating an event: {}", event);

        if (!(event instanceof EventImpl)) {
            throw new IllegalArgumentException("Event must be of type EventImpl");
        }

        try {
            Event savedEvent = eventRepository.save((EventImpl) event);
            LOGGER.log(Level.DEBUG, "Successfully created the event: {}", savedEvent);
            return savedEvent;
        } catch (Exception exception) {
            LOGGER.log(Level.WARN, "Cannot create event: {}", event, exception);
            throw new DbException("Error creating event: " + event, exception);
        }
    }


    @Override
    public Event updateEvent(Event event) {
        Util.validateNotNull(event, "Event");
        LOGGER.log(Level.DEBUG, "Start updating an event: {}", event);

        if (!(event instanceof EventImpl)) {
            throw new IllegalArgumentException("Event must be of type EventImpl");
        }

        try {
            Event updatedEvent = eventRepository.save((EventImpl) event);
            LOGGER.log(Level.DEBUG, "Successfully updated the event: {}", updatedEvent);
            return updatedEvent;
        } catch (Exception exception) {
            LOGGER.log(Level.WARN, "Cannot update event: {}", event, exception);
            throw new DbException("Error updating event: " + event, exception);
        }
    }

    @Override
    @Transactional
    public boolean deleteEvent(long eventId) {
        Util.validateId(eventId);
        LOGGER.log(Level.DEBUG, "Start deleting an event with id: {}", eventId);

        if (eventId <= 0) {
            LOGGER.log(Level.WARN, "Invalid eventId provided: {}", eventId);
            throw new IllegalArgumentException("EventId must be positive" + eventId);
        }

        return eventRepository.findById(eventId)
                .map(event -> {
                    eventRepository.delete(event);
                    LOGGER.log(Level.DEBUG, "Successfully deleted the event with id: {}", eventId);
                    return true;
                })
                .orElseGet(() -> {
                    LOGGER.log(Level.WARN, "Event not found with id: {}", eventId);
                    return false;
                });
    }
}
