package com.example.springdemo.schedule;

import com.example.springdemo.logic.Impl.AsyncLogicImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Configuration
@EnableScheduling
@Component
public class ScheduleApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleApplication.class);

    private AsyncLogicImpl asyncServiceImpl;

    @Scheduled(cron = "0 0 3 * * ?")
    public void uploadFile() {
        asyncServiceImpl = new AsyncLogicImpl();
        asyncServiceImpl.uploadFilesAsync(0);
    }
}
