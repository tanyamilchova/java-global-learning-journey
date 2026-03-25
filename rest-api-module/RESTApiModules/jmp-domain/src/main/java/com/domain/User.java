package com.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

     public Long getId() {
          return id;
     }

     public void setName(String name) {
          this.name = name;
     }

     public void setSurname(String surname) {
          this.surname = surname;
     }

     public void setBirthday(LocalDate birthday) {
          this.birthday = birthday;
     }

     public void setId(Long id) {
          this.id = id;
     }

     public String getName() {
          return name;
     }

     public String getSurname() {
          return surname;
     }

     public LocalDate getBirthday() {
          return birthday;
     }

     public List<Subscription> getSubscriptions() {
          return subscriptions;
     }
}
