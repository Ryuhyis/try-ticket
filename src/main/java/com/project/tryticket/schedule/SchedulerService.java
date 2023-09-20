package com.project.tryticket.schedule;

import com.project.tryticket.schedule.job.AddVirtualUserJob;
import com.project.tryticket.schedule.job.MoveWaitingToActionJob;
import org.quartz.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class SchedulerService {

   private Scheduler scheduler;

   public void addSchedule(String eventID, String numberOfUsers, LocalDateTime startTime) {
      // Quartz job 생성 및 스케줄링
      JobDataMap jobDataMap = new JobDataMap();
      jobDataMap.put("eventId", eventID);
      jobDataMap.put("numberOfUsers", numberOfUsers);

      JobDetail jobDetail = JobBuilder.newJob(AddVirtualUserJob.class)
              .usingJobData(jobDataMap)
              .build();

      Trigger trigger = TriggerBuilder.newTrigger()
              .startAt(Date.from(startTime.atZone(ZoneId.systemDefault()).toInstant()))
              .build();

      try {
         scheduler.scheduleJob(jobDetail, trigger);
      } catch (SchedulerException e) {
         e.printStackTrace();
         // 에러 핸들링 로직
      }
   }

   public void moveWaitingToAction(String eventID, LocalDateTime startTime) {
      JobDataMap moveUsersJobDataMap = new JobDataMap();
      moveUsersJobDataMap.put("eventId", eventID);

      JobDetail moveUsersJobDetail = JobBuilder.newJob(MoveWaitingToActionJob.class)
              .usingJobData(moveUsersJobDataMap)
              .build();

      // 예: 이벤트 시작 10초 후에 작업을 시작하여, 그 후로는 10초마다 작업을 반복
      Trigger moveUsersTrigger = TriggerBuilder.newTrigger()
              .startAt(Date.from(startTime.plusSeconds(10).atZone(ZoneId.systemDefault()).toInstant()))
              .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).repeatForever())
              .build();

      try {
         scheduler.scheduleJob(moveUsersJobDetail, moveUsersTrigger);
      } catch (SchedulerException e) {
         e.printStackTrace();
         // 에러 핸들링 로직
      }
   }
}
