package com.example.cloudServiceImpl.repository;

import com.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
     Page<User>findAll(Pageable pageable);

}
