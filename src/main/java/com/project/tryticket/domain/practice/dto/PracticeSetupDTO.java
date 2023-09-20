package com.project.tryticket.domain.practice.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PracticeSetupDTO {
   private String userID;
   private String difficultyLevel;
   private int startTime;
}
