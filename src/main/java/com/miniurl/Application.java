package com.miniurl;

import com.miniurl.constants.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableCaching
@EnableScheduling
@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@SpringBootApplication
public class Application {

    public static void main(String[] args)
    {
        log.info("Mini Url App is Lauching ...");

        log.info("App Mode : "+ AppConstants.APP_MODE);

        SpringApplication application = new SpringApplication(Application.class);
        addInitHooks(application);
        application.run(args);
    }



    public static void addInitHooks(SpringApplication application){
    //application.addListeners(new AppReadyEventListener());
    }

}
