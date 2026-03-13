package ua.epam.mishchenko.ticketbooking.model.repository;

import org.springframework.data.repository.CrudRepository;
import ua.epam.mishchenko.ticketbooking.model.impl.UserAccountImpl;

import java.util.Optional;

public interface UserAccountRepository extends CrudRepository<UserAccountImpl, Long> {

    Optional<UserAccountImpl> findByUserId(long userId);
}
