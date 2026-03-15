package ua.epam.mishchenko.ticketbooking.model.impl;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ua.epam.mishchenko.ticketbooking.model.UserAccount;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user_accounts")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
