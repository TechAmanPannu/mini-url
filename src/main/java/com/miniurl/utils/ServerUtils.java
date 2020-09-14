package com.miniurl.utils;

import javax.servlet.http.HttpServletRequest;

public final class ServerUtils {

    private ServerUtils(){}

    public static String getHost(HttpServletRequest request) {
        return removeServletPath(request.getRequestURL().toString(), request.getServletPath());
    }

    private static String removeServletPath(String hostWithPath, String servletPath) {

        StringBuilder sb = new StringBuilder(hostWithPath);
        sb.delete(hostWithPath.lastIndexOf(servletPath), 42);

        return sb.toString();
    }

}

