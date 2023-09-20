package com.project.tryticket.domain.practice.service;

import com.project.tryticket.domain.practice.dto.PracticeSetupDTO;
import com.project.tryticket.domain.practice.entity.Practice;
import com.project.tryticket.domain.practice.repository.PracticeRepository;
import com.project.tryticket.domain.ticketEvent.model.TicketEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PracticeService {

   private final PracticeRepository practiceRepository;
   public void setupPractice(PracticeSetupDTO practiceSetupDTO) {
      // Event 생성해서 eventID 가져오기
      Practice practice = practiceRepository.save(Practice.builder().build());
      // 스케줄 등록해서 가상 사용자 등록 이벤트 설정
      //      return null;
   }

   /**
    * 실제 사용자를 대기열에 넣기
    * @param practiceID
    * @param userId
    */
   public void addUserToQueue(String practiceID, String userId) {
      double score = System.currentTimeMillis();
   }
}
