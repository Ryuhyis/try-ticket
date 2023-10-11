package com.project.tryticket.schedule.job;

import com.project.tryticket.redis.RedisService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MoveWaitingToActionJob implements Job {
   @Autowired
   private RedisService redisService;

   @Override
   public void execute(JobExecutionContext context) throws JobExecutionException {
      JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
      String eventId = jobDataMap.getString("eventId");
      redisService.moveUsersToActionQueue(eventId);
   }
}

