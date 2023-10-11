package com.project.tryticket.redis;

import com.project.tryticket.domain.seat.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RedisService {
   private final RedisTemplate<String, Object> redisTemplate;
   private final SeatService seatService;
   /**
    * 가상 사용자를 일정 시간으로 만들어 대기열에 넣기
    * @param eventId
    * @param numberOfUsers
    * @return
    */
   public void addVirtualUsersToWaitingList(String eventId, int numberOfUsers) {
      for (int i = 0; i < numberOfUsers; i++) {
         double score = System.currentTimeMillis() - new Random().nextInt(10000); // 예: 랜덤한 시간으로 score 설정
         System.out.println("virtualuser " + i + " : score");
         redisTemplate.opsForZSet().add("waiting:" + eventId, "virtualUser" + i, score);
      }
   }

   /**
    * 실제 사용자를 대기열에 넣기
    * @param eventId
    * @param userId
    */
   public void addUserToQueue(int eventId, int userId) {
      double score = System.currentTimeMillis();
      redisTemplate.opsForZSet().add("waiting:" + eventId, userId, score);
   }

   /**
    * 특정 간격으로 사용자들을 대기열에서 작업 대기열로 이동
    * @param eventId
    * @return
    */
   public void moveUsersToActionQueue(String eventId) {
      redisTemplate.execute(new SessionCallback<Object>() {
         @Override
         public Object execute(RedisOperations operations) throws DataAccessException {
            operations.multi(); // transaction start
            Set<Object> usersToMove = redisTemplate.opsForZSet().range("waiting:" + eventId, 0, 10); // 상위 5명의 사용자를 가져옴
            for (Object user : usersToMove) {
               String userId = (String) user;
               redisTemplate.opsForZSet().add("actionQueue:" + eventId, userId, System.currentTimeMillis());
               redisTemplate.opsForZSet().remove("waiting:" + eventId, user); // 사용자를 대기열에서 제거

               if (userId.startsWith("virtualUser")) {
                  seatService.randomReserveSeat(eventId, userId);
               }
            }
            return operations.exec(); // transaction end
         }
      });
   }

   public void reserveRandomSeat(String eventId, String userId) {
      // 랜덤 로직을 사용하여 사용 가능한 좌석 중 하나를 선택
      String randomAvailableSeat = seatService.getRandomAvailableSeat(eventId);
      if (randomAvailableSeat != null) {
         // 선택된 좌석을 예매
         seatService.reserveSeat(eventId, userId, randomAvailableSeat);
      } else {
         // 사용 가능한 좌석이 없으면 로그 작성 등의 처리 수행
      }
   }

   /**
    * 대기 시간이 긴 사용자들을 대기열에서 제거
    * @param eventId
    * @param timeoutInMillis
    */
   public void removeOldUsersFromQueue(String eventId, long timeoutInMillis) {
      double minScore = 0;
      double maxScore = System.currentTimeMillis() - timeoutInMillis;
      redisTemplate.opsForZSet().removeRangeByScore("waiting:" + eventId, minScore, maxScore);
   }
}
