package ua.epam.mishchenko.ticketbooking.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.epam.mishchenko.ticketbooking.model.impl.UserAccountImpl;

import java.util.Optional;


public interface UserAccountRepository extends JpaRepository<UserAccountImpl, Long> {

    Optional<UserAccountImpl> findById(long userId);
}
