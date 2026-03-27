package ua.epam.mishchenko.ticketbooking.model.impl;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user_accounts")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserAccount {

    @Id
    @Column(name = "user_id")
    private long userId;

    @Column(name = "balance")
    private double balance;


    public UserAccount() {
    }
    public UserAccount(long userId) {
        this.userId = userId;
    }

    public UserAccount(long userId, double balance) {
        this.userId = userId;
        this.balance = balance;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccount)) return false;
        UserAccount that = (UserAccount) o;
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
