package ua.epam.mishchenko.ticketbooking.facade;

import org.springframework.stereotype.Component;
import ua.epam.mishchenko.ticketbooking.model.impl.Event;
import ua.epam.mishchenko.ticketbooking.model.impl.Ticket;
import ua.epam.mishchenko.ticketbooking.model.impl.User;
import ua.epam.mishchenko.ticketbooking.model.impl.UserAccount;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Groups together all operations related to tickets booking.
 * Created by maksym_govorischev.
 */
@Component
public interface BookingFacade {

    /**
     * Gets event by its id.
     * @return Event.
     */
    Event getEventById(long eventId);

    /**
     * Get list of events by matching title. Title is matched using 'contains' approach.
     * In case nothing was found, empty list is returned.
     * @param title Event title or it's part.
     * @param pageSize Pagination param. Number of events to return on a page.
     * @param pageNum Pagination param. Number of the page to return. Starts from 1.
     * @return List of events.
     */
    List<Event> getEventsByTitle(String title, int pageSize, int pageNum);

    /**
     * Get list of events for specified day.
     * In case nothing was found, empty list is returned.
     * @param day Date object from which day information is extracted.
     * @param pageSize Pagination param. Number of events to return on a page.
     * @param pageNum Pagination param. Number of the page to return. Starts from 1.
     * @return List of events.
     */
    List<Event> getEventsForDay(LocalDate day, int pageSize, int pageNum);

    /**
     * Creates new event. Event id should be auto-generated.
     * @param event Event data.
     * @return Created Event object.
     */
    Event createEvent(Event event);

    /**
     * Updates event using given data.
     * @param event Event data for update. Should have id set.
     * @return Updated Event object.
     */
    Event updateEvent(Event event);

    /**
     * Deletes event by its id.
     * @param eventId Event id.
     * @return Flag that shows whether event has been deleted.
     */
    boolean deleteEvent(long eventId);

    /**
     * Gets user by their id.
     * @param userId the id of the user to retrieve
     * @return Optional containing the User if found, or Optional.empty() if not found.
     */
    Optional<User> getUserById(long userId);

    /**
     * Retrieves a paginated list of all users.
     *
     * @param pageSize the number of users to return per page
     * @param pageNum the page number to retrieve (starting from 1)
     * @return a list of users for the specified page, or an empty list if none found
     */
     List<User> getAllUsers(int pageSize, int pageNum);

    /**
     * Gets user by their email. Email is strictly matched.
     * @param email the email to search for
     * @return Optional containing the User if found, or Optional.empty() if not found.
     */
    Optional<User> getUserByEmail(String email);

    /**
     * Get list of users by matching name. Name is matched using 'contains' approach.
     * In case nothing was found, empty list is returned.
     * @param name Users name or it's part.
     * @param pageSize Pagination param. Number of users to return on a page.
     * @param pageNum Pagination param. Number of the page to return. Starts from 1.
     * @return List of users.
     */
    List<User> getUsersByName(String name, int pageSize, int pageNum);

    /**
     * Creates new user. User id should be auto-generated.
     * @param user User data.
     * @return Created User object.
     */
    User createUser(User user);

    /**
     * Updates user using given data.
     * @param user User data for update. Should have id set.
     * @return Updated User object.
     */
    User updateUser(User user);

    /**
     * Deletes user by its id.
     * @param userId User id.
     * @return Flag that shows whether user has been deleted.
     */
    boolean deleteUser(long userId);

    /**
     * Book ticket for a specified event on behalf of specified user.
     * @param userId User Id.
     * @param eventId Event Id.
     * @param place Place number.
     * @param category Service category.
     * @return Booked ticket object.
     * @throws IllegalStateException if this place has already been booked.
     */
    Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category);

    /**
     * Get all booked tickets for specified user. Tickets should be sorted by event date in descending order.
     * @param user User
     * @param pageSize Pagination param. Number of tickets to return on a page.
     * @param pageNum Pagination param. Number of the page to return. Starts from 1.
     * @return List of Ticket objects.
     */
    List<Ticket> getBookedTickets(User user, int pageSize, int pageNum);

    /**
     * Get all booked tickets for specified event. Tickets should be sorted in by user email in ascending order.
     * @param event Event
     * @param pageSize Pagination param. Number of tickets to return on a page.
     * @param pageNum Pagination param. Number of the page to return. Starts from 1.
     * @return List of Ticket objects.
     */
    List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum);

    /**
     * Cancel ticket with a specified id.
     * @param ticketId Ticket id.
     * @return Flag whether anything has been canceled.
     */
    boolean cancelTicket(long ticketId);

    /**
     * Create a new user account for the specified user.
     *
     * @param userId User id for which the account will be created.
     * @return Created UserAccount object or null if creation failed.
     */
    UserAccount createUserAccount(long userId);

    /**
     * Get the user account by user id.
     *
     * @param userId User id whose account should be retrieved.
     * @return UserAccount associated with the specified user id or null if not found.
     */
    UserAccount getUserAccountByUserId(long userId);

    /**
     * Update an existing user account.
     *
     * @param userAccount UserAccount object containing updated information.
     * @return Updated UserAccount object or null if update failed.
     */
    UserAccount updateUserAccount(UserAccount userAccount);

    /**
     * Delete the user account for the specified user.
     *
     * @param userId User id whose account should be deleted.
     * @return Flag whether the user account has been successfully deleted.
     */
    boolean deleteUserAccount(long userId);


    /**
     * Adds the specified amount of funds to the user's account.
     * <p>
     * This method increases the balance of the {@link UserAccount} associated with the given user ID
     * by the provided amount. The amount must be positive.
     * </p>
     *
     * @param userId the ID of the user whose account will be credited
     * @param amount the amount to add to the user's account; must be positive
     * @return the updated {@link UserAccount} with the new balance
     * @throws IllegalArgumentException if {@code userId} is invalid or {@code amount} is not positive
     * @throws ua.epam.mishchenko.ticketbooking.exception.DbException if the user account cannot be found or updated due to a database error
     */
     UserAccount addFunds(long userId, double amount);
}
