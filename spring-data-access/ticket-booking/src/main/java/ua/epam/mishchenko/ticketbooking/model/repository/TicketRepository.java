package ua.epam.mishchenko.ticketbooking.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import ua.epam.mishchenko.ticketbooking.model.impl.TicketImpl;

public interface TicketRepository extends CrudRepository<TicketImpl, Long> {

Page<TicketImpl> getAllByUserId(Long userId, Pageable pageable);

    Page<TicketImpl> getAllByEventId(Long eventId, Pageable pageable);
    void deleteById(Long ticketId);
}
