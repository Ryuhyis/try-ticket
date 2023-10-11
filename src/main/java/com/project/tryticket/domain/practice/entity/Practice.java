package com.project.tryticket.domain.practice.entity;

import com.project.tryticket.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
@Entity
@Getter
@Builder
@Table(name = "Practice")
public class Practice {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long practiceID;

   @ManyToOne
   @JoinColumn(name = "userID")
   private User user;
   private int startTime;
   private String difficultyLevel;
   private LocalDateTime createDateTime;
   private LocalDateTime updateDateTime;
   private Boolean deleted;
}
