/*
 * Author : Hyeokwoo Kwon, Arakene
 * Filename : ApiService.java
 * Desc :
 */
package com.codrest.teriser.utils;

import com.codrest.teriser.errors.NotFoundException;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class HttpClient {

    public String get(String address, MediaType mediaType) {
        HttpURLConnection httpURLConnection = makeHttpURLConnectionOrNull(address, "GET", mediaType);
        if (Objects.isNull(httpURLConnection)) {
            throw new NotFoundException("S");
        }
        String response = receiveResponse(httpURLConnection);
        httpURLConnection.disconnect();
        return response;
    }

    public String post(String address, String requestBody, MediaType mediaType) {
        HttpURLConnection httpURLConnection = makeHttpURLConnectionOrNull(address, "POST", mediaType);
        if (Objects.isNull(httpURLConnection)) {
            throw new NotFoundException("");
        }
        sendRequest(httpURLConnection, requestBody);
        String response = receiveResponse(httpURLConnection);
        httpURLConnection.disconnect();
        return response;
    }

    private HttpURLConnection makeHttpURLConnectionOrNull(String to, String method, MediaType mediaType) {
        try {
            URL url = new URL(to);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod(method);
            httpURLConnection.setRequestProperty("Content-Type", mediaType.getType() + "/" + mediaType.getSubtype());
            httpURLConnection.setRequestProperty("Accept", mediaType.getType() + "/" + mediaType.getSubtype());
            httpURLConnection.setDoOutput(true);
            httpURLConnection.connect();
            return httpURLConnection;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void sendRequest(HttpURLConnection httpURLConnection, String data) {
        try {
            OutputStream out = httpURLConnection.getOutputStream();
            out.write(data.getBytes(StandardCharsets.UTF_8));
            out.flush();
//            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String receiveResponse(HttpURLConnection httpURLConnection) {
        try {
            byte[] bytes = httpURLConnection.getInputStream().readAllBytes();

            String result = "";
            if (bytes.length > 0) {
                result = new String(bytes);
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
