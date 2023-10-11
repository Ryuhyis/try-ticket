package com.project.tryticket.domain.practice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PracticeSetupDTO {
   private String userID;
   private String difficultyLevel;
   private int startTime;

   public PracticeSetupDTO(String userID, String difficultyLevel, int startTime) {
      this.userID = userID;
      this.difficultyLevel = difficultyLevel;
      this.startTime = startTime;
   }
}
