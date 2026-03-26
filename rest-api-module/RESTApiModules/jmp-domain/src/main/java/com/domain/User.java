package com.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

     private String name;
     private String surname;
     private LocalDate birthday;

     @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
     List<Subscription> subscriptions = new ArrayList<>();
}
