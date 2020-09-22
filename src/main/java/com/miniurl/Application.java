package com.miniurl;

import com.miniurl.utils.ObjUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableCaching
@EnableScheduling
@SpringBootApplication
public class Application {

    public static void main(String[] args)
    {
        log.info("Mini Url App is Lauching ...");
        log.info("Environmental variables :"+ObjUtil.getJson(System.getenv()));
        SpringApplication.run(Application.class, args);
    }

}
