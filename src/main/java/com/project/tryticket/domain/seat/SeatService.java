package com.project.tryticket.domain.seat;

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

   public void initializeSeats(String eventId, int seatCount) {
      Map<String, String> seats = new HashMap<>();
      for (int i = 0; i < seatCount; i++) {
         seats.put("seat:" + i, "available");
      }
      redisTemplate.opsForHash().putAll("eventSeats:" + eventId, seats);
   }

   public boolean reserveSeat(String eventId, String userId, String seatId) {
      Boolean wasSuccessful = redisTemplate.opsForHash().putIfAbsent("eventSeats:" + eventId, "seat:" + seatId, userId);
      return wasSuccessful != null && wasSuccessful;
   }

   public void randomReserveSeat(String eventId, String userId) {
      Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan("eventSeats:" + eventId, ScanOptions.NONE);
      while (cursor.hasNext()) {
         Map.Entry<Object, Object> entry = cursor.next();
         if ("available".equals(entry.getValue())) {
            redisTemplate.opsForHash().put("eventSeats:" + eventId, (String) entry.getKey(), userId);
            break;
         }
      }
   }
}
