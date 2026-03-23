package ua.epam.mishchenko.ticketbooking.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.epam.mishchenko.ticketbooking.model.impl.EventImpl;

import java.time.LocalDate;

public interface EventRepository extends JpaRepository<EventImpl, Long> {

    Page<EventImpl> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<EventImpl> findByDate(LocalDate date, Pageable pageable);
}
