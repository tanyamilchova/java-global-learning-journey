package cacheTests;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ua.epam.mishchenko.ticketbooking.model.Event;
import ua.epam.mishchenko.ticketbooking.model.Ticket;
import ua.epam.mishchenko.ticketbooking.model.User;
import ua.epam.mishchenko.ticketbooking.model.UserAccount;
import ua.epam.mishchenko.ticketbooking.service.EventService;
import ua.epam.mishchenko.ticketbooking.service.TicketService;
import ua.epam.mishchenko.ticketbooking.service.UserAccountService;
import ua.epam.mishchenko.ticketbooking.service.UserService;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CacheTest {
    private  final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(CacheTest.class);
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    UserService userService;

    @Autowired
    UserAccountService userAccountService;

    @Autowired
    TicketService ticketService;

    @Autowired
    EventService eventService;

    private Statistics getStatistics() {
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        Statistics stats = sessionFactory.getStatistics();
        stats.setStatisticsEnabled(true);
        stats.clear();
        return stats;
    }

    private void clearFirstLevelCache() {
        entityManagerFactory.createEntityManager().clear();
    }

    @Test
    public void shouldUseSecondLevelCacheWhenGetUserByID() {
        LOGGER.log(Level.INFO, "Test start");

        Statistics stats = getStatistics();
        stats.clear();

        User firstUser = userService.getUserById(1L);
        LOGGER.log(Level.INFO, "First load: " + firstUser);
        LOGGER.log(Level.INFO, "L2C stats after first load - Hits: " + stats.getSecondLevelCacheHitCount() +
                ", Misses: " + stats.getSecondLevelCacheMissCount() +
                ", Puts: " + stats.getSecondLevelCachePutCount());

        clearFirstLevelCache();


        User secondUser = userService.getUserById(1L);
        LOGGER.log(Level.INFO, "Second load: " + secondUser);
        LOGGER.log(Level.INFO, "L2C stats after second load - Hits: " + stats.getSecondLevelCacheHitCount() +
                ", Misses: " + stats.getSecondLevelCacheMissCount() +
                ", Puts: " + stats.getSecondLevelCachePutCount());
    }


    @Test
    public void shouldUseSecondLevelCacheWhenGetUserAccountByUserID() {

        LOGGER.log(Level.INFO, "Test cache UserAccount start");

        Statistics stats = getStatistics();
        stats.clear();

        UserAccount firstUserAccount = userAccountService.getUserAccountByUserId(1L);
        LOGGER.log(Level.INFO, "First load userAccount: " + firstUserAccount);
        LOGGER.log(Level.INFO, "L2C stats after first load of UserAccount - Hits: " + stats.getSecondLevelCacheHitCount() +
                ", Misses: " + stats.getSecondLevelCacheMissCount() +
                ", Puts: " + stats.getSecondLevelCachePutCount());

        clearFirstLevelCache();

        UserAccount secondUserAccount = userAccountService.getUserAccountByUserId(1L);
        LOGGER.log(Level.INFO, "Second load userAccount: " + secondUserAccount);
        LOGGER.log(Level.INFO, "L2C stats after second load of UserAccount- Hits: " + stats.getSecondLevelCacheHitCount() +
                ", Misses: " + stats.getSecondLevelCacheMissCount() +
                ", Puts: " + stats.getSecondLevelCachePutCount());
    }

    @Test
    public void shouldUseSecondLevelCacheWhenGetTicketByID() {
        Statistics stats = getStatistics();
        User user = userService.getUserById(1L);

        LOGGER.log(Level.DEBUG, "Test cache Tickets start");
        stats.clear();

        List<Ticket> firstTicket = ticketService.getBookedTickets(user, 10, 1);
        LOGGER.log(Level.INFO, "First load ticket list: " + firstTicket);
        LOGGER.log(Level.INFO, "L2C stats after first load of ticket list - Hits: " + stats.getSecondLevelCacheHitCount() +
                ", Misses: " + stats.getSecondLevelCacheMissCount() +
                ", Puts: " + stats.getSecondLevelCachePutCount());

        clearFirstLevelCache();

        List<Ticket> secondTicket = ticketService.getBookedTickets(user, 10, 1);
        LOGGER.log(Level.INFO, "Second load ticket list: " + secondTicket);
        LOGGER.log(Level.INFO, "L2C stats after second load of ticket list- Hits: " + stats.getSecondLevelCacheHitCount() +
                ", Misses: " + stats.getSecondLevelCacheMissCount() +
                ", Puts: " + stats.getSecondLevelCachePutCount());
    }

    @Test
    public void shouldUseSecondLevelCacheWhenGetEventsByID() {

        LOGGER.log(Level.INFO, "Test cache Events start");

        Statistics stats = getStatistics();
        stats.clear();

        Event firstEvent= eventService.getEventById(1L);
        LOGGER.log(Level.INFO, "First load event: " + firstEvent);
        LOGGER.log(Level.INFO, "L2C stats after first load of Event - Hits: " + stats.getSecondLevelCacheHitCount() +
                ", Misses: " + stats.getSecondLevelCacheMissCount() +
                ", Puts: " + stats.getSecondLevelCachePutCount());

        clearFirstLevelCache();

        Event secondEvent= eventService.getEventById(1L);
        LOGGER.log(Level.INFO, "Second load Event: " + secondEvent);
        LOGGER.log(Level.INFO, "L2C stats after second load of Event- Hits: " + stats.getSecondLevelCacheHitCount() +
                ", Misses: " + stats.getSecondLevelCacheMissCount() +
                ", Puts: " + stats.getSecondLevelCachePutCount());
    }
}
