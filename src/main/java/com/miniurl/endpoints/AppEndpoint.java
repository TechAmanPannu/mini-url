package com.miniurl.endpoints;

import com.miniurl.kafka.keycounter.KeyCounterProducer;
import com.miniurl.model.response.ApiResponse;
import com.miniurl.zookeeper.keycounter.KeyCounter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/app")
public class AppEndpoint {

    @Autowired
    private KeyCounterProducer keyCounterProducer;

    @GetMapping(value = "/create/ranges")
    public ApiResponse createRanges(){

        ApiResponse response = new ApiResponse();

        keyCounterProducer.addCounterRanges();
        response.setOk(true);
        return response;
    }
}
