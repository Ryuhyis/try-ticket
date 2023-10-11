package com.project.tryticket.schedule.job;

import com.project.tryticket.domain.practice.service.PracticeService;
import com.project.tryticket.redis.RedisService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddVirtualUserJob implements Job {
   @Autowired
   private RedisService redisService;

   @Override
   public void execute(JobExecutionContext context) throws JobExecutionException {
      System.out.println("context = " + context);
      JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
      String eventId = jobDataMap.getString("eventId");
      int numberOfUsers = jobDataMap.getInt("numberOfUsers");

      redisService.addVirtualUsersToWaitingList(eventId, numberOfUsers);
   }
}
