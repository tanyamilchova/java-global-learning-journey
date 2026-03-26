package ua.epam.mishchenko.ticketbooking.service.impl;


import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.epam.mishchenko.ticketbooking.exception.DbException;
import ua.epam.mishchenko.ticketbooking.model.impl.Event;
import ua.epam.mishchenko.ticketbooking.model.repository.EventRepository;
import ua.epam.mishchenko.ticketbooking.service.EventService;
import ua.epam.mishchenko.ticketbooking.validator.GenericValidator;
import ua.epam.mishchenko.ticketbooking.validator.Util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    private final GenericValidator genericValidator;


    @Autowired
    public EventServiceImpl(EventRepository eventRepository, GenericValidator genericValidator) {
        this.eventRepository = eventRepository;
        this.genericValidator = genericValidator;
    }

    @Override
    public Event getEventById(long eventId) {
        genericValidator.validateId(eventId, "Event id");
        log.debug("Finding an event by id: {}", eventId);

        if (eventId <= 0) {
            log.warn("Invalid eventId provided: {}", eventId);
            throw new IllegalArgumentException("EventId must be positive: " + eventId);
        }

        return eventRepository.findById(eventId)
                .map(event -> {
                    log.info("Event with id {} successfully found", eventId);
                    return event;
                })
                .orElseThrow(() -> {
                    log.error("Event with id {} not found", eventId);
                    return new DbException("Event not found with id: " + eventId);
                });
    }

    @Override
    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        genericValidator.validateString(title, "Title");
        genericValidator.validatePagination(pageSize, pageNum);
        log.debug("Finding all events by title '{}' with page size {} and number of page {}", title, pageSize, pageNum);


        if (title == null || title.isBlank()) {
            log.warn("Invalid title provided: '{}'", title);
            throw new IllegalArgumentException("Title must not be null or empty");
        }
        if (pageSize <= 0 || pageNum < 0) {
            log.warn("Invalid pagination parameters: pageSize={}, pageNum={}", pageSize, pageNum);
            throw new IllegalArgumentException("Page size must be > 0 and page number >= 0");
        }

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        List<Event> eventsByTitle = eventRepository.findByTitleContainingIgnoreCase(title, pageable).getContent();

        log.info("Found {} events by title '{}' on page {} with page size {}", eventsByTitle.size(), title, pageNum, pageSize);

        return eventsByTitle;
    }


    public List<Event> getEventsForDay(LocalDate day, int pageSize, int pageNum) {
        Util.validateNotNull(day, "LocalDate");
        genericValidator.validatePagination(pageSize, pageNum);

        try {
            Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
            List<Event> eventsImpl = eventRepository.findByDate(day, pageable).getContent();
            log.info("Found {} events for day {} on page {} with page size {}", eventsImpl.size(), day, pageNum, pageSize);

            return new ArrayList<>(eventsImpl);
        } catch (Exception exception) {
            log.error("Cannot find events for day {}", day, exception);
            throw new DbException("Error fetching events for day " + day, exception);
        }
    }


    @Override
    @Transactional
    public Event createEvent(Event event) {
        Util.validateNotNull(event, "Event");
        log.debug("Start creating an event: {}", event);

        try {
            Event savedEvent = eventRepository.save( event);
            log.info("Successfully created the event: {}", savedEvent);
            return savedEvent;
        } catch (Exception exception) {
            log.error("Cannot create event: {}", event, exception);
            throw new DbException("Error creating event: " + event, exception);
        }
    }


    @Override
    @Transactional
    public Event updateEvent(Event event) {
        Util.validateNotNull(event, "Event");
        log.debug("Start updating an event: {}", event);

        try {
            Event updatedEvent = eventRepository.save(event);
            log.info("Successfully updated the event: {}", updatedEvent);
            return updatedEvent;
        } catch (Exception exception) {
            log.error("Cannot update event: {}", event, exception);
            throw new DbException("Error updating event: " + event, exception);
        }
    }

    @Override
    @Transactional
    public boolean deleteEvent(long eventId) {
        genericValidator.validateId(eventId, "Event id");
        log.debug("Start deleting an event with id: {}", eventId);

        if (eventId <= 0) {
            log.warn("Invalid eventId provided: {}", eventId);
            throw new IllegalArgumentException("EventId must be positive" + eventId);
        }

        return eventRepository.findById(eventId)
                .map(event -> {
                    eventRepository.delete(event);
                    log.info("Successfully deleted the event with id: {}", eventId);
                    return true;
                })
                .orElseGet(() -> {
                    log.warn("Event not found with id: {}", eventId);
                    return false;
                });
    }
}
