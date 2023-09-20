package com.project.tryticket.domain.seat;

import com.project.tryticket.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class SeatService {
   @Autowired
   private RedisTemplate<String, String> redisTemplate;

   public boolean reserveSeat(String eventId, String userId, String seatId) {
      Boolean wasSuccessful = redisTemplate.opsForHash().putIfAbsent("eventSeats:" + eventId, "seat:" + seatId, userId);
      return wasSuccessful != null && wasSuccessful;
   }

   public String getRandomAvailableSeat(String eventId) {
      Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan("eventSeats:" + eventId, ScanOptions.scanOptions().match("available").build());
      while (cursor.hasNext()) {
         Map.Entry<Object, Object> entry = cursor.next();
         if ("available".equals(entry.getValue())) {
            return (String) entry.getKey();
         }
      }
      return null;
   }

   public void randomReserveSeat(String eventId, String userId) {
      String randomAvailableSeat = getRandomAvailableSeat(eventId);
      if (randomAvailableSeat != null) {
         reserveSeat(eventId, userId, randomAvailableSeat.split(":")[1]);
      } else {
         // 사용 가능한 좌석이 없으면 로그 작성 등의 처리 수행
      }
   }
}

