package ua.epam.mishchenko.ticketbooking.model;

public interface UserAccount {

    void setUserId(long id);

    double getBalance();

    void setBalance(double balance);

    long getUserId();
}
