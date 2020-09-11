package com.miniUrl.endpoints;

import com.miniUrl.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class BaseEndpoint {

    @Autowired
    HttpServletRequest request;

    @Autowired
    HttpServletResponse response;
}
