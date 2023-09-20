package com.project.tryticket.domain.user.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "USER")
public class User {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long userID;

   private String username;
   private String password;
   private String email;
   private String socialID;
   private LocalDateTime createDateTime;
   private LocalDateTime updateDateTime;
   private Boolean deleted;

}
