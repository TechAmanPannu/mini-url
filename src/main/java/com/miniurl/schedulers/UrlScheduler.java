package com.miniurl.schedulers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UrlScheduler {

//    @Scheduled(cron = "0 0 10 15 * ?", zone = "Asia/Kolkata")
    @Scheduled(fixedRate = 1000)
    public void urlExpire() {

        log.info("scheduler is running every second");


    }
}
