package ua.epam.mishchenko.ticketbooking.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import ua.epam.mishchenko.ticketbooking.model.impl.TicketImpl;

import javax.persistence.QueryHint;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<TicketImpl, Long> {
    Optional<TicketImpl>findById(long id);
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    Page<TicketImpl> getAllByUserId(Long userId, Pageable pageable);
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    Page<TicketImpl> getAllByEventId(Long eventId, Pageable pageable);
    void deleteById(Long ticketId);
}