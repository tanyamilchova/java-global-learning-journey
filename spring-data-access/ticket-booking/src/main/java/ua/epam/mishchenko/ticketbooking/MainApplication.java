//package ua.epam.mishchenko.ticketbooking;
//
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.AnnotationConfigApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
//import ua.epam.mishchenko.ticketbooking.config.AppConfig;
//import ua.epam.mishchenko.ticketbooking.facade.BookingFacade;
//import ua.epam.mishchenko.ticketbooking.model.Event;
//import ua.epam.mishchenko.ticketbooking.model.Ticket;
//import ua.epam.mishchenko.ticketbooking.model.User;
//import ua.epam.mishchenko.ticketbooking.model.UserAccount;
//import ua.epam.mishchenko.ticketbooking.model.impl.EventImpl;
//import ua.epam.mishchenko.ticketbooking.model.impl.UserAccountImpl;
//import ua.epam.mishchenko.ticketbooking.model.impl.UserImpl;
//import ua.epam.mishchenko.ticketbooking.service.EventService;
//import ua.epam.mishchenko.ticketbooking.service.TicketService;
//import ua.epam.mishchenko.ticketbooking.service.UserAccountService;
//import ua.epam.mishchenko.ticketbooking.service.UserService;
//
//
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//
//
//public class MainApplication {
////    private static ApplicationContext context =
////            new ClassPathXmlApplicationContext("applicationContext.xml");
//
//    static AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
//
//    @Autowired
//    static EventService eventService;
//    @Autowired
//    static TicketService ticketService;
//    @Autowired
//    static UserAccountService userAccountService;
//    @Autowired
//    static UserService userService;
//
//
//    static BookingFacade bookingFacade = context.getBean(BookingFacade.class);
//
//    private static Event createEvent(String title, double price) {
////        EventService eventService =
////                context.getBean(EventService.class);
//
//
//        EventImpl event = new EventImpl();
//        event.setTitle(title);
//        event.setDate(LocalDate.now());
//        event.setTicketPrice(price);
//
//
//        eventService.createEvent(event);
//        return event;
//    }
//
//
//
//
//    private static void updateEvent(long id) {
//
//
//        EventService eventService =
//                context.getBean(EventService.class);
//
//
//        EventImpl eventUpdated = (((EventImpl)eventService.getEventById(id)));
//        eventUpdated.setTitle("Updated Concert");
//        eventUpdated.setTicketPrice(200);
//
//
//        eventService.createEvent(eventUpdated);
//    }
//
//
//    private static Event getEventById(long id) {
////        ApplicationContext context =
////                new ClassPathXmlApplicationContext("applicationContext.xml");
////        EventService eventService =
////                context.getBean(EventService.class);
//
//
//        return eventService.getEventById(id);
//    }
//
//
//    private static List<Event> getEventByTitle(String title, int pageSize, int pageNum) {
////        ApplicationContext context =
////                new ClassPathXmlApplicationContext("applicationContext.xml");
////        EventService eventService = context.getBean(EventService.class);
////
//
//        return eventService.getEventsByTitle(title, pageSize, pageNum);
//    }
//
//
//
//
//    private static boolean deleteEvent(int id) {
////        EventService eventService =
////                context.getBean(EventService.class);
////
//
//        return eventService.deleteEvent(id);
//    }
//
//
//
//
//
//
//    private static List<Event> getEventsByDay(String strDate, int size, int num) {
////        EventService eventService = context.getBean(EventService.class);
////
//
//        LocalDate localDate = LocalDate.parse(strDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//
//
//        return eventService.getEventsForDay(localDate, size, num);
//    }
//
//
//
//
//    //////////////////////////// TICKET  /////////////////////////
//
//
//    public static Ticket bookTicket( long userId, long eventId, int place, Ticket.Category category) {
////        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
////
////
////        TicketService ticketService = context.getBean(TicketService.class);
//        Ticket ticket = ticketService.bookTicket(userId, eventId, place, category);
//        System.out.println("Ticket successfully booked: " + ticket);
//        return ticket;
//    }
//
//
//
//
//    private static List<Ticket> getBookedTicketsByUser(User user, int pageSize, int pageNum) {
//
////
////        TicketService ticketService = context.getBean(TicketService.class);
//
//
//        List<Ticket> tickets = ticketService.getBookedTickets(user, pageSize, pageNum);
//
//
//        tickets.forEach(System.out::println);
//
//
//        return tickets;
//    }
//
//
//    private static List<Ticket> getBookedTicketsByEvent(Event event, int pageSize, int pageNum) {
//
////
////        TicketService ticketService = context.getBean(TicketService.class);
//
//
//        List<Ticket> tickets = ticketService.getBookedTickets(event, pageSize, pageNum);
//
//
//        tickets.forEach(System.out::println);
//
//
//        return tickets;
//    }
//
//
//    private static boolean cancelTicket(long ticketId) {
////
////
////        TicketService ticketService = context.getBean(TicketService.class);
//
//
//        boolean result = ticketService.cancelTicket(ticketId);
//
//
//        System.out.println("Ticket cancelled: " + result);
//
//
//        return result;
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//    ////////////////////////////////////////   USER    ///////////////////////////////
//
//
//
//
//    private static User createUser(String name, String email) {
////        UserService userService = context.getBean(UserService.class);
////
//
//        UserImpl user = new UserImpl();
//        user.setName(name);
//        user.setEmail(email);
//
//
//        userService.createUser(user);
//        return user;
//    }
//
//
////    private static User getUserById(long userId) {
//////
//////
//////        UserService userService = context.getBean(UserService.class);
////        return userService.getUserById(userId);
////    }
////
////
////    private static User getUserByEmail(String email) {
////        UserService userService = context.getBean(UserService.class);
////        return userService.getUserByEmail(email);
////    }
//
//
//    private static List<User> getAllUsers(int pageSize, int pageNum) {
////        UserService userService = context.getBean(UserService.class);
//        return userService.getAllUsers(pageSize, pageNum);
//    }
//
//
//    private static List<User> getUsersByName(String name) {
////        UserService userService = context.getBean(UserService.class);
//        return userService.getUsersByName(name, 1, 3);
//    }
//
//
//    private static User updateUser(User user) {
////        UserService userService = context.getBean(UserService.class);
//        return userService.updateUser(user);
//    }
//
//
//    private static boolean deleteUser(User user) {
////        UserService userService = context.getBean(UserService.class);
//        return userService.deleteUser(user.getId());
//    }
//
//
//
//
//
//
//    ////////////////////////////////////////   USER-ACCOUNT    ///////////////////////////////
//    private static UserAccount createUserAccount(long userId) {
////        UserAccountService userAccountService = context.getBean(UserAccountService.class);
//
//
//        UserAccount userAccount = userAccountService.createUserAccount(userId);
//
//
//        System.out.println("Created UserAccount: " + userAccount);
//        return userAccount;
//    }
//    private static UserAccount addFundsToUserAccount(long userId, long funds) {
////        UserAccountService userAccountService = context.getBean(UserAccountService.class);
//
//
//        UserAccount userAccount = userAccountService.addFunds(userId, funds);
//
//
//        System.out.println("Funded UserAccount: " + userAccount);
//        return userAccount;
//    }
//
//
//
//
//    private static UserAccount getUserAccount(long userId) {
////        UserAccountService userAccountService = context.getBean(UserAccountService.class);
//
//
//        UserAccount userAccount = userAccountService.getUserAccountByUserId(userId);
//
//
//        System.out.println("Found UserAccount: " + userAccount);
//        return userAccount;
//    }
//
//
//    private static UserAccount updateUserAccount(long userId, double newBalance) {
////        UserAccountService userAccountService = context.getBean(UserAccountService.class);
//
//
//        UserAccount userAccount = userAccountService.getUserAccountByUserId(userId);
//        userAccount.setBalance(newBalance);
//
//
//        UserAccount updated = userAccountService.updateUserAccount(userAccount);
//
//
//        System.out.println("Updated UserAccount: " + updated);
//        return updated;
//    }
//
//
//    private static void deleteUserAccount(long userId) {
////        UserAccountService userAccountService = context.getBean(UserAccountService.class);
//
//
//        boolean deleted = userAccountService.deleteUserAccount(userId);
//
//
//        System.out.println("UserAccount deleted: " + deleted);
//    }
//
//
//
//
//
//
//
//
//    public static void main(String[] args) {
//
////        BookingFacade bookingFacade = context.getBean(BookingFacade.class);
//        //////////////////////////////////////////////
///// Event
////        System.out.println(bookingFacade.createEvent(new EventImpl("Balley1", LocalDate.now())).toString());
////        System.out.println("getEventById >>>>>>>>>>>> " + bookingFacade.getEventById(113));
////        updateEvent(2);
////        System.out.println(deleteEvent(17));
////        System.out.println(getEventByTitle("Pop Concert", 5,1));
////
////        System.out.println(getEventsByDay("2026-03-11", 5, 1));
////
//
//
//        //////////////////////////////////////////////
//
//
//
//
/////User
////        System.out.println(bookingFacade.createUser(new UserImpl("User", "5userEmail@gmail.com")));
////        createUser("User", "userEmail@gmail.com");
////        UserService userService = context.getBean(UserService.class);
////
////
//
////        System.out.println(bookingFacade.createUser(new UserImpl("User1", "user1Email@gmail.com")));
////        System.out.println(bookingFacade.getUserByEmail("userEmail@gmail.com"));
////        System.out.println(bookingFacade.getUserByEmail("alice@example.com"));
////        System.out.println(bookingFacade.getUsersByName("Alice", 3, 2));
//        System.out.println(bookingFacade.getAllUsers(5, 2));
////
////        System.out.println(bookingFacade.getUsersByName("Alice", 3, 1));
////        System.out.println(bookingFacade.getUserByEmail("alice@example.com"));
//
//
//
//
//
//
////        User user = bookingFacade.createUser(new UserImpl("User" + incrementNum++, incrementNum++ +"userEmail@gmail.com"));
////        System.out.println(user);
////
////        System.out.println(bookingFacade.updateUser(user));
////        System.out.println(bookingFacade.deleteUser(user.getId()));
//
//
//
//
//
//
//
//
//        //////////////////////////////////////////////
//
//
///// TICKET
////        User user = bookingFacade.createUser(new UserImpl("User", "18userEmail@gmail.com"));
////        UserAccount userAccount = bookingFacade.createUserAccount(user.getId());
////        bookingFacade.addFunds(userAccount.getUserId(), 10000);
////        System.out.println(bookingFacade.bookTicket(user.getId(), 1,12, Ticket.Category.PREMIUM).toString());
////
////        Event event = bookingFacade.createEvent(new EventImpl("Concert", LocalDate.now()));
////        bookingFacade.addFunds(user.getId(), 10000);
////        Ticket ticket = bookingFacade.bookTicket(123, 2,12, Ticket.Category.BAR);
////         bookingFacade.cancelTicket(22);
//
//
////        System.out.println("---- Tickets by user ----");
////        User user1 = bookingFacade.getUserById(1).get();
////        System.out.println("................................." + user);
////        System.out.println("By email: " + bookingFacade.getUserByEmail("alice@example.com"));
////        System.out.println(bookingFacade.deleteUser(55));
////        System.out.println(getBookedTicketsByUser(user, 5, 1));
//
////
////        System.out.println("---- Tickets by event ----");
////        getBookedTicketsByEvent(event, 5, 1);
////
////        System.out.println("---- Cancel ticket ----");
////        cancelTicket(ticket.getId());
////////////////////////////////////////////////
//
//
///// USER ACCOUNT
//
//
////        User user1 = bookingFacade.createUser(new UserImpl("UserA", "26user9Email@gmail.com"));
////
////        System.out.println(bookingFacade.createUserAccount(user1.getId()));
//////
////        System.out.println(bookingFacade.getUserAccountByUserId(user1.getId()));
////        updateUserAccount(user.getId(), 500);
////        deleteUserAccount(user.getId());
//    }
//}
//
