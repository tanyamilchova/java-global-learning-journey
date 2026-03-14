package ua.epam.mishchenko.ticketbooking.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ua.epam.mishchenko.ticketbooking.dao.impl.EventDAOImpl;
import ua.epam.mishchenko.ticketbooking.exception.DbException;
import ua.epam.mishchenko.ticketbooking.model.Event;
import ua.epam.mishchenko.ticketbooking.model.impl.EventImpl;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EventServiceImplTest {

    private EventServiceImpl eventService;

    @Mock
    private EventDAOImpl eventDAO;

    @Before
    public void setUp() {
        eventService = new EventServiceImpl();
        eventService.setEventDAO(eventDAO);
    }

    @Test
    public void getEventByIdWithExistsIdShouldBeOk() throws ParseException {
        long userId = 3L;
        Event expectedEvent = new EventImpl(userId, "Third event", LocalDate.of(2026, 1, 15));
        when(eventDAO.getById(userId)).thenReturn(expectedEvent);

        Event actualEvent = eventService.getEventById(userId);

        assertEquals(expectedEvent, actualEvent);
    }

    @Test
    public void getEventByIdWithExceptionShouldReturnNull() {
        when(eventDAO.getById(anyLong())).thenThrow(DbException.class);

        Event actualEvent = eventService.getEventById(10L);

        assertNull(actualEvent);
    }

    @Test
    public void getEventsByTitleWithExistsTitleShouldBeOk() throws ParseException {
        String title = "Third event";
        List<Event> expectedEvents = Arrays.asList(
                new EventImpl(3L, title, LocalDate.of(2022, 5, 16)),
                new EventImpl(5L, title, LocalDate.of(2022, 5, 25))
        );

        when(eventDAO.getEventsByTitle(eq(title), anyInt(), anyInt())).thenReturn(expectedEvents);

        List<Event> actualEvents = eventService.getEventsByTitle(title, 2, 1);

        assertEquals(expectedEvents, actualEvents);
    }

    @Test
    public void getEventsByTitleWithExceptionShouldReturnNull() {
        when(eventDAO.getEventsByTitle(anyString(), anyInt(), anyInt())).thenThrow(DbException.class);

        List<Event> actualEventsByTitle = eventService.getEventsByTitle("not exists title", 1, 1);

        assertNull(actualEventsByTitle);
    }

    @Test
    public void getEventsForDayWithExistsDayShouldBeOk() throws ParseException {
        LocalDate day = LocalDate.of(2022, 15, 5);
        List<Event> expectedEvents = Arrays.asList(
                new EventImpl(3L, "second event", LocalDate.of(2022, 5, 16)),
                new EventImpl(5L, "third event", LocalDate.of(2022, 5, 25))
        );

        Date dayAsDate = Date.from(day.atStartOfDay(ZoneId.systemDefault()).toInstant());

        when(eventDAO.getEventsForDay(eq(dayAsDate), anyInt(), anyInt())).thenReturn(expectedEvents);
        List<Event> actualEvents = eventService.getEventsForDay(day, 2, 1);

        assertTrue(expectedEvents.containsAll(actualEvents));
    }

    @Test
    public void getEventsForDayWithExceptionShouldReturnNull() throws ParseException {
        LocalDate day = LocalDate.of(2000, 5, 15);
        when(eventDAO.getEventsForDay(any(), anyInt(), anyInt())).thenThrow(DbException.class);

        List<Event> actualEventsForDay = eventService.getEventsForDay(day, 1, 1);

        assertNull(actualEventsForDay);
    }

    @Test
    public void createEventWithExceptionShouldReturnNull() {
        when(eventDAO.insert(any())).thenThrow(DbException.class);

        Event actualEvent = eventService.createEvent(null);

        assertNull(actualEvent);
    }

    @Test
    public void createEventWithExistsTitleAndEmailShouldReturnNull() throws ParseException {
        LocalDate eventDate = LocalDate.of(2022, 5, 15);
        EventImpl expectedEvent = new EventImpl(1L, "Second event", eventDate);
        when(eventDAO.insert(expectedEvent)).thenReturn(expectedEvent);

        Event actualEvent = eventService.createEvent(expectedEvent);

        assertEquals(expectedEvent, actualEvent);
    }

    @Test
    public void updateEventWithExistsEventShouldBeOk() throws ParseException {
        Event expectedEvent = new EventImpl(1L, "Second event", LocalDate.of(2022, 5, 15));
        when(eventDAO.update(any())).thenReturn(expectedEvent);

        Event actualEvent = eventService.updateEvent(expectedEvent);

        assertEquals(expectedEvent, actualEvent);
    }

    @Test
    public void updateEventWithExceptionShouldReturnNull() {
        when(eventDAO.update(any())).thenThrow(DbException.class);

        Event actualEvent = eventService.updateEvent(null);

        assertNull(actualEvent);
    }

    @Test
    public void deleteEventExistsEventShouldReturnTrue() {
        when(eventDAO.delete(anyLong())).thenReturn(true);

        boolean actualIsDeleted = eventService.deleteEvent(6L);

        assertTrue(actualIsDeleted);
    }

    @Test
    public void deleteEventWithExceptionShouldReturnFalse() {
        when(eventDAO.delete(anyLong())).thenThrow(DbException.class);

        boolean actualIsDeleted = eventService.deleteEvent(10L);

        assertFalse(actualIsDeleted);
    }
}