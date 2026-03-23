package ua.epam.mishchenko.ticketbooking.service.impl;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.epam.mishchenko.ticketbooking.exception.DbException;
import ua.epam.mishchenko.ticketbooking.model.impl.EventImpl;
import ua.epam.mishchenko.ticketbooking.model.repository.EventRepository;
import ua.epam.mishchenko.ticketbooking.service.EventService;
import ua.epam.mishchenko.ticketbooking.validator.GenericValidator;
import ua.epam.mishchenko.ticketbooking.validator.Util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {
    private static final Logger LOGGER = LogManager.getLogger(EventServiceImpl.class);

    private final EventRepository eventRepository;

    private final GenericValidator genericValidator;


    @Autowired
    public EventServiceImpl(EventRepository eventRepository, GenericValidator genericValidator) {
        this.eventRepository = eventRepository;
        this.genericValidator = genericValidator;
    }

    @Override
    public EventImpl getEventById(long eventId) {
        genericValidator.validateId(eventId, "Event id");
        LOGGER.debug("Finding an event by id: {}", eventId);

        if (eventId <= 0) {
            LOGGER.warn("Invalid eventId provided: {}", eventId);
            throw new IllegalArgumentException("EventId must be positive: " + eventId);
        }

        return eventRepository.findById(eventId)
                .map(event -> {
                    LOGGER.info("Event with id {} successfully found", eventId);
                    return event;
                })
                .orElseThrow(() -> {
                    LOGGER.error("Event with id {} not found", eventId);
                    return new DbException("Event not found with id: " + eventId);
                });
    }

    @Override
    public List<EventImpl> getEventsByTitle(String title, int pageSize, int pageNum) {
        genericValidator.validateString(title, "Title");
        genericValidator.validatePagination(pageSize, pageNum);
        LOGGER.debug("Finding all events by title '{}' with page size {} and number of page {}", title, pageSize, pageNum);


        if (title == null || title.isBlank()) {
            LOGGER.warn("Invalid title provided: '{}'", title);
            throw new IllegalArgumentException("Title must not be null or empty");
        }
        if (pageSize <= 0 || pageNum < 0) {
            LOGGER.warn("Invalid pagination parameters: pageSize={}, pageNum={}", pageSize, pageNum);
            throw new IllegalArgumentException("Page size must be > 0 and page number >= 0");
        }

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        List<EventImpl> eventsByTitle = eventRepository.findByTitleContainingIgnoreCase(title, pageable).getContent();

        LOGGER.info("Found {} events by title '{}' on page {} with page size {}", eventsByTitle.size(), title, pageNum, pageSize);

        return eventsByTitle;
    }


    public List<EventImpl> getEventsForDay(LocalDate day, int pageSize, int pageNum) {
        Util.validateNotNull(day, "LocalDate");
        genericValidator.validatePagination(pageSize, pageNum);

        try {
            Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
            List<EventImpl> eventsImpl = eventRepository.findByDate(day, pageable).getContent();
            LOGGER.info("Found {} events for day {} on page {} with page size {}", eventsImpl.size(), day, pageNum, pageSize);

            return new ArrayList<>(eventsImpl);
        } catch (Exception exception) {
            LOGGER.error("Cannot find events for day {}", day, exception);
            throw new DbException("Error fetching events for day " + day, exception);
        }
    }


    @Override
    @Transactional
    public EventImpl createEvent(EventImpl event) {
        Util.validateNotNull(event, "Event");
        LOGGER.debug("Start creating an event: {}", event);

        try {
            EventImpl savedEvent = eventRepository.save( event);
            LOGGER.info("Successfully created the event: {}", savedEvent);
            return savedEvent;
        } catch (Exception exception) {
            LOGGER.error("Cannot create event: {}", event, exception);
            throw new DbException("Error creating event: " + event, exception);
        }
    }


    @Override
    public EventImpl updateEvent(EventImpl event) {
        Util.validateNotNull(event, "Event");
        LOGGER.debug("Start updating an event: {}", event);

        try {
            EventImpl updatedEvent = eventRepository.save(event);
            LOGGER.info("Successfully updated the event: {}", updatedEvent);
            return updatedEvent;
        } catch (Exception exception) {
            LOGGER.error("Cannot update event: {}", event, exception);
            throw new DbException("Error updating event: " + event, exception);
        }
    }

    @Override
    @Transactional
    public boolean deleteEvent(long eventId) {
        genericValidator.validateId(eventId, "Event id");
        LOGGER.debug("Start deleting an event with id: {}", eventId);

        if (eventId <= 0) {
            LOGGER.warn("Invalid eventId provided: {}", eventId);
            throw new IllegalArgumentException("EventId must be positive" + eventId);
        }

        return eventRepository.findById(eventId)
                .map(event -> {
                    eventRepository.delete(event);
                    LOGGER.info("Successfully deleted the event with id: {}", eventId);
                    return true;
                })
                .orElseGet(() -> {
                    LOGGER.warn("Event not found with id: {}", eventId);
                    return false;
                });
    }
}
