package com.project.tryticket.domain.practice.controller;

import com.project.tryticket.domain.practice.dto.PracticeSetupDTO;
import com.project.tryticket.service.PracticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/practice")
@RequiredArgsConstructor
public class PracticeController {
   private final PracticeService practiceService;

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
      return ResponseEntity.ok().build();
   }

   /*
      대기번호 확인
    */
   @GetMapping("waiting")
   public void getWaitingNumber() {

   }
}
