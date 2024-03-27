package com.example.demo.controller;
import com.example.demo.service.GeoLocationService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.maxmind.geoip2.exception.GeoIp2Exception;

//for java 17
import jakarta.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//for java 8
//import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@RestController
@EnableAutoConfiguration
public class ClientInfoController {

    private final GeoLocationService geoLocationService;

    @Autowired
    public ClientInfoController(GeoLocationService geoLocationService) {
        this.geoLocationService = geoLocationService;
    }

    public String getIpV6(){
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet("https://api6.ipify.org?format=json");

        try {
            HttpResponse response = httpClient.execute(httpGet);
            String jsonResponse = EntityUtils.toString(response.getEntity());
            System.out.println("JSON Response: " + jsonResponse);

            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);

            String ip = jsonObject.get("ip").getAsString();
            System.out.println("IP: " + ip);
            return ip;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/client-info")
    public String getClientInfo(HttpServletRequest request) {
        String ipAddress = getIpV6();
        // System.out.println(ipAddress);
//        String ipAddress = request.getRemoteAddr();
        try {
            return geoLocationService.getGeoLocationInfo(ipAddress, request);
        } catch (IOException | GeoIp2Exception e) {
            e.printStackTrace();
            return "Error retrieving geolocation information";
        }
    }
}

