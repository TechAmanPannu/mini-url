package com.miniurl;

import com.miniurl.constants.AppConstants;
import com.miniurl.listeners.AppReadyEventListener;
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

        log.info("App Mode : "+ AppConstants.APP_MODE);
        log.info("ServerId : "+ AppConstants.SERVER_ID);

        SpringApplication application = new SpringApplication(Application.class);
        addInitHooks(application);
        application.run(args);
    }

    public static void addInitHooks(SpringApplication application){
    application.addListeners(new AppReadyEventListener());
    }

}
