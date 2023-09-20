package com.project.tryticket.domain.practice.service;

import com.project.tryticket.domain.practice.dto.PracticeSetupDTO;
import com.project.tryticket.domain.practice.entity.Practice;
import com.project.tryticket.domain.practice.repository.PracticeRepository;
import com.project.tryticket.domain.ticketEvent.model.TicketEvent;
import com.project.tryticket.redis.RedisService;
import com.project.tryticket.schedule.SchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PracticeService {

   private final PracticeRepository practiceRepository;
   private final SchedulerService schedulerService;
   private final RedisService redisService;
   public void setupPractice(PracticeSetupDTO practiceSetupDTO) {
      // Event 생성해서 practice 가져오기
      Practice practice = practiceRepository.save(Practice.builder().difficultyLevel(practiceSetupDTO.getDifficultyLevel()).startTime(practiceSetupDTO.getStartTime()).build());
      // 스케줄 등록해서 가상 사용자 등록 이벤트 설정
      schedulerService.addSchedule(practice.getPracticeID().toString(), practice.getDifficultyLevel(), practice.getStartTime());
      // 해당 시간 이후 일성 시간마다 대기열 -> 작업열 이동
      schedulerService.moveWaitingToAction(practice.getPracticeID().toString(), practice.getStartTime());
   }

   /**
    * 실제 사용자를 대기열에 넣기
    * @param practiceID
    * @param userId
    */
   public void addUserToQueue(String practiceID, String userId) {
      double score = System.currentTimeMillis();
      redisService.addUserToQueue(practiceID, userId);
   }
}
