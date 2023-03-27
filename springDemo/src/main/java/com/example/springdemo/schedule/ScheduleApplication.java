package com.example.springdemo.schedule;

import com.example.springdemo.logic.Impl.AsyncLogicImpl;
import com.example.springdemo.service.Impl.TestServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableScheduling
@Component
public class ScheduleApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleApplication.class);

    @Autowired
    private TaskScheduler taskScheduler;

    private AsyncLogicImpl asyncServiceImpl;

    @Autowired
    private TestServiceImpl testService;

    @Qualifier("taskExecutor")
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    /**
     * 定时自动调用sftp
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public Boolean uploadFile() throws ExecutionException, InterruptedException {
        asyncServiceImpl = new AsyncLogicImpl();
        CompletableFuture<Boolean> result = CompletableFuture.supplyAsync(() -> {
            LOGGER.info("当前线程名: " + Thread.currentThread().getName());
            asyncServiceImpl.uploadFilesAsync(0);
            return true;
        }, taskExecutor);

        return result.get();
    }

    public void triggerUploadFile() {
        LOGGER.info("triggerUploadFile");
        taskScheduler.schedule(() -> {
            try {
                Boolean result = uploadFile();
                if (result == true) {
                    // TODO block是一个可以优化的点
                    String resultFromFlask = testService.test().block();
                    LOGGER.info("resultFromFlask: " + resultFromFlask);
                }
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, new Date());
    }
}
