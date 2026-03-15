package ua.epam.mishchenko.ticketbooking.model.impl;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ua.epam.mishchenko.ticketbooking.model.Ticket;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "tickets")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TicketImpl implements Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "place")
    private int place;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Category category;

    public TicketImpl() {}

    public TicketImpl(Long id, Long userId, Long eventId, int place, Category category) {
        this.id = id;
        this.userId = userId;
        this.eventId = eventId;
        this.place = place;
        this.category = category;
    }

    public TicketImpl(Long userId, Long eventId, int place, Category category) {
        this.userId = userId;
        this.eventId = eventId;
        this.place = place;
        this.category = category;
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
    public long getEventId() {
        return eventId;
    }

    @Override
    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    @Override
    public long getUserId() {
        return userId;
    }

    @Override
    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public Category getCategory() {
        return category;
    }

    @Override
    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public int getPlace() {
        return place;
    }

    @Override
    public void setPlace(int place) {
        this.place = place;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketImpl ticket = (TicketImpl) o;
        return place == ticket.place && Objects.equals(id, ticket.id) && Objects.equals(userId, ticket.userId) && Objects.equals(eventId, ticket.eventId) && category == ticket.category;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, eventId, place, category);
    }

    @Override
    public String toString() {
        return "{" +
                "'id' : " + id +
                ", 'userId' : " + userId +
                ", 'eventId' : " + eventId +
                ", 'place' : " + place +
                ", 'category' : '" + category +
                "'}";
    }
}
