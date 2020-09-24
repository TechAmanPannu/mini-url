package com.miniurl.listeners;

import com.miniurl.zookeeper.LeaderSelectorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.SpringApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Component
public class AppReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private  LeaderSelectorService leaderSelectorService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("Starting leader selector");

        leaderSelectorService.begin();
        log.info("children : "+leaderSelectorService.getChildren());

    }
}
