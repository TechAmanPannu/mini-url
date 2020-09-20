package com.miniurl.http;

import java.io.*;
import java.net.HttpURLConnection;

public class UrlFetcher {

    /**
     * Make get request.
     *
     * @param urlString the url string
     * @return the http response
     * @throws IOException the iO exception
     */
    public static HttpResponse makeGetRequest(String urlString) throws IOException {

        return makeRequest(new HttpRequest(urlString, HttpMethod.GET));
    }

    /**
     * Make request.
     *
     * @param request the request
     * @return the http response
     * @throws IOException the iO exception
     */
    public static HttpResponse makeRequest(HttpRequest request) throws IOException {

        System.out.println("Fetching url : " + request.getUrl());

        try {
            HttpURLConnection conn = (HttpURLConnection) request.getUrl().openConnection();

            conn.setRequestMethod(request.getMethod().toString());

            if (request.getConnectionTimeOut() > 0)
                conn.setConnectTimeout(request.getConnectionTimeOut() * 1000);

            if (request.getHeaders() != null) {

                for (String key : request.getHeaders().keySet()) {
                    conn.setRequestProperty(key, request.getHeaderValue(key));
                }
            }

            if (request.getMethod() != HttpMethod.GET) {

                conn.setRequestProperty("Content-Type", request.getContentType());
                conn.setDoOutput(true);

                if (request.getPayload() != null) {

                    OutputStream os = conn.getOutputStream();
                    os.write(request.getPayload());
                    os.flush();
                } else {
                    conn.setChunkedStreamingMode(0);
                }
            }

            InputStream is = null;
            StringBuffer responseContent = null;

            try {
                is = conn.getInputStream();
            } catch (IOException e) {
                if (conn.getResponseCode() != 200) {
                    is = conn.getErrorStream();
                }
            }

            if (is != null) {
                BufferedReader in = new BufferedReader(new InputStreamReader(is));

                String inputLine;
                responseContent = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    responseContent.append(inputLine);
                }
                in.close();
            }

            HttpResponse httpResponse = new HttpResponse();
            httpResponse.setStatusCode(conn.getResponseCode());
            httpResponse.setResponseContent(responseContent == null ? null : responseContent.toString());

            httpResponse.setHeaders(conn.getHeaderFields());

            conn.disconnect();

            return httpResponse;

        } catch (Exception e) {
            System.out.println(e.getMessage()+ e);
            return null;
        }
    }
}
