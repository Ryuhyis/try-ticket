package com.project.tryticket.domain.practice.controller;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.tryticket.domain.practice.dto.PracticeSetupDTO;
import com.project.tryticket.domain.practice.service.PracticeService;
import com.project.tryticket.domain.seat.SeatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class PracticeControllerTest {

   @Autowired
   private MockMvc mockMvc;

   @MockBean
   private PracticeService practiceService;

   @MockBean
   private SeatService seatService;

   @Test
   public void testSetupPractice() throws Exception {
      PracticeSetupDTO practiceSetupDTO = new PracticeSetupDTO("1", "상", 10);

      mockMvc.perform(post("/practice/setting")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(new ObjectMapper().writeValueAsString(practiceSetupDTO)))
              .andExpect(status().isOk());

      verify(practiceService, times(1)).setupPractice(practiceSetupDTO);
   }

   @Test
   public void testAddUserToQueue() throws Exception {
      String practiceID = "testPracticeID";
      String userId = "testUserID";

      mockMvc.perform(post("/waiting/{practiceID}", practiceID)
                      .param("userId", userId))
              .andExpect(status().isNoContent());

      verify(practiceService, times(1)).addUserToQueue(practiceID, userId);
   }

   @Test
   public void testReserveSeatSuccessful() throws Exception {
      String practiceID = "testPracticeID";
      String userId = "testUserID";
      String seatId = "testSeatID";

      when(seatService.reserveSeat(anyString(), anyString(), anyString())).thenReturn(true);

      mockMvc.perform(post("/reserve-seat/{practiceID}", practiceID)
                      .param("userId", userId)
                      .param("seatId", seatId))
              .andExpect(status().isOk());

      verify(seatService, times(1)).reserveSeat(practiceID, userId, seatId);
   }

   @Test
   public void testReserveSeatConflict() throws Exception {
      String practiceID = "testPracticeID";
      String userId = "testUserID";
      String seatId = "testSeatID";

      when(seatService.reserveSeat(anyString(), anyString(), anyString())).thenReturn(false);

      mockMvc.perform(post("/reserve-seat/{practiceID}", practiceID)
                      .param("userId", userId)
                      .param("seatId", seatId))
              .andExpect(status().isConflict());

      verify(seatService, times(1)).reserveSeat(practiceID, userId, seatId);
   }
}
