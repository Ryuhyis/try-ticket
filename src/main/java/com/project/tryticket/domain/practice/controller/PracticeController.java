package com.project.tryticket.domain.practice.controller;

import com.project.tryticket.domain.practice.dto.PracticeSetupDTO;
import com.project.tryticket.domain.practice.service.PracticeService;
import com.project.tryticket.domain.seat.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/practice")
@RequiredArgsConstructor
public class PracticeController {
   private final PracticeService practiceService;
   private final SeatService seatService;

   /**
    * 연습 티켓팅 세팅
    */
   @PostMapping("/setting")
   public ResponseEntity<Void> setupPractice(@RequestBody PracticeSetupDTO practiceSetupDTO) {
      practiceService.setupPractice(practiceSetupDTO);
      return ResponseEntity.ok().build();
   }

   /**
    * 사용자가 예매하기 버튼 눌렀을때
    * @param practiceID
    * @param userId
    * @return
    */
   @PostMapping("/waiting/{practiceID}")
   public ResponseEntity<Void> addUserToQueue(
           @PathVariable String practiceID,
           @RequestParam String userId) {
      practiceService.addUserToQueue(practiceID, userId);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
   }

   /**
    * 사용자가 특정 좌석을 예매할 때 호출됩니다.
    */
   @PostMapping("/reserve-seat/{practiceID}")
   public ResponseEntity<Void> reserveSeat(
           @PathVariable String practiceID,
           @RequestParam String userId,
           @RequestParam String seatId) {
      boolean wasSuccessful = seatService.reserveSeat(practiceID, userId, seatId);
      if (wasSuccessful) {
         return ResponseEntity.ok().build();
      } else {
         return ResponseEntity.status(HttpStatus.CONFLICT).build();  // 예: 충돌 상태 코드를 사용하여 예매 실패를 나타냅니다.
      }
   }

   /*
      대기번호 확인
    */
   @GetMapping("waiting")
   public void getWaitingNumber() {

   }
}
