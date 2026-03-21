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
import ua.epam.mishchenko.ticketbooking.config.AppConfig;
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
import java.util.Optional;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
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
    LOGGER.info("Test start");

    Statistics stats = getStatistics();
    stats.clear();

    Optional<User> firstUserOpt = userService.getUserById(1L);
    assertTrue(firstUserOpt.isPresent());
    User firstUser = firstUserOpt.get();
    LOGGER.info("First load: {}", firstUser);
    LOGGER.info("L2C stats after first load - Hits: {}, Misses: {}, Puts: {}",
            stats.getSecondLevelCacheHitCount(),
            stats.getSecondLevelCacheMissCount(),
            stats.getSecondLevelCachePutCount());

    clearFirstLevelCache();

    Optional<User> secondUserOpt = userService.getUserById(1L);
    assertTrue(secondUserOpt.isPresent());
    User secondUser = secondUserOpt.get();
    LOGGER.info("Second load: {}", secondUser);
    LOGGER.info("L2C stats after second load - Hits: {}, Misses: {}, Puts: {}",
            stats.getSecondLevelCacheHitCount(),
            stats.getSecondLevelCacheMissCount(),
            stats.getSecondLevelCachePutCount());
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

        Optional<User> userOpt = userService.getUserById(1L);
        assertTrue(userOpt.isPresent());
        User user = userOpt.get();

        LOGGER.debug("Test cache Tickets start");
        stats.clear();

        List<Ticket> firstTicket = ticketService.getBookedTickets(user, 10, 1);
        LOGGER.info("First load ticket list: {}", firstTicket);
        LOGGER.info("L2C stats after first load of ticket list - Hits: {}, Misses: {}, Puts: {}",
                stats.getSecondLevelCacheHitCount(),
                stats.getSecondLevelCacheMissCount(),
                stats.getSecondLevelCachePutCount());

        clearFirstLevelCache();

        List<Ticket> secondTicket = ticketService.getBookedTickets(user, 10, 1);
        LOGGER.info("Second load ticket list: {}", secondTicket);
        LOGGER.info("L2C stats after second load of ticket list - Hits: {}, Misses: {}, Puts: {}",
                stats.getSecondLevelCacheHitCount(),
                stats.getSecondLevelCacheMissCount(),
                stats.getSecondLevelCachePutCount());
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
