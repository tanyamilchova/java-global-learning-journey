package tests;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ua.epam.mishchenko.ticketbooking.dao.UserDAO;
import ua.epam.mishchenko.ticketbooking.exception.DbException;
import ua.epam.mishchenko.ticketbooking.model.Event;
import ua.epam.mishchenko.ticketbooking.model.impl.EventImpl;
import ua.epam.mishchenko.ticketbooking.model.repository.EventRepository;
import ua.epam.mishchenko.ticketbooking.service.impl.EventServiceImpl;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class EventServiceTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventServiceImpl eventService;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getEventByIdShouldReturnEvent() {
        long eventId = 1L;
        EventImpl event = new EventImpl();
        event.setId(eventId);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        Event result = eventService.getEventById(eventId);

        assertNotNull(result);
        assertEquals(eventId, result.getId());
        verify(eventRepository).findById(eventId);
    }

    @Test
    public void getEventByIdShouldThrowWhenEventIdInvalid() {
        long invalidId = -1L;

        assertThrows(IllegalArgumentException.class, () -> {
            eventService.getEventById(invalidId);
        });

        verifyNoInteractions(eventRepository);
    }

    @Test
    public void getEventByIdShouldThrowWhenEventNotFound() {
        long eventId = 1L;

        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        assertThrows(DbException.class, () -> {
            eventService.getEventById(eventId);
        });

        verify(eventRepository).findById(eventId);
    }

    @Test
    public void getEventsByTitleShouldReturnList() {
        String title = "concert";
        int pageSize = 2;
        int pageNum = 1;

        List<Event> events = List.of(new EventImpl(), new EventImpl());
        Page<Event> page = new PageImpl<>(events);

        when(eventRepository.findByTitleContainingIgnoreCase(eq(title), any(Pageable.class)))
                .thenReturn(page);

        List<Event> result = eventService.getEventsByTitle(title, pageSize, pageNum);

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(eventRepository).findByTitleContainingIgnoreCase(eq(title), any(Pageable.class));
    }

    @Test
    public void getEventsByTitleShouldReturnEmptyList() {
        String title = "unknown";
        int pageSize = 5;
        int pageNum = 1;

        Page<Event> page = new PageImpl<>(Collections.emptyList());

        when(eventRepository.findByTitleContainingIgnoreCase(eq(title), any(Pageable.class)))
                .thenReturn(page);

        List<Event> result = eventService.getEventsByTitle(title, pageSize, pageNum);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(eventRepository).findByTitleContainingIgnoreCase(eq(title), any(Pageable.class));
    }

    @Test
    public void getEventsByTitleShouldThrowWhenTitleInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            eventService.getEventsByTitle("  ", 2, 0);
        });

        verifyNoInteractions(eventRepository);
    }

    @Test
    public void getEventsByTitleShouldThrowWhenPaginationInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            eventService.getEventsByTitle("concert", 0, 0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            eventService.getEventsByTitle("concert", 2, -1);
        });

        verifyNoInteractions(eventRepository);
    }

    @Test
    public void getEventsForDayShouldReturnList() {
        LocalDate day = LocalDate.of(2026, 3, 13);
        int pageSize = 2;
        int pageNum = 1;

        List<EventImpl> eventsImpl = List.of(new EventImpl(), new EventImpl());
        Page<EventImpl> page = new PageImpl<>(eventsImpl);

        when(eventRepository.findByDate(eq(day), any(Pageable.class)))
                .thenReturn(page);

        List<Event> result = eventService.getEventsForDay(day, pageSize, pageNum);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.get(1) instanceof EventImpl);

        verify(eventRepository).findByDate(eq(day), any(Pageable.class));
    }

    @Test
    public void getEventsForDayShouldThrowWhenDayIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                eventService.getEventsForDay(null, 5, 0)
        );

        verifyNoInteractions(eventRepository);
    }

    @Test
    public void getEventsForDayShouldThrowWhenPaginationInvalid() {
        LocalDate day = LocalDate.of(2026, 3, 13);

        assertThrows(IllegalArgumentException.class, () ->
                eventService.getEventsForDay(day, 0, 0)
        );

        assertThrows(IllegalArgumentException.class, () ->
                eventService.getEventsForDay(day, 5, -1)
        );

        verifyNoInteractions(eventRepository);
    }

    @Test
    public void createEventShouldReturnSavedEvent_thenReturn() {
        EventImpl event = new EventImpl();
        event.setTitle("Concert");

        EventImpl savedEvent = new EventImpl();
        savedEvent.setTitle("Concert");

        when(eventRepository.save(any(EventImpl.class))).thenReturn(savedEvent);

        Event result = eventService.createEvent(event);

        assertNotNull(result);
        assertEquals("Concert", result.getTitle());
        verify(eventRepository).save(event);
    }

    @Test
    public void createEventShouldThrowWhenEventIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            eventService.createEvent(null);
        });

        verifyNoInteractions(eventRepository);
    }

    @Test
    public void createEventShouldThrowWhenEventNotEventImpl() {
        Event event = mock(Event.class); // Not EventImpl

        assertThrows(IllegalArgumentException.class, () -> {
            eventService.createEvent(event);
        });

        verifyNoInteractions(eventRepository);
    }

    @Test
    public void createEventShouldThrowDbExceptionWhenRepositoryFails() {
        EventImpl event = new EventImpl();
        event.setTitle("Concert");

        when(eventRepository.save(any(EventImpl.class)))
                .thenThrow(new RuntimeException("DB error"));

        DbException exception = assertThrows(DbException.class, () -> {
            eventService.createEvent(event);
        });

        assertTrue(exception.getMessage().contains("Error creating event"));
        verify(eventRepository).save(event);
    }

    @Test
    public void updateEventShouldReturnUpdatedEvent() {
        EventImpl event = new EventImpl();
        event.setTitle("Concert");

        EventImpl updatedEvent = new EventImpl();
        updatedEvent.setTitle("Concert Updated");

        when(eventRepository.save(any(EventImpl.class))).thenReturn(updatedEvent);

        Event result = eventService.updateEvent(event);

        assertNotNull(result);
        assertEquals("Concert Updated", result.getTitle());
        verify(eventRepository).save(event);
    }

    @Test
    public void updateEventShouldThrowWhenEventIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            eventService.updateEvent(null);
        });

        verifyNoInteractions(eventRepository);
    }

    @Test
    public void updateEventShouldThrowWhenEventNotEventImpl() {
        Event event = mock(Event.class);

        assertThrows(IllegalArgumentException.class, () -> {
            eventService.updateEvent(event);
        });

        verifyNoInteractions(eventRepository);
    }

    @Test
    public void updateEventShouldThrowDbExceptionWhenRepositoryFails() {
        EventImpl event = new EventImpl();
        event.setTitle("Concert");

        when(eventRepository.save(any(EventImpl.class)))
                .thenThrow(new RuntimeException("DB error"));

        DbException exception = assertThrows(DbException.class, () -> {
            eventService.updateEvent(event);
        });

        assertTrue(exception.getMessage().contains("Error updating event"));
        verify(eventRepository).save(event);
    }


    @Test
    public void deleteEventShouldReturnTrueWhenEventExists() {
        long eventId = 1L;
        EventImpl event = new EventImpl();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        boolean result = eventService.deleteEvent(eventId);

        assertTrue(result);
        verify(eventRepository).findById(eventId);
        verify(eventRepository).delete(event);
    }

    @Test
    public void deleteEventShouldReturnFalseWhenEventNotFound() {
        long eventId = 2L;

        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        boolean result = eventService.deleteEvent(eventId);

        assertFalse(result);
        verify(eventRepository).findById(eventId);
        verify(eventRepository, never()).delete(any());
    }

    @Test
    public void deleteEventShouldThrowWhenEventIdInvalid() {
        long invalidId = 0L;

        assertThrows(IllegalArgumentException.class, () -> {
            eventService.deleteEvent(invalidId);
        });

        verifyNoInteractions(eventRepository);
    }
}
