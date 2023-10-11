package com.project.tryticket.schedule;

import com.project.tryticket.schedule.job.AddVirtualUserJob;
import com.project.tryticket.schedule.job.MoveWaitingToActionJob;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
@Service
@RequiredArgsConstructor
public class SchedulerService {

   private final Scheduler scheduler;

   public void addSchedule(String eventID, int numberOfUsers, int startTimeInSeconds) {
      // Quartz job 생성 및 스케줄링
      JobDataMap jobDataMap = new JobDataMap();
      jobDataMap.put("eventId", eventID);
      jobDataMap.put("numberOfUsers", numberOfUsers);

      JobDetail jobDetail = JobBuilder.newJob(AddVirtualUserJob.class)
              .usingJobData(jobDataMap)
              .build();

      
      Trigger trigger = TriggerBuilder.newTrigger()
              .startAt(Date.from(Instant.now().plusSeconds(startTimeInSeconds)))
              .build();

      try {
         scheduler.scheduleJob(jobDetail, trigger);
      } catch (SchedulerException e) {
         e.printStackTrace();
         // 에러 핸들링 로직
      }
   }

   public void moveWaitingToAction(String eventID, int startTimeInSeconds) {
      JobDataMap moveUsersJobDataMap = new JobDataMap();
      moveUsersJobDataMap.put("eventId", eventID);

      JobDetail moveUsersJobDetail = JobBuilder.newJob(MoveWaitingToActionJob.class)
              .usingJobData(moveUsersJobDataMap)
              .build();

      Trigger moveUsersTrigger = TriggerBuilder.newTrigger()
              .startAt(Date.from(Instant.now().plusSeconds(startTimeInSeconds)))
              .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(1).repeatForever())
              .build();

      try {
         scheduler.scheduleJob(moveUsersJobDetail, moveUsersTrigger);
      } catch (SchedulerException e) {
         e.printStackTrace();
         // 에러 핸들링 로직
      }
   }
}
