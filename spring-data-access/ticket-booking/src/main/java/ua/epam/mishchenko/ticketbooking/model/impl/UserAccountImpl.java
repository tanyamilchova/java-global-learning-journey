package ua.epam.mishchenko.ticketbooking.model.impl;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import ua.epam.mishchenko.ticketbooking.model.UserAccount;

import java.util.Objects;

@Entity
@Table(name = "user_accounts")
public class UserAccountImpl implements UserAccount {

    @Id
    @Column(name = "user_id")
    private long userId;

    @Column(name = "balance")
    private double balance;


    public UserAccountImpl() {
    }
    public UserAccountImpl(long userId) {
        this.userId = userId;
    }

    public UserAccountImpl(long userId, double balance) {
        this.userId = userId;
        this.balance = balance;
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
    public double getBalance() {
        return balance;
    }

    @Override
    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccountImpl)) return false;
        UserAccountImpl that = (UserAccountImpl) o;
        return Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userId);
    }

    @Override
    public String toString() {
        return "UserAccountImpl{" +
                "userId=" + userId +
                ", balance=" + balance +
                '}';
    }

}
