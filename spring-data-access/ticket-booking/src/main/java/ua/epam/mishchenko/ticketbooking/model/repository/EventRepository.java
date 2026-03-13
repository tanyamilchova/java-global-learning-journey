package ua.epam.mishchenko.ticketbooking.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import ua.epam.mishchenko.ticketbooking.model.Event;
import ua.epam.mishchenko.ticketbooking.model.impl.EventImpl;

import java.time.LocalDate;

public interface EventRepository extends CrudRepository<EventImpl, Long> {

    Page<Event> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<EventImpl> findByDate(LocalDate date, Pageable pageable);
}
