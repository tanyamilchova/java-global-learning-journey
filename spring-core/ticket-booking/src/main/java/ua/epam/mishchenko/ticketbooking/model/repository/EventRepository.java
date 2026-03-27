package ua.epam.mishchenko.ticketbooking.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.epam.mishchenko.ticketbooking.model.impl.Event;

import java.time.LocalDate;

public interface EventRepository extends JpaRepository<Event, Long> {

    Page<Event> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Event> findByDate(LocalDate date, Pageable pageable);
}
