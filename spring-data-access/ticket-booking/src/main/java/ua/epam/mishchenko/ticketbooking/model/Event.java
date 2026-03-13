package ua.epam.mishchenko.ticketbooking.model;

import java.time.LocalDate;

/**
 * Created by maksym_govorischev.
 */
public interface Event {
    /**
     * Event id. UNIQUE.
     * @return Event Id
     */
    long getId();
    void setId(long id);
    String getTitle();
    void setTitle(String title);
    LocalDate getDate();
    void setDate(LocalDate date);
    double getTicketPrice();
    void setTicketPrice(double ticketPrice);
}
