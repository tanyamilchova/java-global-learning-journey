package ua.epam.mishchenko.ticketbooking.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.epam.mishchenko.ticketbooking.model.impl.UserAccount;

import java.util.Optional;


public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    Optional<UserAccount> findById(long userId);
}
