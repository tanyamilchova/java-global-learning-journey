package com.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "subscriptions")
public class Subscription {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

     @ManyToOne
     @JoinColumn(name = "user_id")
     private User user;

     private LocalDate startDate;

     public void setStartDate(LocalDate startDate) {
          this.startDate = startDate;
     }
     public User getUser() {
          return user;
     }

     public Long getId() {
          return id;
     }

     public LocalDate getStartDate() {
          return startDate;
     }

     public void setId(Long id) {
          this.id = id;
     }

     public void setUser(User user) {
          this.user = user;
     }
}
