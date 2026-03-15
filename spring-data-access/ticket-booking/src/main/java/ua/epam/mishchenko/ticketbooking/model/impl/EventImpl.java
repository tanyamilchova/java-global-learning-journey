package ua.epam.mishchenko.ticketbooking.model.impl;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ua.epam.mishchenko.ticketbooking.model.Event;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "events")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EventImpl implements Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "ticket_price")
    private double ticketPrice;


    public EventImpl() {}

    public EventImpl(String title, LocalDate date) {
        this.title = title;
        this.date = date;
    }

    public EventImpl(long id, String title, LocalDate date, double price) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.ticketPrice = price;
    }

    public EventImpl(String title, LocalDate date, double price) {
        this.title = title;
        this.date = date;
        this.ticketPrice = price;
    }

    public EventImpl(long id, String title, LocalDate date) {
        this.id = id;
        this.title = title;
        this.date = date;
    }

    public EventImpl(long id, String title, double ticketPrice) {
        this.id = id;
        this.title = title;
        this.ticketPrice = ticketPrice;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    @Override
    public double getTicketPrice() {
        return ticketPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventImpl event = (EventImpl) o;
        return id == event.id && Objects.equals(title, event.title) && Objects.equals(date, event.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, date);
    }

    @Override
    public String toString() {
        return "EventImpl{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", date=" + date +
                ", ticketPrice=" + ticketPrice +
                '}';
    }
}
