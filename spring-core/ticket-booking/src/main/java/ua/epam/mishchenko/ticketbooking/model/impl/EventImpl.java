package ua.epam.mishchenko.ticketbooking.model.impl;

import jakarta.persistence.*;
import ua.epam.mishchenko.ticketbooking.model.Event;

import java.util.Date;
import java.util.Objects;

import static ua.epam.mishchenko.ticketbooking.utils.Constants.DATE_FORMATTER;

@Entity
@Table(name = "events")
public class EventImpl implements Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title")
    private String title;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date")
    private Date date;

    @Column(name = "ticket_price")
    private double ticketPrice;


    public EventImpl() {}

    public EventImpl(String title, Date date) {
        this.title = title;
        this.date = date;
    }

    public EventImpl(long id, String title, Date date, double price) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.ticketPrice = price;
    }

    public EventImpl(String title, Date date, double price) {
        this.title = title;
        this.date = date;
        this.ticketPrice = price;
    }

    public EventImpl(long id, String title, Date date) {
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
    public Date getDate() {
        return date;
    }

    @Override
    public void setDate(Date date) {
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
        return "{" +
                "'id' : " + id +
                ", 'title' : '" + title + '\'' +
                ", 'date' : '" + DATE_FORMATTER.format(date) +
                "'}";
    }
}
