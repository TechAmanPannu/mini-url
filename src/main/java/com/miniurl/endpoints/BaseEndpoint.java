package com.miniurl.endpoints;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class BaseEndpoint {

    @Autowired
    HttpServletRequest request;

    @Autowired
    HttpServletResponse response;
}
