package com.project.tryticket.domain.practice.service;

import com.project.tryticket.domain.practice.dto.PracticeSetupDTO;
import com.project.tryticket.domain.practice.entity.Practice;
import com.project.tryticket.domain.practice.repository.PracticeRepository;
import com.project.tryticket.redis.RedisService;
import com.project.tryticket.schedule.SchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PracticeService {

   private final PracticeRepository practiceRepository;
   private final SchedulerService schedulerService;
   private final RedisService redisService;
   public Practice setupPractice(PracticeSetupDTO practiceSetupDTO) {

      // 다음 코드를 추가하여 difficultylevel에 따라 가상 사용자 수를 설정합니다.
      int virtualUserCount = getVirtualUserCountByDifficulty(practiceSetupDTO.getDifficultyLevel());
      Practice practice = Practice.builder().difficultyLevel(practiceSetupDTO.getDifficultyLevel()).startTime(practiceSetupDTO.getStartTime()).build();
      // Event 생성해서 practice 가져오기
      Practice newPractice = practiceRepository.save(practice);
      // 스케줄 등록해서 가상 사용자 등록 이벤트 설정
      schedulerService.addSchedule(practice.getPracticeID().toString(), virtualUserCount, practice.getStartTime());
      // 해당 시간 이후 일성 시간마다 대기열 -> 작업열 이동
      schedulerService.moveWaitingToAction(practice.getPracticeID().toString(), practice.getStartTime());

      return practice;
   }

   private int getVirtualUserCountByDifficulty(String difficultyLevel) {
      switch (difficultyLevel.toLowerCase()) {
         case "high":
            return 1000;  // 예: 높은 난이도로 1000명의 가상 사용자 설정
         case "medium":
            return 500;   // 예: 중간 난이도로 500명의 가상 사용자 설정
         case "low":
            return 100;   // 예: 낮은 난이도로 100명의 가상 사용자 설정
         default:
            throw new IllegalArgumentException("Invalid difficulty level provided");
      }
   }


   /**
    * 실제 사용자를 대기열에 넣기
    * @param practiceID
    * @param userId
    */
   public void addUserToQueue(int practiceID, int userId) {
      redisService.addUserToQueue(practiceID, userId);
   }
}
